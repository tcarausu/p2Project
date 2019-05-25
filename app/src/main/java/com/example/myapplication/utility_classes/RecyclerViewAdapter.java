package com.example.myapplication.utility_classes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.models.Post;
import com.example.myapplication.models.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private Context mContext;
    private List<Post> mPosts;

    public RecyclerViewAdapter(Context mContext, List<Post> mPosts) {
        this.mContext = mContext;
        this.mPosts = mPosts;
    }

    public void setUserForPost(Post post, User user) {
        post.setUser(user);
    }

    private User getUserForPost(Post post) {
        return post.getUser();
    }

    public void setPostsList(List<Post> mPosts) {
        this.mPosts = mPosts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_list_item_post, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int index) {
        viewHolder.mOptions.setVisibility(View.INVISIBLE);
        Log.d(TAG, "onBindViewHolder: Called");

        final Post postCurrent = mPosts.get(index);
        User postUser = getUserForPost(postCurrent);

        if (postUser != null) {
            Glide.with(mContext)
                    .load(postUser.getProfile_photo())
                    .fitCenter()
                    .centerCrop()
                    .into(viewHolder.mProfilePic);
            viewHolder.mUserName.setText(postUser.getUsername());

        } else {
            Glide.with(mContext)
                    .load(R.drawable.my_avatar)
                    .fitCenter()
                    .centerCrop()
                    .into(viewHolder.mProfilePic);
            viewHolder.mUserName.setText(R.string.loading);

        }

        viewHolder.mDescription.setText(postCurrent.getmDescription());

        Glide.with(mContext)
                .load(postCurrent.getmFoodImgUrl())
                .fitCenter()
                .centerCrop()
                .into(viewHolder.mFoodImg);


        viewHolder.mLikes.setOnClickListener(v -> {
            int nrOfLikes =  postCurrent.getLikes().size();
            if (nrOfLikes > 1){
            viewHolder.post_likes.setText(nrOfLikes+ " Likes");
            }
            else
            viewHolder.post_likes.setText(nrOfLikes+ " Like");
        });

        viewHolder.mComments.setOnClickListener(v -> {
            // implementation for displaying the comments for each post
        });

        viewHolder.mRecipe.setOnClickListener(v -> viewHolder.post_likes.setText(postCurrent.getmRecipe()));

        viewHolder.mIngredients.setOnClickListener(v -> viewHolder.post_likes.setText(postCurrent.getmIngredients()));
        viewHolder.post_TimeStamp.setText(postCurrent.getDate_created());

    }



    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout mParentLayout;
        CircleImageView mProfilePic;
        TextView mUserName;
        TextView mDescription;
        ImageView mFoodImg;
        ImageButton mLikes, mComments, mRecipe, mIngredients , mOptions;
        FrameLayout mPostToolbarBtnsExpansionContainer;
        TextView mToolbarExpasionText, post_likes, post_TimeStamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mParentLayout = itemView.findViewById(R.id.parentLayoutID);
            mProfilePic = itemView.findViewById(R.id.userProfilePicID);
            mUserName = itemView.findViewById(R.id.userNameID);
            mDescription = itemView.findViewById(R.id.postDescriptionID);
            mFoodImg = itemView.findViewById(R.id.foodImgID);
            mLikes = itemView.findViewById(R.id.likes_button);
            mOptions = itemView.findViewById(R.id.personal_post_options_menu);
            mComments = itemView.findViewById(R.id.commentsBtnID);
            mRecipe = itemView.findViewById(R.id.recipeBtnID);
            mIngredients = itemView.findViewById(R.id.ingredientsBtnID);
            mPostToolbarBtnsExpansionContainer = itemView.findViewById(R.id.toolbarExpansionContainerID);
            post_likes = itemView.findViewById(R.id.post_likes);
            post_TimeStamp = itemView.findViewById(R.id.post_TimeStamp);

        }
    }
}