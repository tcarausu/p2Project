package com.example.myapplication.utility_classes;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.comment_activity.CommentsActivity;
import com.example.myapplication.models.Comment;
import com.example.myapplication.models.Like;
import com.example.myapplication.models.Post;
import com.example.myapplication.models.User;
import com.example.myapplication.user_profile.ViewPostFragmentNewsFeed;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class RecyclerViewAdapterPostItems extends RecyclerView.Adapter<RecyclerViewAdapterPostItems.ViewHolder> {
    private static final String TAG = "AdapterPostItems";

    // Member variables
    private Context mContext;

    // Get the recycler view that contains this adapter so that we are able to scroll automatically

    //firebase
    private FirebaseAuth mAuth = FirebaseMethods.getAuth();
    private FirebaseDatabase mFirebaseDatabase = FirebaseMethods.getmFirebaseDatabase();
    private DatabaseReference mPostsRef, mUserRef, currentPostRef, currentPostLikeRef;

    private ViewPostFragmentNewsFeed viewPost = new ViewPostFragmentNewsFeed();
    private RecyclerView mRecyclerView;
    private List<Post> mPosts;
    private List<Like> likeList = new ArrayList<>();
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private boolean isLiked = false;


    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        listener = mOnItemClickListener;
    }


    public RecyclerViewAdapterPostItems(Context context, List<Post> posts) {
        this.mContext = context;
        this.mPosts = posts;
    }

    @Exclude
    public void setPostsList(List<Post> mPosts) {
        this.mPosts = mPosts;
    }

    @Exclude
    public void setUserForPost(Post post, User user) {
        post.setUser(user);
    }

    private User getUserForPost(Post post) {
        return post.getUser();
    }

    @NonNull
    @Override
    public synchronized ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_list_item_post, viewGroup, false);


        return new ViewHolder(view);
    }

