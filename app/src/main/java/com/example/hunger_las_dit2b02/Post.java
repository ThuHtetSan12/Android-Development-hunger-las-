package com.example.hunger_las_dit2b02;

public class Post {
    private User user;
    private String restaurantInfo;
    private String imageUrl; // Change the type to String
    private int likeCount;
    private int commentCount;
    private String caption;
    private String date;
    private int rating;

    public Post(User user, String restaurantInfo, String imageUrl, // Change the parameter type
                int likeCount, int commentCount, String caption, String date, int rating) {
        this.user = user;
        this.restaurantInfo = restaurantInfo;
        this.imageUrl = imageUrl;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.caption = caption;
        this.date = date;
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public int getRating() {
        return rating;
    }

    public String getRestaurantInfo() {
        return restaurantInfo;
    }

    public String getImageUrl() { // Change the method name
        return imageUrl;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public String getCaption() {
        return caption;
    }

    public String getDate() {
        return date;
    }
}
