package com.example.hunger_las_dit2b02;

public class Deal {
    private String restaurantName;
    private int restaurantImage;
    private String dealInformation;

    public Deal(String restaurantName, int restaurantImage, String dealInformation) {
        this.restaurantName = restaurantName;
        this.restaurantImage = restaurantImage;
        this.dealInformation = dealInformation;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public int getRestaurantImage() {
        return restaurantImage;
    }

    public String getDealInformation() {
        return dealInformation;
    }
}
