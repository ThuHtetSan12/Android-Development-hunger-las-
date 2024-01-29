package com.example.hunger_las_dit2b02;

public class Post {
    private User1 user;
    private String restaurantInfo;
    private int postImageResId;
    private int likeCount;
    private int commentCount;
    private String caption;
    private String date;
    private int rating;

    public Post(User1 user, String restaurantInfo, int postImageResId,
                int likeCount, int commentCount, String caption, String date, int rating) {
        this.user = user;
        this.restaurantInfo = restaurantInfo;
        this.postImageResId = postImageResId;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.caption = caption;
        this.date = date;
        this.rating = rating;
    }

    public User1 getUser() {
        return user;
    }

    public int getRating() {
        return rating;
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