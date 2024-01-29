package com.example.hunger_las_dit2b02;

// Restaurant.java
public class Restaurant {
    private int imageResourceId;
    private String name;
    private double rating;
    private int totalRatings;

    public Restaurant(int imageResourceId, String name, double rating, int totalRatings) {
        this.imageResourceId = imageResourceId;
        this.name = name;
        this.rating = rating;
        this.totalRatings = totalRatings;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getName() {
        return name;
    }

    public double getRating() {
        return rating;
    }

    public int getTotalRatings() {
        return totalRatings;
    }
}
