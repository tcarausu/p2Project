package com.example.myapplication.post;

public class Post {

    private String mProfileImgUrl;
    private String mUsername;
    private String mDescription;
    private String mFoodImgUrl;
    private long mLikes;
    private String mRecipe;
    private String mIngredients;
    private String userId;

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


    //empty constructor for firebase
    public Post(){ }

    public Post(String mProfileImgUrl,String mUsername,String mDescription, String mFoodImgUrl, long mLikes, String mRecipe, String mIngredients, String userId) {
        if(mDescription.trim().equals("")){
            mDescription = "No description available";
        }
        this.mProfileImgUrl = mProfileImgUrl;
        this.mUsername = mUsername;
        this.mDescription = mDescription;
        this.mFoodImgUrl = mFoodImgUrl;
        this.mLikes = mLikes;
        this.mRecipe = mRecipe;
        this.mIngredients = mIngredients;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
