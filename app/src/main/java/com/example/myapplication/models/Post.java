package com.example.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

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

    public Post(String mDescription, String mFoodImgUrl, String mRecipe,
                String mIngredients, String userId, String postId, String date_created, List<Like> likes) {

        synchronized (Post.class) {
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
    }


    protected Post(Parcel in) {
        mDescription = in.readString();
        mFoodImgUrl = in.readString();
        mRecipe = in.readString();
        mIngredients = in.readString();
        userId = in.readString();
        postId = in.readString();
        date_created = in.readString();
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

    public synchronized String getUserId() {
        return userId;
    }

    @Exclude
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getmDescription() {
        return mDescription;
    }
    @Exclude
    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmFoodImgUrl() {
        return mFoodImgUrl;
    }

    @Exclude
    public void setmFoodImgUrl(String mFoodImgUrl) {
        this.mFoodImgUrl = mFoodImgUrl;
    }

    public String getmRecipe() {
        return mRecipe;
    }
    @Exclude
    public void setmRecipe(String mRecipe) {
        this.mRecipe = mRecipe;
    }

    public String getmIngredients() {
        return mIngredients;
    }
    @Exclude
    public void setmIngredients(String mIngredients) {
        this.mIngredients = mIngredients;
    }

    public List<Like> getLikes() {
        return likes;
    }
    @Exclude
    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public String getDate_created() {
        return date_created;
    }
    @Exclude
    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getPostId() {
        return postId;
    }
    @Exclude
    public void setPostId(String postId) {
        this.postId = postId;
    }

    public User getUser() {
        return user;
    }
    @Exclude
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
        dest.writeString(mFoodImgUrl);
        dest.writeString(mRecipe);
        dest.writeString(mIngredients);
        dest.writeString(userId);
        dest.writeString(postId);
        dest.writeString(date_created);
    }


}
