package com.example.hunger_las_dit2b02;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";
    private RestaurantAdapter restaurantAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        EditText searchEditText = view.findViewById(R.id.txtSearch);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewRestaurants);
        RestaurantAdapter adapter = new RestaurantAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filter restaurants based on the search query
                restaurantAdapter.filterRestaurants(charSequence.toString());
            }

            public void afterTextChanged(Editable editable) {
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("restaurants")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Restaurant> restaurantList = new ArrayList<>();

                        for (QueryDocumentSnapshot restaurantDocument : task.getResult()) {
                            String restaurantName = restaurantDocument.getString("restaurant_name");
                            String imageUrl = restaurantDocument.getString("imageUrl");

                            db.collection("posts")
                                    .whereEqualTo("restaurant_id", restaurantDocument.getLong("restaurant_id"))
                                    .get()
                                    .addOnCompleteListener(postsTask -> {
                                        if (postsTask.isSuccessful()) {
                                            double totalRating = 0;
                                            int totalRatingsCount = 0;

                                            for (QueryDocumentSnapshot postDocument : postsTask.getResult()) {
                                                // Map Firebase fields to Post object
                                                double rating = postDocument.getDouble("rating");
                                                totalRating += rating;
                                                totalRatingsCount++;
                                            }

                                            double averageRating = totalRating / totalRatingsCount;

                                            // Create Restaurant object with the average rating
                                            Restaurant restaurant = new Restaurant(imageUrl, restaurantName, averageRating, totalRatingsCount);
                                            restaurantList.add(restaurant);

                                            // Set up and attach the adapter after fetching data
                                            restaurantAdapter = new RestaurantAdapter(restaurantList);
                                            recyclerView.setAdapter(restaurantAdapter);
                                        } else {
                                            Log.w(TAG, "Error getting posts documents.", postsTask.getException());
                                        }
                                    });

                        }
                    } else {
                        Log.w(TAG, "Error getting restaurants documents.", task.getException());
                    }
                });

        return view;
    }
}
