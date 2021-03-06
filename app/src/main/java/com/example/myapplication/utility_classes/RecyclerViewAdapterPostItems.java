package com.example.myapplication.utility_classes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.home.CommentsActivity;
import com.example.myapplication.models.Post;
import com.example.myapplication.models.User;
import com.example.myapplication.user_profile.ViewPostFragmentNewsFeed;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class RecyclerViewAdapterPostItems extends RecyclerView.Adapter<RecyclerViewAdapterPostItems.ViewHolder> {
    private static final String TAG = "AdapterPostItems";

    // Member variables
    private Context mContext;
    // Get the recycler view that contains this adapter so that we are able to scroll automatically
    private RecyclerView mRecyclerView;
    private List<Post> mPosts;

    private ViewPostFragmentNewsFeed viewPost = new ViewPostFragmentNewsFeed();
    private FirebaseAuth mAuth = FirebaseMethods.getAuth();
    private FirebaseDatabase mFirebaseDatabase = FirebaseMethods.getmFirebaseDatabase();
    private DatabaseReference mPostsRef, mUserRef;


    // Constants
    private final int FOCUS_ANY = -350;

    public RecyclerViewAdapterPostItems(Context context, List<Post> posts
//            , RecyclerView recyclerView
    ) {
        this.mContext = context;
        this.mPosts = posts;
//        this.mRecyclerView = recyclerView;
    }

    public void setPostsList(List<Post> mPosts) {
        this.mPosts = mPosts;
    }

    public void setUserForPost(Post post, User user) {
        post.setUser(user);
    }

    private User getUserForPost(Post post) {
        return post.getUser();
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

        final Post currentPost = mPosts.get(index);
        User postUser = getUserForPost(currentPost);
        mUserRef = mFirebaseDatabase.getReference("users");
        mPostsRef = mFirebaseDatabase.getReference("posts");


        if (postUser != null) {
            Glide.with(mContext)
                    .load(postUser.getProfile_photo())
                    .fitCenter()
                    .centerCrop()
                    .into(viewHolder.mProfilePic);
            viewHolder.mUserName.setText(postUser.getDisplay_name());

        } else {
            Glide.with(mContext)
                    .load(R.drawable.my_avatar)
                    .fitCenter()
                    .centerCrop()
                    .into(viewHolder.mProfilePic);
            viewHolder.mUserName.setText(R.string.loading);

        }

        viewHolder.mDescription.setText(currentPost.getmDescription());

        Glide.with(mContext)
                .load(currentPost.getmFoodImgUrl())
                .fitCenter()
                .centerCrop()
                .into(viewHolder.mFoodImg);


        // Button Listeners ***************************************************
        viewHolder.mLikes.setOnClickListener(v -> {
            if (currentPost.getUserId().equals(mAuth.getCurrentUser().getUid())) {
                viewPost.toggleLikes(mPostsRef,
                        mUserRef,
                        mAuth.getCurrentUser().getUid(),
                        currentPost.getPostId(),
                        mAuth.getCurrentUser().getUid(),
                        viewHolder.mLikes
                );
            } else {
                viewPost.toggleLikes(mPostsRef,
                        mUserRef,
                        currentPost.getUserId(),
                        currentPost.getPostId(),
                        mAuth.getCurrentUser().getUid(),
                        viewHolder.mLikes
                );
            }
            if (viewHolder.mLikedByCurrentUser) {
                int nrOfLikes = currentPost.getLikeList().size();
                if (nrOfLikes > 1) {
                    viewHolder.likes_overview.setText(nrOfLikes + " Likes");
                } else
                    viewHolder.likes_overview.setText(nrOfLikes + " Like");
            } else {
                int nrOfLikes = currentPost.getLikeList().size();
                if (nrOfLikes > 1) {
                    viewHolder.likes_overview.setText(nrOfLikes + " Likes");
                } else
                    viewHolder.likes_overview.setText(nrOfLikes + " Like");
            }
        });

        viewHolder.mComments.setOnClickListener(v -> {
            // implementation for displaying the comments for each post
            Intent intent = new Intent(mContext, CommentsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("currentPost", currentPost);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        });

//        viewHolder.mRecipe.setOnClickListener(v -> {
//            // implementation for displaying the recipe for each post
//            viewHolder.mToolbarExpasionText.setText(currentPost.getmRecipe());
//            viewHolder.focusExpandable(viewHolder, FOCUS_ANY);
//        });
//
//        viewHolder.mIngredients.setOnClickListener(v -> {
//            // implementation for displaying the ingredients for each post
//            viewHolder.mToolbarExpasionText.setText(currentPost.getmIngredients());
//            viewHolder.focusExpandable(viewHolder, FOCUS_ANY);
//        });

        viewHolder.mRecipe.setOnClickListener(v ->
                viewHolder.likes_overview.setText(currentPost.getmRecipe()));

        viewHolder.mIngredients.setOnClickListener(v ->
                viewHolder.likes_overview.setText(currentPost.getmIngredients()));

        viewHolder.post_TimeStamp.setText(currentPost.getDate_created());
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }


    /**
     * Creating custom ViewHolder class
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        boolean mLikedByCurrentUser;

        RelativeLayout mParentLayout;
        CircleImageView mProfilePic;
        TextView mUserName;
        TextView mDescription;
        ImageView mFoodImg;
        ImageButton mLikes, mComments, mRecipe, mIngredients, mOptions;
        RelativeLayout mPostToolbarBtnsExpansionContainer, mOverviewLayout;
        //        FrameLayout mPostToolbarBtnsExpansionContainer;
        TextView mToolbarExpasionText, likes_overview, post_TimeStamp;


        // ViewHolder item constructor
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mParentLayout = itemView.findViewById(R.id.parentLayoutID);
            mProfilePic = itemView.findViewById(R.id.userProfilePicID);
            mUserName = itemView.findViewById(R.id.userNameID);
            mDescription = itemView.findViewById(R.id.postDescriptionID);
            mOptions = itemView.findViewById(R.id.personal_post_options_menu);
            mFoodImg = itemView.findViewById(R.id.foodImgID);
            mLikes = itemView.findViewById(R.id.likesBtnID);
            mComments = itemView.findViewById(R.id.commentsBtnID);
            mRecipe = itemView.findViewById(R.id.recipeBtnID);
            mIngredients = itemView.findViewById(R.id.ingredientsBtnID);
            // Expandable Toolbar and Contents
            mPostToolbarBtnsExpansionContainer = itemView.findViewById(R.id.toolbarExpansionContainerID);

            likes_overview = itemView.findViewById(R.id.expansionTextID);
            post_TimeStamp = itemView.findViewById(R.id.post_TimeStamp);

            if (mLikedByCurrentUser) {
                mLikes.setImageResource(R.drawable.post_like_pressed);
            } else {
                mLikes.setImageResource(R.drawable.post_like_not_pressed);
            }
        }

        // Used to scroll recycler view over the expandable layout
        private void focusExpandable(ViewHolder viewHolder, int focusItem) {
            LinearLayoutManager lm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            lm.scrollToPositionWithOffset(viewHolder.getAdapterPosition(), focusItem);
        }
    }
}