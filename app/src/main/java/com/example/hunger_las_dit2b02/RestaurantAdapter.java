package com.example.hunger_las_dit2b02;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private List<Restaurant> restaurantList;
    private List<Restaurant> filteredRestaurantList;

    public RestaurantAdapter(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
        this.filteredRestaurantList = new ArrayList<>(restaurantList);
    }

    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
        notifyDataSetChanged();
    }
    public void filterRestaurants(String query) {
        filteredRestaurantList.clear();
        if (query.isEmpty()) {
            // display all restaurants initially
            filteredRestaurantList.addAll(restaurantList);
        } else {
            // Filter deals based on the search query (case-insensitive)
            for (Restaurant restaurant : restaurantList) {
                if (restaurant.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredRestaurantList.add(restaurant);
                    Log.d("SearchAdapter", "Added restaurant: " + restaurant.getName());
                }
            }
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_card_layout, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        if (position < filteredRestaurantList.size()) {
            Restaurant restaurant = filteredRestaurantList.get(position);
            // Use Glide to load images from URLs
            Glide.with(holder.itemView.getContext())
                    .load(restaurant.getImageUrl())
                    .into(holder.imgRestaurant);

            // Bind other data to views
            holder.txtRestaurantName.setText(restaurant.getName());
            holder.txtRating.setText(String.valueOf(restaurant.getRating()));
            holder.txtTotalRatings.setText("(" + restaurant.getTotalRatings() + ")");
        }
        else {
            Restaurant restaurant = restaurantList.get(position);
            // Use Glide to load images from URLs
            Glide.with(holder.itemView.getContext())
                    .load(restaurant.getImageUrl())
                    .into(holder.imgRestaurant);

            // Bind other data to views
            holder.txtRestaurantName.setText(restaurant.getName());
            holder.txtRating.setText(String.valueOf(restaurant.getRating()));
            holder.txtTotalRatings.setText("(" + restaurant.getTotalRatings() + ")");
        }
    }

    @Override
    public int getItemCount() {
        return filteredRestaurantList.size() > 0 ? filteredRestaurantList.size() : restaurantList.size();
    }

    static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        ImageView imgRestaurant;
        TextView txtRestaurantName;
        TextView txtRating;
        TextView txtTotalRatings;

        RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);

            imgRestaurant = itemView.findViewById(R.id.imgRestaurant);
            txtRestaurantName = itemView.findViewById(R.id.txtRestaurantName);
            txtRating = itemView.findViewById(R.id.txtRating);
            txtTotalRatings = itemView.findViewById(R.id.txtReviewCount);
        }
    }
}
