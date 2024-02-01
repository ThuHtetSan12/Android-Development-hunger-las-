package com.example.hunger_las_dit2b02;

public class Review {
    private String reviewId;
    private String description;
    private float rating;
    private int restaurantId;
    private String uid;

    public Review() {
    }

    public Review(String reviewId, String description, float rating, int restaurantId, String uid) {
        this.reviewId = reviewId;
        this.description = description;
        this.rating = rating;
        this.restaurantId = restaurantId;
        this.uid = uid;
    }

    public String getReviewId() {
        return reviewId;
    }

    public String getDescription() {
        return description;
    }

    public float getRating() {
        return rating;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public String getUid() {
        return uid;
    }
}

