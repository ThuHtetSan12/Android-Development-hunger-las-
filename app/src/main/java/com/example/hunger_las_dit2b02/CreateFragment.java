package com.example.hunger_las_dit2b02;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateFragment extends Fragment {

    private EditText txtRestaurantInput, txtDescriptionInput;
    private RatingBar ratingBar;
    private Button btnPost;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        txtRestaurantInput = view.findViewById(R.id.txtRestaurantInput);
        txtDescriptionInput = view.findViewById(R.id.txtDescriptionInput);
        ratingBar = view.findViewById(R.id.ratingBar);
        btnPost = view.findViewById(R.id.btnPost);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadReview();
            }
        });

        return view;
    }

    private void uploadReview() {
        String restaurantName = txtRestaurantInput.getText().toString().trim();
        String description = txtDescriptionInput.getText().toString().trim();
        float rating = ratingBar.getRating();

        if (restaurantName.isEmpty() || description.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Assuming you have the restaurant ID and UID, replace "1" and "wfzzvcHzQyXAm0uoQwTvLKaU0pf2" with actual values
        String restaurantId = "1";
        String uid = "wfzzvcHzQyXAm0uoQwTvLKaU0pf2";

        // Create a review object
        Map<String, Object> review = new HashMap<>();
        review.put("description", description);
        review.put("rating", rating);
        review.put("restaurant_id", restaurantId);
        review.put("uid", uid);

        // Add the review to the "reviews" collection
        firestore.collection("reviews")
                .add(review)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Review posted successfully", Toast.LENGTH_SHORT).show();
                            // Handle Fragment transaction if needed
                        } else {
                            Toast.makeText(getContext(), "Error posting review", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
