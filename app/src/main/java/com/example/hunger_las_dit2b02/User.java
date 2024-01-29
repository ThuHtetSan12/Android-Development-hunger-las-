package com.example.hunger_las_dit2b02;

// User.java
public class User {
    private int profilePictureResId;
    private String username;

    public User(int profilePictureResId, String username) {
        this.profilePictureResId = profilePictureResId;
        this.username = username;
    }

    public int getProfilePictureResId() {
        return profilePictureResId;
    }

    public String getUsername() {
        return username;
    }
}