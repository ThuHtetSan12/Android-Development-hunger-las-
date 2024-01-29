package com.example.hunger_las_dit2b02;

public class User {
    private String userid, email, username, imgUrl;

    // Private constructor to prevent instantiation
    private User() {
    }

    public User(String userid, String email, String username){
        this.userid = userid;
        this.email=email;
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
