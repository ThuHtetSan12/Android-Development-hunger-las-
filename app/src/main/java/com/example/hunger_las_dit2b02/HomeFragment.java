package com.example.hunger_las_dit2b02;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Fetch data from Firebase
        fetchDataFromFirebase();

        return view;
    }

    private void fetchDataFromFirebase() {
            // Assume you have a "posts" collection in Firestore
            db.collection("posts")
                    .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Post> postList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Map Firebase fields to Post object
                            String caption = document.getString("caption");
                            Timestamp timestamp = document.getTimestamp("date");
                            long restaurantIdLong = document.getLong("restaurant_id");

                            // Convert restaurantId to a String
                            String restaurantId = String.valueOf(restaurantIdLong);

                            String imageUrl = document.getString("imageUrl");
                            int rating = document.getLong("rating").intValue(); // Assuming rating is stored as a number
                            String userId = document.getString("userid");

                            // Convert timestamp to a formatted date string
                            String date = timestamp != null ? new SimpleDateFormat("h:mm a", Locale.US).format(timestamp.toDate()) : "";

                            // You may need to fetch additional data (e.g., user info, restaurant info) based on userId and restaurantId

                            // Hardcode like and comment counts for now
                            int likeCount = 0; // Hardcoded value
                            int commentCount = 0; // Hardcoded value

                            // Create Post object
                            User user = new User("Userid", "email","username"); // You may need to fetch user info
                            Post post = new Post(user, restaurantId, imageUrl, likeCount, commentCount, caption, date, rating);
                            postList.add(post);
                        }

                        // Check if the fragment is still attached to the activity before updating UI
                        if (isAdded()) {
                            // Set the adapter with the retrieved data
                            postAdapter = new PostAdapter(postList, getContext());
                            recyclerView.setAdapter(postAdapter);
                        }
                    } else {
                        // Handle errors
                    }
                });
    }
}
