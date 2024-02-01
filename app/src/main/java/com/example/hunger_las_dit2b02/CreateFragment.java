package com.example.hunger_las_dit2b02;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateFragment extends Fragment {

    private View rootView;
    private EditText txtRestaurantInput;
    private EditText txtDescriptionInput;
    private RatingBar ratingBar;
    private Button btnPost;
    private Button btnAddPhoto;
    private FirebaseAuth auth;
    private FirebaseFirestore fstore;
    private Uri imageUrl;
    private StorageReference storageReference;

    private static final String USER_ID_KEY = "user_id";
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_create, container, false);

        txtRestaurantInput = rootView.findViewById(R.id.txtRestaurantInput);
        txtDescriptionInput = rootView.findViewById(R.id.txtDescriptionInput);
        ratingBar = rootView.findViewById(R.id.ratingBar);
        btnPost = rootView.findViewById(R.id.btnPost);
        btnAddPhoto = rootView.findViewById(R.id.btnAddPhoto);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPostButtonClick();
                Log.d("CreateFragment", "Post button pressed");
            }
        });
        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        // Initialize Firebase components
        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("posts");

        return rootView;
    }

    private String getSavedUserId() {
        if (getActivity() != null) {
            SharedPreferences pref = getActivity().getSharedPreferences("user_info", MODE_PRIVATE);
            return pref.getString(USER_ID_KEY, "");
        } else {
            return "";
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUrl = data.getData();
            ImageView imageView = rootView.findViewById(R.id.layoutImagePreview);
            imageView.setImageURI(imageUrl);
        }
    }

    private void onPostButtonClick() {
        String restaurantNameInput = txtRestaurantInput.getText().toString();

        if (restaurantNameInput.isEmpty() || txtDescriptionInput.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUrl == null) {
            Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore.getInstance().collection("restaurants")
                .whereEqualTo("restaurant_name", restaurantNameInput)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            // No matching restaurant found
                            Toast.makeText(getContext(), "No matching restaurant found", Toast.LENGTH_SHORT).show();
                        } else {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Long restaurantId = document.getLong("restaurant_id");
                                if (restaurantId != null) {
                                    saveReviewWithRestaurantId(restaurantId);
                                } else {
                                    Log.e("CreateFragment", "restaurant_id is null for document with restaurant_name: " + restaurantNameInput);
                                }
                            }
                        }
                    } else {
                        // Handle errors
                        Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveReviewWithRestaurantId(Long restaurantId) {
        String description = txtDescriptionInput.getText().toString();
        float rating = ratingBar.getRating();

        Map<String, Object> reviewData = new HashMap<>();
        reviewData.put("caption", description);
        reviewData.put("date", new Date());
        reviewData.put("rating", rating);
        reviewData.put("restaurant_id", restaurantId);
        reviewData.put("userid", getSavedUserId());

        if (imageUrl != null) {
            // Upload the image to Firebase Storage
            uploadImage(reviewData);
        } else {
            saveReviewToFirestore(reviewData);
        }
    }

    private void uploadImage(final Map<String, Object> reviewData) {
        StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUrl));

        fileReference.putFile(imageUrl)
                .addOnSuccessListener(taskSnapshot -> {
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        reviewData.put("imageUrl", uri.toString());
                        saveReviewToFirestore(reviewData);
                        Log.d("CreateFragment", "Upload Success: " + uri.toString());
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveReviewToFirestore(Map<String, Object> reviewData) {
        imageUrl = null;
        FirebaseFirestore.getInstance().collection("posts")
                .add(reviewData)
                .addOnSuccessListener(documentReference -> {
                    Log.d("CreateFragment", "Review saved successfully");
                    txtRestaurantInput.getText().clear();
                    txtDescriptionInput.getText().clear();
                    ratingBar.setRating(0);
                    ImageView imageView = rootView.findViewById(R.id.layoutImagePreview);
                    imageView.setImageURI(null);

                    Toast.makeText(getContext(), "Review uploaded!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("CreateFragment", "Error saving review: " + e.getMessage());
                });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
