package com.example.myapplication.user_profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.comment_activity.CommentsActivity;
import com.example.myapplication.models.Like;
import com.example.myapplication.models.Post;
import com.example.myapplication.models.User;
import com.example.myapplication.utility_classes.BottomNavigationViewHelper;
import com.example.myapplication.utility_classes.FirebaseMethods;
import com.example.myapplication.utility_classes.OurAlertDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * File created by tcarau18
 **/
public class ViewPostFragmentNewsFeed extends Fragment implements View.OnClickListener {

    private static final String TAG = "ViewPostFragmentNews";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserRef, mPostsRef, currentPostRef;
    private FirebaseMethods mFirebaseMethods;

    //fire-base storage
    private FirebaseStorage mStorageRef;
    private String userId;

    //widgets
    private ImageView mFoodImg;
    private CircleImageView mProfilePhoto;
    private ImageView profileMenu, backArrow;
    private TextView mUserName, mPostLikes, mPostDescription, mPostTimeStamp;
    private ImageButton optionsMenu;
    private ImageButton likesPost, recipePost, commentPost, ingredientsPost;
    private Toolbar toolbar;

    //vars
    private BottomNavigationViewEx bottomNavigationViewEx;
    private Post mPost;
    private int mActivityNumber = 0;

    //vars for Query
    private User user;
    private boolean mLikedByCurrentUser, isLiked;
    private StringBuilder mUsers;
    private List<Like> likeList;

    public String mLikesString = "Likes string";

    public ViewPostFragmentNewsFeed() {
        setArguments(new Bundle());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_post_news_feeed, container, false);
        findWidgets(view);
        connectToDatabase();
        initLayout(view);
        buttonListeners();
        setupBottomNavigationView();

