package com.example.hunger_las_dit2b02;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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

                            // Fetch restaurant details based on restaurantId
                            fetchRestaurantDetails(restaurantId, (restaurantInfo) -> {
                                // Handle the result asynchronously
                                // Extract restaurant details
                                String restaurantName = (String) restaurantInfo.get("restaurantName");
                                GeoPoint restaurantLocation = (GeoPoint) restaurantInfo.get("restaurantLocation");

                                // Hardcode like and comment counts for now
                                int likeCount = 0; // Hardcoded value
                                int commentCount = 0; // Hardcoded value

                                // Inside fetchDataFromFirebase method, before creating the Post object
                                fetchUserDetails(userId, (username, email) -> {
                                    // Update the User object with the fetched username and email
                                    User user = new User(userId, email, username);

                                    // Continue with the existing code to create the Post object
                                    Post post = new Post(user, restaurantName, imageUrl, likeCount, commentCount, caption, date, rating);
                                    postList.add(post);

                                    // Check if the fragment is still attached to the activity before updating UI
                                    if (isAdded()) {
                                        // Set the adapter with the retrieved data
                                        postAdapter = new PostAdapter(postList, getContext());
                                        recyclerView.setAdapter(postAdapter);
                                    }
                                });


                            });
                        }
                    } else {
                        // Handle errors
                    }
                });

    }

    private void fetchRestaurantDetails(String restaurantId, RestaurantInfoCallback callback) {
        // Fetch restaurant details based on restaurantId
        db.collection("restaurants")
                .whereEqualTo("restaurant_id", Long.parseLong(restaurantId))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        QueryDocumentSnapshot restaurantDocument = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);

                        // Retrieve restaurant details
                        String restaurantName = restaurantDocument.getString("restaurant_name");
                        GeoPoint restaurantLocation = restaurantDocument.getGeoPoint("location");

                        // Create a Map to hold restaurant details
                        Map<String, Object> restaurantInfo = new HashMap<>();
                        restaurantInfo.put("restaurantName", restaurantName);
                        restaurantInfo.put("restaurantLocation", restaurantLocation);

                        // Call the callback with the result
                        callback.onCallback(restaurantInfo);
                    } else {
                        // Handle errors or set default values
                    }
                });
    }

    // Define a callback interface
    interface RestaurantInfoCallback {
        void onCallback(Map<String, Object> restaurantInfo);
    }

    private void fetchUserDetails(String userId, UserDetailsCallback callback) {
        // Fetch user details based on userId from the "users" collection
        db.collection("users")
                .whereEqualTo("userid", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        QueryDocumentSnapshot userDocument = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);

                        // Retrieve username and email
                        String username = userDocument.getString("username");
                        String email = userDocument.getString("email");

                        // Call the callback with the result
                        callback.onCallback(username, email);
                    } else {
                        // Handle errors or set default values
                    }
                });
    }

    // Define a callback interface for the username and email
    interface UserDetailsCallback {
        void onCallback(String username, String email);
    }


    private void openMap(GeoPoint location) {
        // Create a Uri from the GeoPoint (latitude and longitude)
        String uri = "geo:" + location.getLatitude() + "," + location.getLongitude() + "?z=15";

        // Create an Intent with the Uri
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        mapIntent.setPackage("com.google.android.apps.maps"); // Specify the package name for Google Maps

        // Check if there's a map app available to handle the Intent
        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            // Handle the case where no map app is available
            Toast.makeText(getContext(), "No map app found", Toast.LENGTH_SHORT).show();
        }
    }
}
