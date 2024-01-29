package com.example.hunger_las_dit2b02;

// RestaurantAdapter.java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private List<Restaurant> restaurantList;

    public RestaurantAdapter(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_card_layout, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);

        // Bind data to views
        holder.imgRestaurant.setImageResource(restaurant.getImageResourceId());
        holder.txtRestaurantName.setText(restaurant.getName());
        holder.txtRating.setText(String.valueOf(restaurant.getRating()));
        holder.txtTotalRatings.setText("(" + restaurant.getTotalRatings() + ")");
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
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
            txtTotalRatings = itemView.findViewById(R.id.txtTotalRatings);
        }
    }
}
