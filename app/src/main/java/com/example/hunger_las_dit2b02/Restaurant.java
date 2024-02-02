package com.example.hunger_las_dit2b02;

// Restaurant.java
public class Restaurant {
    private String imageUrl;
    private String name;
    private double rating;
    private int totalRatings;

    public Restaurant(String imageUrl, String name, double rating, int totalRatings) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.rating = rating;
        this.totalRatings = totalRatings;
    }

    public String getImageUrl() {
        return imageUrl;
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
