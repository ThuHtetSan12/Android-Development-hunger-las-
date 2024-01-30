package com.example.hunger_las_dit2b02;

public class Deal {
    private String restaurantName;
    private String imagePath;
    private String dealInformation;

    public Deal(String restaurantName, String imagePath, String dealInformation) {
        this.restaurantName = restaurantName;
        this.imagePath = imagePath;
        this.dealInformation = dealInformation;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getDealInformation() {
        return dealInformation;
    }
}

