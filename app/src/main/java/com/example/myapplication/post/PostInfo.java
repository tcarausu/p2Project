package com.example.myapplication.post;

public class PostInfo {

    private String imageDesc;
    private String imageIngred;
    private String imageRecipe;
    private String imageURL;

    public PostInfo(){

    }

    public PostInfo(String imageDesc, String imageIngred, String imageRecipe, String imageURL){
        this.imageDesc = imageDesc;
        this.imageIngred = imageIngred;
        this.imageRecipe = imageRecipe;
        this.imageURL = imageURL;
    }


    public String getImageURL() {
        return imageURL;
    }

    public String getImageDesc() {
        return imageDesc;
    }

    public String getImageRecipe() {
        return imageRecipe;
    }

    public String getImageIngred() {
        return imageIngred;
    }
}
