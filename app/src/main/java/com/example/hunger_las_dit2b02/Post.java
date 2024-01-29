package com.example.hunger_las_dit2b02;

// Post.java
public class Post {
    private int profilePictureResId;
    private String username;
    private String restaurantInfo;
    private int postImageResId;
    private int likeCount;
    private int commentCount;
    private String caption;
    private String date;

    public Post(int profilePictureResId, String username, String restaurantInfo,
                int postImageResId, int likeCount, int commentCount,
                String caption, String date) {
        this.profilePictureResId = profilePictureResId;
        this.username = username;
        this.restaurantInfo = restaurantInfo;
        this.postImageResId = postImageResId;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.caption = caption;
        this.date = date;
    }

    // Add getters and setters as needed
    // ...

    public int getProfilePictureResId() {
        return profilePictureResId;
    }

    public String getUsername() {
        return username;
    }

    public String getRestaurantInfo() {
        return restaurantInfo;
    }

    public int getPostImageResId() {
        return postImageResId;
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