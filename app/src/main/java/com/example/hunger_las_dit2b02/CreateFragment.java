package com.example.hunger_las_dit2b02;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateFragment extends Fragment {

    private EditText txtRestaurantInput;
    private EditText txtDescriptionInput;
    private RatingBar ratingBar;
    private Button btnPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        txtRestaurantInput = view.findViewById(R.id.txtRestaurantInput);
        txtDescriptionInput = view.findViewById(R.id.txtDescriptionInput);
        ratingBar = view.findViewById(R.id.ratingBar);
        btnPost = view.findViewById(R.id.btnPost);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPostButtonClick();
                Log.d("CreateFragment", "Post button pressed");
            }
        });

        return view;
    }

    private void onPostButtonClick() {
        // Retrieve the entered restaurant name
        String RestaurantNameInput = txtRestaurantInput.getText().toString();

        // Check if any of the fields are empty
        if (RestaurantNameInput.isEmpty() || txtDescriptionInput.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Query "restaurants" collection to find matching restaurant name
        FirebaseFirestore.getInstance().collection("restaurants")
                .whereEqualTo("restaurant_name", RestaurantNameInput)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // If a match is found, retrieve restaurant_id
                            Long restaurantId = document.getLong("restaurant_id");

                            if (restaurantId != null) {
                                saveReviewWithRestaurantId(restaurantId);
                            } else {
                                // Handle the case where restaurant_id is null
                                Log.e("CreateFragment", "restaurant_id is null for document with restaurant_name: " + RestaurantNameInput);
                            }
                        }
                    } else {
                        // Handle errors
                    }
                });
    }

    private void saveReviewWithRestaurantId(Long restaurantId) {
        String description = txtDescriptionInput.getText().toString();
        float rating = ratingBar.getRating();

        Map<String, Object> reviewData = new HashMap<>();
        reviewData.put("caption", description);
        reviewData.put("date", new Date());
        reviewData.put("imageUrl", "https://firebasestorage.googleapis.com/v0/b/hunger-las.appspot.com/o/posts%2Fsoup.jpg?alt=media&token=f7a43f20-ed59-4e34-9d79-51745a9611f2");  // Replace with the actual image URL
        reviewData.put("rating", rating);
        reviewData.put("restaurant_id", restaurantId);
        reviewData.put("userid", "0IvXvT92nebBGsPtdJ0aatSax7H2");  // hardcoded rn

        // Save the review data in the "posts" collection
        FirebaseFirestore.getInstance().collection("posts")
                .add(reviewData)
                .addOnSuccessListener(documentReference -> {
                    // Handle success
                    Log.d("CreateFragment", "Review saved successfully");
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Log.e("CreateFragment", "Error saving review: " + e.getMessage());
                });
    }
}