package com.example.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

public class User implements Parcelable {

    private String about;
    private String display_name;
    private String username;
    private long followers;
    private long following;
    private long nrOfPosts;
    private String profile_photo;
    private String website;

    private long phone_number;
    private String email;


    public User() {
    }

    public User(String about, String display_name, String username,
                long followers, long following, long nrOfPosts,
                String profile_photo, String website,
                long phone_number, String email) {

        synchronized (User.class) {
            this.about = about;
            this.display_name = display_name;
            this.username = username;
            this.followers = followers;
            this.following = following;
            this.nrOfPosts = nrOfPosts;
            this.profile_photo = profile_photo;
            this.website = website;
            this.phone_number = phone_number;
            this.email = email;
        }
    }

    protected User(Parcel in) {
        about = in.readString();
        display_name = in.readString();
        username = in.readString();
        followers = in.readLong();
        following = in.readLong();
        nrOfPosts = in.readLong();
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

    public long getNrOfPosts() {
        return nrOfPosts;
    }

    @Exclude
    public void setNrPosts(long posts) {
        this.nrOfPosts = posts;
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
                "about='" + about + '\'' +
                ", display_name='" + display_name + '\'' +
                ", username='" + username + '\'' +
                ", followers=" + followers +
                ", following=" + following +
                ", nrOfPosts=" + nrOfPosts +
                ", profile_photo='" + profile_photo + '\'' +
                ", website='" + website + '\'' +
                ", phone_number=" + phone_number +
                ", email='" + email + '\'' +
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
        dest.writeLong(followers);
        dest.writeLong(following);
        dest.writeLong(nrOfPosts);
        dest.writeString(profile_photo);
        dest.writeString(website);
        dest.writeLong(phone_number);
        dest.writeString(email);
    }
}
