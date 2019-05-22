package com.example.myapplication.models;

import com.google.firebase.database.Exclude;

public  class  User {

    private String about;
    private String display_name;
    private String username;
    private long followers;
    private long following;
    private long posts;
    private String profile_photo;
    private String website;

    private long phone_number;
    private String email;

    public User(String about, String display_name, String username, String email, long phone_number,
                long followers, long following, long posts, String profile_photo, String website) {


        synchronized(User.class){
        this.about = about;
        this.display_name = display_name;
        this.username = username;
        this.followers = followers;
        this.following = following;
        this.posts = posts;
        this.profile_photo = profile_photo;
        this.website = website;
        this.phone_number = phone_number;
        this.email = email;
        }
    }

    public User() {
    }

    public String getAbout() {
        return about;
    }
    @Exclude
    public void setAbout(String about) {
        this.about = about;
    }

    public String getDisplay_name() {
        return display_name;
    }
    @Exclude
    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getUsername() {
        return username;
    }

    @Exclude
    public void setUsername(String username) {
        this.username = username;
    }

    public long getFollowers() {
        return followers;
    }
    @Exclude
    public void setFollowers(long followers) {
        this.followers = followers;
    }

    public long getFollowing() {
        return following;
    }
    @Exclude
    public void setFollowing(long following) {
        this.following = following;
    }

    public long getPosts() {
        return posts;
    }
    @Exclude
    public void setPosts(long posts) {
        this.posts = posts;
    }

    public String getProfile_photo() {
        return profile_photo;
    }
    @Exclude
    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getEmail() {
        return email;
    }
    @Exclude
    public void setEmail(String email) {
        this.email = email;
    }

    public long getPhone_number() {
        return phone_number;
    }
    @Exclude
    public void setPhone_number(long phone_number) {
        this.phone_number = phone_number;
    }

    public String getWebsite() {
        return website;
    }
    @Exclude
    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", phone_number=" + phone_number +
                ", email=" + email +
                ", display_name='" + display_name + '\'' +
                ", about='" + about + '\'' +
                ", followers=" + followers +
                ", following=" + following +
                ", posts=" + posts +
                ", profile_photo='" + profile_photo + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}
