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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_list_item_post,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int index) {
        Log.d(TAG, "onBindViewHolder: Called");

        final Post postCurrent = mPosts.get(index);

        Glide.with(mContext)
                .load(postCurrent.getmProfileImgUrl())
                .fitCenter()
                .centerCrop()
                .into(viewHolder.mProfilePic);
        viewHolder.mUserName.setText(postCurrent.getmUsername());

        viewHolder.mDescription.setText(postCurrent.getmDescription());

        Glide.with(mContext)
                .load(postCurrent.getmFoodImgUrl())
                .fitCenter()
                .centerCrop()
                .into(viewHolder.mFoodImg);


        viewHolder.mLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nrOfLikes ="" + postCurrent.getmLikes();
                viewHolder.mToolbarExpasionText.setText(nrOfLikes);
            }
        });

        viewHolder.mComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // implementation for displaying the comments for each post
            }
        });

        viewHolder.mRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.mToolbarExpasionText.setText(postCurrent.getmRecipe());
            }
        });

        viewHolder.mIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.mToolbarExpasionText.setText(postCurrent.getmIngredients());
            }
        });
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
        ImageButton mLikes;
        ImageButton mComments;
        ImageButton mRecipe;
        ImageButton mIngredients;
        FrameLayout mPostToolbarBtnsExpansionContainer;
        TextView mToolbarExpasionText;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mParentLayout = itemView.findViewById(R.id.parentLayoutID);
            mProfilePic = itemView.findViewById(R.id.userProfilePicID);
            mUserName = itemView.findViewById(R.id.userNameID);
            mDescription = itemView.findViewById(R.id.postDescriptionID);
            mFoodImg = itemView.findViewById(R.id.foodImgID);
            mLikes = itemView.findViewById(R.id.likesBtnID);
            mComments = itemView.findViewById(R.id.commentsBtnID);
            mRecipe = itemView.findViewById(R.id.recipeBtnID);
            mIngredients = itemView.findViewById(R.id.ingredientsBtnID);
            mPostToolbarBtnsExpansionContainer = itemView.findViewById(R.id.toolbarExpansionContainerID);
            mToolbarExpasionText = itemView.findViewById(R.id.toolbarExpansionTextID);
        }
    }
}