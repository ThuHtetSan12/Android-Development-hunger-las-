package com.example.hunger_las_dit2b02;

// RestaurantDetailsActivity.java
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RestaurantDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        // Set up UI elements and handle back button click
        initUI();
    }

    private void initUI() {
        // Find UI elements
        ImageView backButton = findViewById(R.id.backButton);
        ImageView restaurantImage = findViewById(R.id.restaurantImage);
        TextView restaurantName = findViewById(R.id.restaurantName);
        ImageView favouriteIcon = findViewById(R.id.favouriteIcon);
        ImageView locationIcon = findViewById(R.id.locationIcon);
        TextView restaurantLocation = findViewById(R.id.restaurantLocation);
        ImageView phoneIcon = findViewById(R.id.phoneIcon);
        TextView restaurantContactNumber = findViewById(R.id.restaurantContactNumber);
        TextView openingHours = findViewById(R.id.openingHours);

        // Set up data (replace with actual data)
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Close the current activity when back button is clicked
            }
        });

        // TODO: Set up other UI elements with actual restaurant details
    }
}
