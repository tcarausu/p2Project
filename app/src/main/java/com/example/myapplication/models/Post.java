package com.example.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Post implements Parcelable {

    private String mDescription;
    private String mFoodImgUrl;
    private String mRecipe;
    private String mIngredients;
    private String date_created;
    private String userId;
    private String postId;
    private List<Like> likes;
    private User user;

    public Post() {
    }

    public Post(String mDescription,
                String mFoodImgUrl, String mRecipe, String mIngredients, String userId, String postId, String date_created, List<Like> likes) {
        if (mDescription.trim().equals("")) {
            mDescription = "No description available";
        }
        this.mDescription = mDescription;
        this.date_created = date_created;
        this.mFoodImgUrl = mFoodImgUrl;
        this.mRecipe = mRecipe;
        this.mIngredients = mIngredients;
        this.userId = userId;
        this.postId = postId;
        this.likes = likes;
    }


    protected Post(Parcel in) {
        mDescription = in.readString();
        mFoodImgUrl = in.readString();
        mIngredients = in.readString();
        mRecipe = in.readString();
        date_created = in.readString();
        userId = in.readString();
        postId = in.readString();

    }

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmFoodImgUrl() {
        return mFoodImgUrl;
    }

    public void setmFoodImgUrl(String mFoodImgUrl) {
        this.mFoodImgUrl = mFoodImgUrl;
    }

    public String getmRecipe() {
        return mRecipe;
    }

    public void setmRecipe(String mRecipe) {
        this.mRecipe = mRecipe;
    }

    public String getmIngredients() {
        return mIngredients;
    }

    public void setmIngredients(String mIngredients) {
        this.mIngredients = mIngredients;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Post{" +
                ", mDescription='" + mDescription + '\'' +
                ", mFoodImgUrl='" + mFoodImgUrl + '\'' +
                ", mRecipe='" + mRecipe + '\'' +
                ", mIngredients='" + mIngredients + '\'' +
                ", mIngredients='" + mIngredients + '\'' +
                ", postId='" + postId + '\'' +
                ", userId='" + userId + '\'' +
                ", likes=" + likes +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mDescription);
        dest.writeString(date_created);
        dest.writeString(mFoodImgUrl);
        dest.writeString(mIngredients);
        dest.writeString(mRecipe);
        dest.writeString(userId);
        dest.writeString(postId);
    }

}