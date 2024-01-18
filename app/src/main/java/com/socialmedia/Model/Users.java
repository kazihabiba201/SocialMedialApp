package com.socialmedia.Model;

public class Users {
    private String Username;
    private String Bio;
    private String Email;
    private String Password;
    private String profile;
    private String userId;
    private int followerCount;



    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public Users(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    private String lastMsg;

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Users(String profile, int followerCount, String cover_image) {
        this.profile = profile;
        this.userId = userId;
        this.followerCount = followerCount;
        this.cover_image = cover_image;
    }

    private String  cover_image;

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public Users(String username, String bio, String email, String password) {
        Username = username;
        Bio = bio;
        Email = email;
        Password = password;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }
    public Users(){

    }
}
