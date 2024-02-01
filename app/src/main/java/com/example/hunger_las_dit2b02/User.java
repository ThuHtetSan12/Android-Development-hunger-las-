package com.example.hunger_las_dit2b02;

import java.util.List;

public class User {
    private String userid, email, username, imgUrl, name, bio;

    private List<String> followers;
    private List<String> following;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }

    public void addFollower(String follower) {
        this.followers.add(follower);
    }

    public void addFollowing(String following) {
        this.following.add(following);
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