        return view;
    }

    private void connectToDatabase() {
        mFirebaseMethods = FirebaseMethods.getInstance(getActivity());
        mAuth = FirebaseMethods.getAuth();
        current_user = mAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseMethods.getmFirebaseDatabase();
        mUserRef = mFirebaseDatabase.getReference("users");
        mPostsRef = mFirebaseDatabase.getReference("posts");
        mStorageRef = FirebaseMethods.getFirebaseStorage();
    }

    private void initLayout(View view) {
        try {
            userId = current_user.getUid();
            mUserName = view.findViewById(R.id.userNameID);
            mProfilePhoto = view.findViewById(R.id.userProfilePicID);
            mFoodImg = view.findViewById(R.id.foodImgID);
            mPostDescription = view.findViewById(R.id.postDescriptionID);
            mPostLikes = view.findViewById(R.id.expansionTextID);
            mPostTimeStamp = view.findViewById(R.id.post_TimeStamp);
            bottomNavigationViewEx = view.findViewById(R.id.bottomNavigationBar);

            try {
                mPost = getPhotoFromBundle();
                Glide.with(getActivity()).load(mPost.getmFoodImgUrl()).centerCrop().into(mFoodImg);
                mActivityNumber = getActivityNumberBundle();
                getPhotoDetails(mUserRef, mPost);
                getLikesString(mPostsRef, mUserRef, mPost.getUserId(), mPost.getPostId());

            } catch (NullPointerException e) {
                Log.e(TAG, "initLayout: NullPointerException " + e.getMessage());
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "toggleLikes: NullPointerException", e.getCause());
        }
    }

    private void initializeLikeList(@NonNull Post post) {
        likeList = post.getLikeList();
        for (Like lk : likeList) {
            if (lk.getUser_id().equals(current_user.getUid()))

//                likesPost.setImageResource(R.drawable.post_like_pressed);
                likesPost.setPressed(true);
            else
                likesPost.setPressed(false);
//                likesPost.setImageResource(R.drawable.post_like_not_pressed);
        }

    }

    private void findWidgets(View view) {
        optionsMenu = view.findViewById(R.id.personal_post_options_menu);
        profileMenu = view.findViewById(R.id.account_settings_options);
        backArrow = view.findViewById(R.id.backArrow);
        likesPost = view.findViewById(R.id.likesBtnID);
        commentPost = view.findViewById(R.id.commentsBtnID);
        recipePost = view.findViewById(R.id.recipeBtnID);
        ingredientsPost = view.findViewById(R.id.ingredientsBtnID);
    }

    private void buttonListeners() {
        optionsMenu.setOnClickListener(this);
        profileMenu.setOnClickListener(this);
        backArrow.setOnClickListener(this);
        likesPost.setOnClickListener(this);
        commentPost.setOnClickListener(this);
        recipePost.setOnClickListener(this);
        ingredientsPost.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();

    }


    private void setupWidgets() {
        try {
            String timeStampDiff = getTimeStampDifference();
            if (!timeStampDiff.equals(String.valueOf(0))) {
                mPostTimeStamp.setText(String.format("%s Days Ago", timeStampDiff));
            } else {
                mPostTimeStamp.setText("Today");
            }
            String profilePicURL = user.getProfile_photo();

            //check for image profile url if null, to prevent app crushing when there is no link to profile image in database
            try {
                if (profilePicURL == null) {
                    mProfilePhoto.setImageResource(R.drawable.my_avatar);

                } else
                    Glide.with(this).load(profilePicURL).centerCrop().into(mProfilePhoto);

            } catch (Exception e) {
                Log.e(TAG, "setProfileWidgets: Error: " + e.getMessage());

            }

            mUserName.setText(user.getUsername());
            mPostLikes.setText(mLikesString);
            mPostDescription.setText(mPost.getmDescription());

            if (mLikedByCurrentUser) {
                likesPost.setImageResource(R.drawable.post_like_pressed);
            } else {
                likesPost.setImageResource(R.drawable.post_like_not_pressed);
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "toggleLikes: NullPointerException", e.getCause());
        }
    }

    /**
     * This method displays retrieves the Likes from the user's post
     * and displaying the likes in the setupUserLikedString method
     */
    public void getLikesString(DatabaseReference mPostsRef, DatabaseReference mUserRef, String userId, String postId) {
        try {
            Query query = mPostsRef
                    .child(userId)
                    .child(postId)
                    .child("Likes")
                    .getRef();

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mUsers = new StringBuilder();

                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        String userLikeSnapshotID = singleSnapshot.getValue(Like.class).getUser_id();
                        Query query = mUserRef
                                .child(userId).getRef();

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final User currentUser = dataSnapshot.getValue(User.class);
                                if (dataSnapshot.exists()
                                        && dataSnapshot.getKey().equals(userLikeSnapshotID)) {
                                    Log.d(TAG, "onDataChange: the current User " + currentUser.getUsername());

                                    mUsers.append(currentUser.getUsername());
                                    mUsers.append(",");

                                    user = currentUser;

                                    setupUserLikedString(currentUser.getUsername(), user);

                                } else if (dataSnapshot.exists()
                                        && !dataSnapshot.getKey().equals(userLikeSnapshotID)) {
                                    Query differentUserQuery = mUserRef
                                            .child(userLikeSnapshotID).getRef();
                                    differentUserQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshotForDifferentUser) {
                                            final User differentUser = dataSnapshotForDifferentUser.getValue(User.class);
                                            Log.d(TAG, "onDataChange: One of the linking Users " + differentUser.getUsername());

                                            mUsers.append(differentUser.getUsername());
                                            mUsers.append(",");

                                            user = differentUser;

                                            setupUserLikedString(differentUser.getUsername(), user);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.d(TAG, "onCancelled: Query Cancelled");

                            }
                        });
                    }
                    if (!dataSnapshot.exists()) {
                        mLikesString = "";
                        mLikedByCurrentUser = false;
                        setupWidgets();
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled: Query Cancelled");

                }
            });
        } catch (NullPointerException e) {
            Log.e(TAG, "toggleLikes: NullPointerException", e.getCause());
        }
    }

    /**
     * The methods takes a
     *
     * @param username which is the Username of an existing username in the databse,
     *                 being already checked by the getLikesString method
     *                 <p>
     *                 Here we create a array of String which will be taking all the users,
     *                 Then based on the length it will display "Liked by" and number of them
     */
    private void setupUserLikedString(String username, User user) {

        String[] splitUsers = mUsers.toString().split(",");
        boolean checkCurrentUserState = mUsers.toString().contains(user.getUsername());
        boolean checkDesiredUserState = mUsers.toString().contains(username);

        if (username.equals(user.getUsername())) {
            mLikedByCurrentUser = mUsers.toString().contains(user.getUsername()
                    + ","
            );
        } else if (checkCurrentUserState && checkDesiredUserState) {
            mLikedByCurrentUser = true;
        } else if (!checkCurrentUserState && checkDesiredUserState) {
            mLikedByCurrentUser = false;
        }

        int length = splitUsers.length;
        if (length == 1) {
            mLikesString = "Liked by " + splitUsers[0];
            setmLikesString(mLikesString);
        } else if (length == 2) {
            mLikesString = "Liked by " + splitUsers[0]
                    + " and " + splitUsers[1];
            setmLikesString(mLikesString);

        } else if (length == 3) {
            mLikesString = "Liked by " + splitUsers[0]
                    + " , " + splitUsers[1]
                    + " and " + splitUsers[2];
            setmLikesString(mLikesString);

        } else if (length == 4) {
            mLikesString = "Liked by " + splitUsers[0]
                    + " , " + splitUsers[1]
                    + " , " + splitUsers[2]
                    + " and " + splitUsers[3];
            setmLikesString(mLikesString);

        } else if (length > 4) {
            mLikesString = "Liked by " + splitUsers[0]
                    + " , " + splitUsers[1]
                    + " , " + splitUsers[2]
                    + " and " + (splitUsers.length - 3) + " others";
            setmLikesString(mLikesString);

        }
        setupWidgets();
    }

    public String getmLikesString() {
        return mLikesString;
    }

    public void setmLikesString(String mLikesString) {
        this.mLikesString = mLikesString;
    }


    /**
     * Here we have the dialogue to delete, report a post
     */
    private void dialogChoice() {

        //TODO: replace the current dialog with customised one

        View layoutView = getLayoutInflater().inflate(R.layout.dialog_deletepost_layout, null);
        Button deletePostButton = layoutView.findViewById(R.id.deletePostButton);
        Button reportButton = layoutView.findViewById(R.id.reportPostButton);
        ImageButton cancelButton = layoutView.findViewById(R.id.cencelDeletePost);

        OurAlertDialog.Builder myDialogBuilder = new OurAlertDialog.Builder(getActivity());
        myDialogBuilder.setView(layoutView);
        myDialogBuilder.setIcon(R.mipmap.chefood_icones);
        final AlertDialog alertDialog = myDialogBuilder.create();
        WindowManager.LayoutParams wlp = alertDialog.getWindow().getAttributes();
        wlp.windowAnimations = R.style.AlertDialogAnimation;
        wlp.gravity = Gravity.CENTER;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;

        alertDialog.getWindow().setAttributes(wlp);
        alertDialog.setCanceledOnTouchOutside(true);
        // Setting transparent the background (layout) of alert dialog
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        deletePostButton.setOnClickListener(v -> {
            deletePost();
            alertDialog.dismiss();
        });
        reportButton.setOnClickListener(v -> {
            reportPost();
            alertDialog.dismiss();
        });
        cancelButton.setOnClickListener(v -> {
            alertDialog.dismiss();
        });


    }

    private void reportPost() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.account_settings_options:
                ((UserProfileActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
                mFirebaseMethods.goToWhereverWithFlags(getActivity(), AccountSettingsActivity.class);
                getActivity().overridePendingTransition(R.anim.left_enter, R.anim.right_out);
                getActivity().finish();
                break;

            case R.id.personal_post_options_menu:
                dialogChoice();
                break;

            case R.id.commentsBtnID:
                goToCommentActivity();
                getActivity().overridePendingTransition(R.anim.left_enter, R.anim.right_out);
                getActivity().finish();
                break;

            case R.id.ingredientsBtnID:
                String ingredients = mPost.getmIngredients();
                mPostLikes.setText(ingredients);
                break;

            case R.id.recipeBtnID:
                String recipe = mPost.getmRecipe();
                mPostLikes.setText(recipe);
                break;

            case R.id.backArrow:
                Log.d(TAG, "onClick: navigating back to " + getActivity());
                mFirebaseMethods.goToWhereverWithFlags(getActivity(), UserProfileActivity.class);
                getActivity().overridePendingTransition(R.anim.right_out, R.anim.left_enter);
                getActivity().finish();

                break;

            case R.id.likesBtnID:
                toggleLikes();
                break;
        }

    }

    private void goToCommentActivity() {
        Intent intent = new Intent(getActivity(), CommentsActivity.class);

        Post bundle = getPhotoFromBundle();
        Bundle myBundle = new Bundle();
        myBundle.putParcelable("currentPost", mPost);
        myBundle.putParcelable("currentPost", (Parcelable) mPost.getCommentList());
//        Parcelable parcelable = bundle;
        intent.putExtras(myBundle);
        startActivity(intent);
       getActivity().overridePendingTransition(R.anim.right_out, R.anim.left_out);
    }

    /**
     * This method add a new like directly the requests, current post.
     */
    private void addNewLikeForOwnPost(DatabaseReference mPostsRef) {
        try {
            Log.d(TAG, "addNewLikeForOwnPost: add new like");

            String postId = mPost.getPostId();
            String currentUserId = current_user.getUid();
            String newLikeId = mPostsRef.child(postId).push().getKey();
            Like like = new Like();
            like.setUser_id(currentUserId);

            mPostsRef
                    .child(currentUserId)
                    .child(postId)
                    .child("Likes")
                    .child(newLikeId)
                    .setValue(like);

            likeList.add(like);


        } catch (NullPointerException e) {
            Log.e(TAG, "toggleLikes: NullPointerException", e.getCause());
        }
    }

    private void toggleLikes() {

        try {
            String currentUserId = current_user.getUid();
            String postId = mPost.getPostId();
            String newLikeId = mPostsRef.child(postId).push().getKey();
            Like newLike = new Like();
            newLike.setUser_id(currentUserId);

            currentPostRef = mPostsRef.child(currentUserId).child(postId).child("Likes").getRef();
            currentPostRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Log.d(TAG, "likeTest: dataSnapshot.gkey " + dataSnapshot.getKey());
                        for (DataSnapshot dss : dataSnapshot.getChildren())
                            if (!dss.getValue(Like.class).getUser_id().equals(currentUserId)) {
                                mPostsRef
                                        .child(currentUserId)
                                        .child(postId)
                                        .child("Likes")
                                        .child(newLikeId)
                                        .setValue(newLike);
                                getLikesString(mPostsRef, mUserRef, currentUserId, postId);
                                initializeLikeList(mPost);


                            } else {
                                dss.getRef().removeValue();
                                initializeLikeList(mPost);
                            }
                    } else
                        mPostsRef
                                .child(currentUserId)
                                .child(postId)
                                .child("Likes")
                                .child(newLikeId)
                                .setValue(newLike);
                    getLikesString(mPostsRef, mUserRef, currentUserId, postId);
                    initializeLikeList(mPost);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } catch (NullPointerException nuller) {
            Log.d(TAG, "toggleLikes: error: " + nuller.getMessage());
        }
