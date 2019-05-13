package com.example.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Post implements Parcelable {

    private static final String TAG = "Post";
    private String mProfileImgUrl;
    private String mUsername;
    private String mDescription;
    private String mFoodImgUrl;
    private String mRecipe;
    private String mIngredients;
    private String date_created;
    private String userId;
    private String postId;
    private List<Like> likes;

    public Post() {
    }

    public Post(String mProfileImgUrl, String mUsername, String mDescription,
                String mFoodImgUrl, String mRecipe, String mIngredients, String userId, String postId, String date_created, List<Like> likes) {
        if (mDescription.trim().equals("")) {
            mDescription = "No description available";
        }
        this.mProfileImgUrl = mProfileImgUrl;
        this.mUsername = mUsername;
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
        mProfileImgUrl = in.readString();
        mFoodImgUrl = in.readString();
        mUsername = in.readString();
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

    public String getmProfileImgUrl() {
        return mProfileImgUrl;
    }

    public void setmProfileImgUrl(String mProfileImgUrl) {
        this.mProfileImgUrl = mProfileImgUrl;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
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

    @Override
    public String toString() {
        return "Post{" +
                "mProfileImgUrl='" + mProfileImgUrl + '\'' +
                ", mUsername='" + mUsername + '\'' +
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
        dest.writeString(mProfileImgUrl);
        dest.writeString(mFoodImgUrl);
        dest.writeString(mUsername);
        dest.writeString(mIngredients);
        dest.writeString(mRecipe);
        dest.writeString(userId);
        dest.writeString(postId);
    }

}
