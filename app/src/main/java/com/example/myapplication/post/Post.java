package com.example.myapplication.post;

public class Post {

    private String mDescription;
    private String mFoodImgUrl;
    private long mLikes;
    private String mRecipe;
    private String mIngredients;
    private String mUserId;


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

    public long getmLikes() {
        return mLikes;
    }

    public void setmLikes(long mLikes) {
        this.mLikes = mLikes;
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

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }


    //empty constructor for firebase
    public Post(){ }

    public Post(String mDescription, String mFoodImgUrl, long mLikes, String mRecipe, String mIngredients, String mUserId) {
        if(mDescription.trim().equals("")){
            mDescription = "No description available";
        }

        this.mDescription = mDescription;
        this.mFoodImgUrl = mFoodImgUrl;
        this.mLikes = mLikes;
        this.mRecipe = mRecipe;
        this.mIngredients = mIngredients;
        this.mUserId = mUserId;
    }


}
