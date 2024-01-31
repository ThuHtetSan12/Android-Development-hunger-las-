package com.example.hunger_las_dit2b02;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EditProfile extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseAuth auth;
    private FirebaseFirestore fstore;
    private FirebaseStorage storage;
    private SharedPreferences pref;
    private static final String USER_ID_KEY = "user_id";
    private ImageView profileImage;
    private ImageView editProfileIcon;
    private EditText editTextName;
    private EditText editTextUsername;
    private EditText editTextBio;
    private Button saveChangesButton;

    private Uri imageUrl;
    private StorageReference storageReference;
    private CircularProgressIndicator progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //To have the back button!!
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_edit_profile);

        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        pref = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        profileImage = findViewById(R.id.profileImage);
        editProfileIcon = findViewById(R.id.editProfileIcon);
        editTextName = findViewById(R.id.editTextName);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextBio = findViewById(R.id.editTextBio);
        saveChangesButton = findViewById(R.id.saveChangesButton);
        progressBar = findViewById(R.id.progressBar);

        storageReference = storage.getReference("profile_images/" + auth.getCurrentUser().getUid());
        loadDataFromFirestore();


        editProfileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadDataFromFirestore() {
        String userId = getSavedUserId();

        if (!userId.isEmpty()) {
            DocumentReference userRef = fstore.collection("users").document(userId);

            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult() != null && task.getResult().exists()) {
                        // Retrieve user data and set it to respective views
                        User user = task.getResult().toObject(User.class);

                        if (user != null) {
                            editTextName.setText(user.getName());
                            editTextUsername.setText(user.getUsername());
                            editTextBio.setText(user.getBio());

                            // Load profile image using Glide
                            if (user.getImgUrl() != null) {
                                Glide.with(EditProfile.this)
                                        .load(user.getImgUrl())
                                        .apply(RequestOptions.circleCropTransform())
                                        .into(profileImage);
                            }
                        }
                    }
                } else {
                    Log.e("ProfileActivity", "Error getting user data", task.getException());
                    Toast.makeText(EditProfile.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void openImagePicker() {
        // Implement code to open image picker
        // This can be done using Intent or any image picker library
        // In this example, I am using Intent for simplicity
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUrl = data.getData();
            profileImage.setImageURI(imageUrl);
        }
    }

    private void saveChanges() {
        String name = editTextName.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String bio = editTextBio.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Name and username are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUrl != null) {
            // Upload the image to Firebase Storage
            uploadImage();
        } else {
            // If no image is selected, save other data directly
            saveUserData(null);
        }
    }

    private void uploadImage() {
        progressBar.setVisibility(View.VISIBLE);

        StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUrl));

        fileReference.putFile(imageUrl)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the uploaded image URL
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        saveUserData(uri.toString());
                        Toast.makeText(EditProfile.this, "Upload  Success: " + uri.toString(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    });
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(EditProfile.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }

    private void saveUserData(String imageUrl) {
        String userId = auth.getCurrentUser().getUid();

        DocumentReference userRef = fstore.collection("users").document(userId);

        userRef.update("name", editTextName.getText().toString(),
                        "username", editTextUsername.getText().toString(),
                        "bio", editTextBio.getText().toString(),
                        "imgUrl", imageUrl)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        setResult(Activity.RESULT_OK);
                        Toast.makeText(EditProfile.this, "Changes saved successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(EditProfile.this, "Failed to save changes", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private String getSavedUserId() {

            pref = getSharedPreferences("user_info", MODE_PRIVATE);
            return pref.getString(USER_ID_KEY, "");
    }

}