//    private void likeButtonSetter(String currentPostUserId, String postID, ImageButton likeButton, TextView likeText) {
//        currentPostLikeRef = mPostsRef.child(currentPostUserId).child(postID).child("Likes").getRef();
//        currentPostLikeRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                likeList.clear();
//                for (DataSnapshot ds : dataSnapshot.getChildren())
//                    likeList.add(ds.getValue(Like.class));
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        for (Like lk : likeList) {
//            if (lk.getUser_id().contains(mAuth.getCurrentUser().getUid()))
//                likeButton.setImageResource(R.drawable.post_like_pressed);
//            else
//                likeButton.setImageResource(R.drawable.post_like_not_pressed);
//        }
//        int nrOfLikes = likeList.size();
//        if (nrOfLikes > 1) {
//            likeText.setText(String.format("%d Likes",nrOfLikes));
//        } else if (nrOfLikes == 0) {
//            likeText.setText(R.string.like);
//        } else if (nrOfLikes == 1)
//            likeText.setText(String.format("%d Like",nrOfLikes));
//
//    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int index) {
        viewHolder.mOptions.setVisibility(View.INVISIBLE);
        Log.d(TAG, "onBindViewHolder: Called");

        final Post currentPost = mPosts.get(viewHolder.getAdapterPosition());
        User postUser = getUserForPost(currentPost);
        mUserRef = mFirebaseDatabase.getReference("users");
        mPostsRef = mFirebaseDatabase.getReference("posts");
        currentPostRef = mPostsRef.child(currentPost.getUserId()).child(currentPost.getPostId()).getRef();
        currentPostLikeRef = mPostsRef.child(currentPost.getUserId()).child(currentPost.getPostId()).child("Likes").getRef();
        viewHolder.setIsRecyclable(true);

        List<Comment> commentList = currentPost.getCommentList();
        int noOfComments = commentList.size();
        likeList = currentPost.getLikeList();

        for (Like like : likeList)
        if (like.getUser_id()== mAuth.getCurrentUser().getUid()){
            viewHolder.mLikes.setImageResource(R.drawable.post_like_pressed);

            return;
        }
        else {
            viewHolder.mLikes.setImageResource(R.drawable.post_like_not_pressed);
        }



        if (noOfComments > 1) {
            viewHolder.commentText.setText(String.format("%d Comments", noOfComments));
        } else if (noOfComments == 0) {
            viewHolder.commentText.setText(R.string.comment);
        } else if (noOfComments == 1)
            viewHolder.commentText.setText(String.format("%d Comment", noOfComments));


        if (postUser != null) {
            Glide.with(mContext)
                    .load(postUser.getProfile_photo())
                    .centerInside()
                    .into(viewHolder.mProfilePic);
            viewHolder.mUserName.setText(postUser.getDisplay_name());

        } else {
            Glide.with(mContext)
                    .load(R.drawable.my_avatar)
                    .centerInside()
                    .into(viewHolder.mProfilePic);
            viewHolder.mUserName.setText(R.string.loading);

        }

        viewHolder.mDescription.setText(currentPost.getmDescription());

        Glide.with(mContext)
                .load(currentPost.getmFoodImgUrl())
                .fitCenter()
                .centerCrop()
                .into(viewHolder.mFoodImg);

        // *************************************************** Button Listeners ***************************************************

        viewHolder.mLikes.setOnClickListener(v -> {
            String currentUserID = mAuth.getCurrentUser().getUid();
            String postID = currentPost.getPostId();
            String newLikeId = mPostsRef.child(postID).push().getKey();
            Like newLike = new Like();
            newLike.setUser_id(currentUserID);
            //TODO  like for any post by any user
            currentPostLikeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Log.d(TAG, "likeTest: dataSnapshot.gkey " + dataSnapshot.getKey());
                        for (DataSnapshot dss : dataSnapshot.getChildren()) {
                            if (dss.getValue(Like.class).getUser_id().equals(currentUserID)) {
                                isLiked = true;
                                dss.getRef().removeValue();
                                likeList.remove(newLike);
                                viewHolder.mLikes.setImageResource(R.drawable.post_like_not_pressed);
                                viewHolder.itemView.refreshDrawableState();
                                isLiked = false;
                                notifyDataSetChanged();

                            } else {
                                dss.getRef().setValue(newLike);
                                isLiked = true;
//                                mPostsRef.child(currentPost.getUserId()).child(postID).child("Likes").child(newLikeId).setValue(newLike);
                                likeList.add(newLike);
                                viewHolder.mLikes.setImageResource(R.drawable.post_like_pressed);
                                viewHolder.itemView.refreshDrawableState();
                                notifyDataSetChanged();

                            }
                        }
                    } else
                        mPostsRef.child(currentPost.getUserId()).child(postID).child("Likes").child(newLikeId).setValue(newLike);
                    isLiked = true;
                    viewHolder.itemView.refreshDrawableState();
                    notifyDataSetChanged();
                    viewHolder.mLikes.setImageResource(R.drawable.post_like_pressed);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });

        viewHolder.mComments.setOnClickListener(v -> {
            // implementation for displaying the comments for each post
            Intent intent = new Intent(mContext, CommentsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("currentPost", currentPost);
            intent.putExtras(bundle);
            mContext.startActivity(intent);

        });

        viewHolder.mIngredients.setOnClickListener(v -> {
            if (currentPost.getmIngredients().equals("")) {
                viewHolder.likes_overview.setText(R.string.no_ing_av);
            } else
                viewHolder.likes_overview.setText(currentPost.getmIngredients());
        });

        viewHolder.mRecipe.setOnClickListener(v -> {
            if (currentPost.getmRecipe().equals("")) {
                viewHolder.likes_overview.setText(R.string.no_rec_av);
            } else
                viewHolder.likes_overview.setText(currentPost.getmRecipe());
        });

        FirebaseMethods.setTimeStampTodays(viewHolder.post_TimeStamp, currentPost);

    }

    private void likeToggler(Post currentPost, ImageButton likeButton) {
        String currentUserID = mAuth.getCurrentUser().getUid();
        String postID = currentPost.getPostId();
        String newLikeId = mPostsRef.child(postID).push().getKey();
        Like newLike = new Like();
        newLike.setUser_id(currentUserID);
        //TODO  like for any post by any user
        currentPostLikeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d(TAG, "likeTest: dataSnapshot.gkey " + dataSnapshot.getKey());
                    for (DataSnapshot dss : dataSnapshot.getChildren()) {
                        if (dss.getValue(Like.class).getUser_id().equals(currentUserID)) {
                            dss.getRef().removeValue();
                            likeButton.setImageResource(R.drawable.post_like_not_pressed);


                        } else {
                            mPostsRef.child(currentPost.getUserId()).child(postID).child("Likes").child(newLikeId).setValue(newLike);
                            likeList.add(newLike);
                            likeButton.setImageResource(R.drawable.post_like_pressed);
                            mRecyclerView.getAdapter().notifyDataSetChanged();

                        }
                    }
                } else
                    mPostsRef.child(currentPost.getUserId()).child(postID).child("Likes").child(newLikeId).setValue(newLike);
                likeButton.setImageResource(R.drawable.post_like_pressed);
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }


    /**
     * Creating custom ViewHolder class
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout mParentLayout;
        CircleImageView mProfilePic;
        TextView mUserName;
        TextView mDescription;
        ImageView mFoodImg;
        ImageButton mLikes, mComments, mRecipe, mIngredients;
        ImageButton mOptions;
        RelativeLayout mPostToolbarBtnsExpansionContainer;
        TextView likes_overview, post_TimeStamp, commentText, likeText;

        // ViewHolder item constructor
        private ViewHolder(@NonNull View itemView) {
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

            likeText = itemView.findViewById(R.id.likes_not_pressed_text);
            commentText = itemView.findViewById(R.id.comments_text);
            mPostToolbarBtnsExpansionContainer = itemView.findViewById(R.id.toolbarExpansionContainerID);
            likes_overview = itemView.findViewById(R.id.expansionTextID);
            post_TimeStamp = itemView.findViewById(R.id.post_TimeStamp);


        }
    }
}