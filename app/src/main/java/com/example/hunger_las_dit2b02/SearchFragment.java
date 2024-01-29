package com.example.hunger_las_dit2b02;

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
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private RestaurantAdapter restaurantAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewRestaurants);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Create sample restaurant data
        List<Restaurant> restaurantList = createSampleRestaurants();

        // Create and set the adapter
        restaurantAdapter = new RestaurantAdapter(restaurantList);
        recyclerView.setAdapter(restaurantAdapter);

        return view;
    }

    private List<Restaurant> createSampleRestaurants() {
        List<Restaurant> restaurantList = new ArrayList<>();

        // Create sample Restaurant objects
        Restaurant restaurant1 = new Restaurant(R.drawable.logo, "Restaurant Name 1", 4.5, 293);
        Restaurant restaurant2 = new Restaurant(R.drawable.logo, "Restaurant Name 2", 4.2, 150);
        Restaurant restaurant3 = new Restaurant(R.drawable.logo, "Restaurant Name 3", 4.8, 500);

        // Add restaurants to the list
        restaurantList.add(restaurant1);
        restaurantList.add(restaurant2);
        restaurantList.add(restaurant3);

        return restaurantList;
    }
}
