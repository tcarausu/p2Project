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
    private String userId;
    private String postId;
    private String date_created;
    private List<Like> likeList;
    private List<Comment> commentList;
    private User user;
    private boolean isCommentsBtnPressed;

    public Post() {
    }

    public Post(String mDescription, String mFoodImgUrl, String mRecipe,
                String mIngredients, String userId, String postId,
                String date_created,
                List<Like> likeList, List<Comment> commentList) {

        synchronized (Post.class) {
            if (mDescription.trim().equals("")) {

                mDescription = "No description available";
            }
            this.mDescription = mDescription;
            this.mFoodImgUrl = mFoodImgUrl;
            this.mRecipe = mRecipe;
            this.mIngredients = mIngredients;
            this.userId = userId;
            this.postId = postId;
            this.date_created = date_created;
            this.likeList = likeList;
            this.commentList = commentList;
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
        commentList = in.createTypedArrayList(Comment.CREATOR);
        likeList = in.createTypedArrayList(Like.CREATOR);
        user = in.readParcelable(User.class.getClassLoader());
        isCommentsBtnPressed = in.readByte() != 0;
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
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

    public List<Like> getLikeList() {
        return likeList;
    }
    @Exclude
    public void setLikeList(List<Like> likeList) {
        this.likeList = likeList;
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

    @Exclude
    public void addComment(Comment comment){
        commentList.add(comment);
    }

    public List<Comment> getCommentList() {

        return commentList;
    }
    @Exclude
    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public boolean isCommentsBtnPressed() {
        return isCommentsBtnPressed;
    }

    public void setCommentsBtnPressed(boolean commentsBtnPressed) {
        isCommentsBtnPressed = commentsBtnPressed;
    }

    @Override
    public String toString() {
        return "Post{" +
                "mDescription='" + mDescription + '\'' +
                ", mFoodImgUrl='" + mFoodImgUrl + '\'' +
                ", mRecipe='" + mRecipe + '\'' +
                ", mIngredients='" + mIngredients + '\'' +
                ", userId='" + userId + '\'' +
                ", postId='" + postId + '\'' +
                ", date_created='" + date_created + '\'' +
                ", likeList=" + likeList +
                ", commentList=" + commentList +
                ", user=" + user +
                ", isCommentsBtnPressed=" + isCommentsBtnPressed +
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
        dest.writeTypedList(commentList);
        dest.writeTypedList(likeList);
        dest.writeParcelable(user, flags);
        dest.writeByte((byte) (isCommentsBtnPressed ? 1 : 0));
    }
}
