package com.example.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

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

    public User(String about, String display_name,
                String username, String email, long phone_number,
                long followers, long following, long posts,
                String profile_photo, String website
    ) {
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

    public User() {
    }

    protected User(Parcel in) {
        about = in.readString();
        display_name = in.readString();
        username = in.readString();
        followers = in.readLong();
        following = in.readLong();
        posts = in.readLong();
        profile_photo = in.readString();
        website = in.readString();
        phone_number = in.readLong();
        email = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getFollowers() {
        return followers;
    }

    public void setFollowers(long followers) {
        this.followers = followers;
    }

    public long getFollowing() {
        return following;
    }

    public void setFollowing(long following) {
        this.following = following;
    }

    public long getNrPosts() {
        return posts;
    }

    public void setNrPosts(long posts) {
        this.posts = posts;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(long phone_number) {
        this.phone_number = phone_number;
    }

    public String getWebsite() {
        return website;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(about);
        dest.writeString(display_name);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(String.valueOf(phone_number));
        dest.writeString(String.valueOf(followers));
        dest.writeString(String.valueOf(following));
        dest.writeString(String.valueOf(posts));
        dest.writeString(profile_photo);
        dest.writeString(website);
    }
}
