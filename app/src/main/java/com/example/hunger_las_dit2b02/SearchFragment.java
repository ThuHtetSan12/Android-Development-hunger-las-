package com.example.hunger_las_dit2b02;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

// SearchFragment.java
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private RestaurantAdapter restaurantAdapter;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewRestaurants);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Fetch restaurant data from Firebase
        fetchRestaurantData();

        return view;
    }

    private void fetchRestaurantData() {
        // Assume you have a "restaurants" collection in Firestore
        db.collection("restaurants")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Restaurant> restaurantList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Map Firebase fields to Restaurant object
                            String restaurantName = document.getString("restaurant_name");
                            String imageUrl = document.getString("imageUrl");

                            // Hardcoded values for rating and count
                            double rating = 4.5;
                            int count = 293;

                            // Create Restaurant object
                            Restaurant restaurant = new Restaurant(imageUrl, restaurantName, rating, count);
                            restaurantList.add(restaurant);
                        }

                        // Set up and attach the adapter after fetching data
                        restaurantAdapter = new RestaurantAdapter(restaurantList);
                        recyclerView.setAdapter(restaurantAdapter);
                    } else {
                        // Handle errors
                    }
                });
    }
}
