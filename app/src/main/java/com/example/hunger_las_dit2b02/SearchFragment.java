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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

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
                // Filter deals based on the search query
                adapter.filterRestaurants(charSequence.toString());
            }

            public void afterTextChanged(Editable editable) {
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("restaurants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
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
                                Log.d(TAG, "Retrieved restaurant: " + restaurantName);
                            }

                            adapter.setRestaurantList(restaurantList);

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        return view;
    }
}