//        try {
//            Query query = mPostsRef
//                    .child(currentUserId)
//                    .child(postId)
//                    .child("Likes")
//                    .orderByChild(Objects.requireNonNull(mPostsRef
//                            .child(postId)
//                            .child("Likes")
//                            .getRef().getKey()));
//
//
//            query.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        if (!dataSnapshot.getKey().contains(currentUserId)) {
//                            String newLikeId = mPostsRef.child(postId).push().getKey();
//                            Like like = new Like();
//                            like.setUser_id(currentUserId);
//                            query.getRef().setValue(like);
//                            addNewLikeForOwnPost(mPostsRef);
//                            likesPost.setImageResource(R.drawable.post_like_pressed);
//                        } else dataSnapshot.getRef().removeValue();
//                        likesPost.setImageResource(R.drawable.post_like_not_pressed);
//                    } else query.getRef().setValue("Likes");
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        } catch (
//                NullPointerException e) {
//            Log.e(TAG, "toggleLikes: NullPointerException", e.getCause());
//        }
    }

    /**
     * The method gets the proper timestamp of the post creation and make the difference between
     * current time and the time created
     *
     * @return the difference
     */
    private String getTimeStampDifference() {
        Log.d(TAG, "getTimeStampDifference: getting TimeStamp Difference");

        String diff;

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Copenhagen"));
        Date today = c.getTime();
        sdf.format(today);
        Date timeStamp;

        final String photoTimeStamp = mPost.getDate_created();
        try {
            timeStamp = sdf.parse(photoTimeStamp);
            diff = String.valueOf(Math.round((today.getTime() - timeStamp.getTime()) / 1000 / 60 / 60 / 24));
        } catch (ParseException e) {
            Log.e(TAG, "getTimeStampDifference: ParseException " + e.getMessage());
            diff = "0";
        }

        return diff;
    }

    /**
     * retrieves the Post from the incoming bundle from ProfileActivity interface
     *
     * @return photo
     */
    public Post getPhotoFromBundle() {
        Log.d(TAG, "getPhotoFromBundle: arguments: " + getArguments());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            return bundle.getParcelable(getString(R.string.post));
        } else {
            return null;
        }

    }

    /**
     * retrieves the activity number from the incoming bundle from ProfileActivity interface
     *
     * @return the activity number
     */
    private int getActivityNumberBundle() {
        Log.d(TAG, "getPhotoFromBundle: arguments: " + getArguments());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            return bundle.getInt(getString(R.string.activity_number));
        } else {
            return 0;
        }

    }

    /**
     * Bottom Navigation View setup
     */
    public void setupBottomNavigationView() {
        BottomNavigationViewHelper bnvh = new BottomNavigationViewHelper(getActivity());
        bnvh.setupBottomNavigationView(bottomNavigationViewEx);
        bnvh.enableNavigation(getActivity(), bottomNavigationViewEx);
        bnvh.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(mActivityNumber);
        menuItem.setChecked(true);

    }


    /**
     * The method gets the Photo details for a specific user
     * If there is an user with this id, in our case the current user exists and the User's id is equal to
     * the requested ost's id then it should return the data for the user.
     */
    public void getPhotoDetails(DatabaseReference mUserRef, Post mPost) {
        try {
            userId = current_user.getUid();
            Query query = mUserRef.child(userId);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.getKey().equals(mPost.getUserId())) {
                        user = dataSnapshot.getValue(User.class);

                    } else {

                        Log.d(TAG, "onDataChange: There is no data: " + dataSnapshot.getValue(User.class).getProfile_photo());
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled: Query Cancelled");

                }
            });
        } catch (NullPointerException e) {
            Log.e(TAG, "toggleLikes: NullPointerException", e.getCause());
        }
    }

    /**
     * @author: Mo.Msaad
     * @use: deletes the current displayed post with all its data and deletes the item from storage files.
     **/
    private void deletePost() {
        try {

            DatabaseReference currentPostRef = mPostsRef.child(mAuth.getCurrentUser().getUid()).child(this.mPost.getPostId());
            DatabaseReference currentUserRef = mUserRef.child(mAuth.getCurrentUser().getUid());
            ProgressDialog progressDialog = new ProgressDialog(this.getContext());
            progressDialog.setTitle("Deleting");
            progressDialog.setMessage("Deleting post, please wait for task to finish");
            progressDialog.setIcon(R.drawable.chefood);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            StorageReference imageRef = mStorageRef.getReferenceFromUrl(this.mPost.getmFoodImgUrl());
            Log.d(TAG, "deletePost: currentPostRef: " + imageRef);

            imageRef.delete().addOnSuccessListener(aVoid -> {
                currentPostRef.removeValue();
                currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            final User user = dataSnapshot.getValue(User.class);
                            if (user.getNrOfPosts() != 0) {
                                user.setNrPosts(user.getNrOfPosts() - 1);
                                currentUserRef.setValue(user);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                progressDialog.dismiss();
                mFirebaseMethods.goToWhereverWithFlags(getActivity(), UserProfileActivity.class);
                Toast.makeText(getContext(), "Item deleted.", Toast.LENGTH_SHORT).show();

            });

        } catch (Exception e) {
            Log.d(TAG, "deletePost: error: " + e.getMessage());
        }
    }

}
