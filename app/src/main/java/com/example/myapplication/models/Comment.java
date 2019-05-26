package com.example.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {

    private String comment;
    private String userId;
    private String username;
    private String userProfilePhoto;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserProfilePhoto() {
        return userProfilePhoto;
    }

    public void setUserProfilePhoto(String userProfilePhoto) {
        this.userProfilePhoto = userProfilePhoto;
    }

//    protected Comment(Parcel in) {
//        comment = in.readString();
//        userId = in.readString();
//    }

//    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
//        @Override
//        public Comment createFromParcel(Parcel in) {
//            return new Comment(in);
//        }
//
//        @Override
//        public Comment[] newArray(int size) {
//            return new Comment[size];
//        }
//    };

    protected Comment(Parcel in) {
        comment = in.readString();
        userId = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public Comment(String comment,String userId) {
        this.comment = comment;
        this.userId = userId;
    }

    public Comment(){ }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(comment);
        dest.writeString(userId);
    }

//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(comment);
//        dest.writeString(userId);
//    }
}