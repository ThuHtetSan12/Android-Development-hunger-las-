package com.example.hunger_las_dit2b02;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePassword extends AppCompatActivity {

    private SharedPreferences pref;
    private static final String USER_ID_KEY = "user_id";
    private FirebaseAuth auth;
    private FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        //To have the back button!!
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.change_password_setting);

        Button submitButton = findViewById(R.id.submitbutton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When the button is clicked, call the changePassword method
                changePassword();
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

    // Method to change the password
    private void changePassword() {
        EditText currentPasswordEditText = findViewById(R.id.editTextCurrentPassword);
        EditText newPasswordEditText = findViewById(R.id.editTextNewPassword);
        EditText confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword);

        String currentPassword = currentPasswordEditText.getText().toString();
        String newPassword = newPasswordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();


        if (TextUtils.isEmpty(currentPassword)) {
            Toast.makeText(ChangePassword.this, "Please enter the current password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(newPassword)) {
            Toast.makeText(ChangePassword.this, "Please enter a new password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(ChangePassword.this, "Please confirm the new password", Toast.LENGTH_SHORT).show();
            return;
        }
        // Ensure the new password and confirm password match
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(ChangePassword.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {

            auth.signInWithEmailAndPassword(user.getEmail(), currentPassword)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // User successfully reauthenticated, update the password
                            user.updatePassword(newPassword)
                                    .addOnCompleteListener(updateTask -> {
                                        if (updateTask.isSuccessful()) {
                                            // Password change successful
                                            Toast.makeText(ChangePassword.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Password change failed
                                            Toast.makeText(ChangePassword.this, "Failed to change password", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // Reauthentication failed
                            Toast.makeText(ChangePassword.this, "Incorrect current password", Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    }
}
