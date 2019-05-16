package com.example.myapplication.user_profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.models.Like;
import com.example.myapplication.models.Post;
import com.example.myapplication.models.User;
import com.example.myapplication.utility_classes.BottomNavigationViewHelper;
import com.example.myapplication.utility_classes.Heart;
import com.example.myapplication.utility_classes.SquareImageView;
import com.example.myapplication.utility_classes.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mUserRef;
    private DatabaseReference mPostsRef;
    private String userId;

    //widgets
    private SquareImageView mFoodImg;
    private CircleImageView mProfilePhoto;
    private ImageView optionsMenu, profileMenu,
            heartRed, heartWhite,
            comments, share, backArrow;
    private TextView mUserName, mPostLikes, mPostDescription, mPostCommentLink, mPostTimeStamp;
    private Toolbar toolbar;
    private ImageButton likesPost, recipePost, commentPost, ingredientsPost;
    //vars
    private BottomNavigationViewEx bottomNavigationViewEx;
    private Post mPost;
    private int mActivityNumber = 0;

    //vars for Query
    private User user;
    private boolean mLikedByCurrentUser;
    private StringBuilder mUsers;

    public String mLikesString = "something";

    public ViewPostFragmentNewsFeed() {
        super();
        setArguments(new Bundle());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_post_news_feeed, container, false);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();


        setupFirebaseAuth();
        initLayout(view);
        setListeners(view);

        setupBottomNavigationView();

        return view;
    }

    private void initLayout(View view) {
        mUserName = view.findViewById(R.id.userNameID);
        mProfilePhoto = view.findViewById(R.id.userProfilePicID);
        mFoodImg = view.findViewById(R.id.foodImgID);
        mPostDescription = view.findViewById(R.id.postDescriptionID);


        mPostLikes = view.findViewById(R.id.post_likes);
        mPostTimeStamp = view.findViewById(R.id.post_TimeStamp);


        bottomNavigationViewEx = view.findViewById(R.id.bottomNavigationBar);

        try {
            mPost = getPhotoFromBundle();
            UniversalImageLoader.setImage(mPost.getmFoodImgUrl(), mFoodImg, null, "");
            mActivityNumber = getActivityNumberBundle();

            getPhotoDetails();
            getLikesString();

        } catch (NullPointerException e) {
            Log.e(TAG, "initLayout: NullPointerException " + e.getMessage());
        }
    }

    private void setListeners(View view) {
        optionsMenu = view.findViewById(R.id.options_menu);
        profileMenu = view.findViewById(R.id.show_more);
        backArrow = view.findViewById(R.id.backArrow);

        likesPost = view.findViewById(R.id.likes_button);
        commentPost = view.findViewById(R.id.commentsBtnID);
        recipePost = view.findViewById(R.id.recipeBtnID);
        ingredientsPost = view.findViewById(R.id.ingredientsBtnID);

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
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");

        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = mFirebaseDatabase.getReference();
        mUserRef = FirebaseDatabase.getInstance().getReference("users");
        mPostsRef = FirebaseDatabase.getInstance().getReference("posts");

        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user != null) {
                Log.d(TAG, "onAuthStateChanged: signed in with: " + user.getUid());
            } else Log.d(TAG, "onAuthStateChanged: signed out");
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getPhotoDetails() {
        Query query = mUserRef.child(mAuth.getCurrentUser().getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()
                        && dataSnapshot.getKey()
                        .equals(mPost.getUserId())) {
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
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupWidgets() {
        String timeStampDiff = getTimeStampDifference();
        if (!timeStampDiff.equals(String.valueOf(0))) {
            mPostTimeStamp.setText(timeStampDiff + " Days Ago");
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
            mProfilePhoto.setImageResource(R.drawable.my_avatar);

        }

        mUserName.setText(user.getUsername());

        mPostLikes.setText(mLikesString);
        mPostDescription.setText(mPost.getmDescription());

        if (mLikedByCurrentUser) {
            likesPost.setImageResource(R.drawable.post_like_pressed);
        } else {
            likesPost.setImageResource(R.drawable.post_like_not_pressed);

        }

    }

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
    private Post getPhotoFromBundle() {
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
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(getActivity(), bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(mActivityNumber);
        menuItem.setChecked(true);

    }

    public void getLikesString() {

        Query query = mPostsRef
                .child(userId)
                .child(mPost.getPostId())
                .child(getString(R.string.field_likes))
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
                                Log.d(TAG, "onDataChange: found like" + dataSnapshot.getValue(User.class));

                                Log.d(TAG, "onDataChange: it's a me mario " + currentUser.getUsername());

                                mUsers.append(currentUser.getUsername());
                                mUsers.append(",");
                                setupUserLikedString(currentUser.getUsername());

                            } else if (dataSnapshot.exists()
                                    && !dataSnapshot.getKey().equals(userLikeSnapshotID)) {
                                Query differentUserQuery = mUserRef
                                        .child(userLikeSnapshotID).getRef();
                                differentUserQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshotForDifferentUser) {
                                        final User differentUser = dataSnapshotForDifferentUser.getValue(User.class);
                                        Log.d(TAG, "onDataChange: Pika_surprised " + differentUser.getUsername());

                                        mUsers.append(differentUser.getUsername());
                                        mUsers.append(",");
                                        setupUserLikedString(differentUser.getUsername());
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
//                            setupUserLikedString(user.getUsername());

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
    }

    private void setupUserLikedString(String username) {
        String[] splitUsers = mUsers.toString().split(",");

        mLikedByCurrentUser = mUsers.toString().contains(username
                + ","
        );

        int length = splitUsers.length;
        if (length == 1) {
            mLikesString = "Liked by " + splitUsers[0];
        } else if (length == 2) {
            mLikesString = "Liked by " + splitUsers[0]
                    + " and " + splitUsers[1];

        } else if (length == 3) {
            mLikesString = "Liked by " + splitUsers[0]
                    + " , " + splitUsers[1]
                    + " and " + splitUsers[2];

        } else if (length == 4) {
            mLikesString = "Liked by " + splitUsers[0]
                    + " , " + splitUsers[1]
                    + " , " + splitUsers[2]
                    + " and " + splitUsers[3];
        } else if (length > 4) {
            mLikesString = "Liked by " + splitUsers[0]
                    + " , " + splitUsers[1]
                    + " , " + splitUsers[2]
                    + " and " + (splitUsers.length - 3) + " others";
        }
        setupWidgets();
    }

    public void addNewLike() {
        Log.d(TAG, "addNewLike: add new like");

        String newLikeId = mPostsRef.child(mPost.getPostId()).push().getKey();
        Like like = new Like();
        like.setUser_id(userId);

        mPostsRef
                .child(userId)
                .child(mPost.getPostId())
                .child(getString(R.string.field_likes))
                .child(newLikeId)
                .setValue(like);

        getLikesString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.show_more:

                ((UserProfileActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
                Log.d(TAG, "onClick: navigating to account settings");

                startActivity(new Intent(getActivity(), AccountSettingsActivity.class));

                break;

            case R.id.commentsBtnID:
                Toast.makeText(getActivity(), "comment", Toast.LENGTH_SHORT).show();

                break;

            case R.id.ingredientsBtnID:
                Toast.makeText(getActivity(), "ingredients", Toast.LENGTH_SHORT).show();

                break;

            case R.id.recipeBtnID:
                Toast.makeText(getActivity(), "recipe", Toast.LENGTH_SHORT).show();

                break;

            case R.id.options_menu:
                Toast.makeText(getActivity(), "Make Delete/Report Dialogue", Toast.LENGTH_SHORT).show();

                break;


            case R.id.backArrow:
                Log.d(TAG, "onClick: navigating back to " + getActivity());
                startActivity(new Intent(getActivity(), UserProfileActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                getActivity().finish();

                break;

            case R.id.likes_button:
                toggleLikes();

                break;
        }

    }
    public void toggleLikes() {
        Query query = mPostsRef
                .child(userId)
                .child(mPost.getPostId())
                .child(getString(R.string.field_likes))
                .orderByChild(Objects.requireNonNull(mPostsRef
                        .child(mPost.getPostId())
                        .child(getString(R.string.field_likes))
                        .child(userId).getKey()));

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                    if (mLikedByCurrentUser &&
                            Objects.requireNonNull(singleSnapshot.getValue(Like.class)).getUser_id()
                                    .equals(userId)) {
                        mPostsRef
                                .child(userId)
                                .child(mPost.getPostId())
                                .child(getString(R.string.field_likes))
                                .child(Objects.requireNonNull(singleSnapshot.getKey()))
                                .removeValue();

                        likesPost.setImageResource(R.drawable.post_like_not_pressed);
                        getLikesString();
                    }
//                    else if (!mLikedByCurrentUser &&
//                            !Objects.requireNonNull(singleSnapshot.getValue(Like.class)).getUser_id()
//                                    .equals(userId)) {
//                        likesPost.setImageResource(R.drawable.post_like_pressed);
//                        addNewLike();
////                        getLikesString();
//                    } else if (mLikedByCurrentUser &&
//                            (!singleSnapshot.getValue(Like.class).getUser_id()
//                                    .equals(userId))) {
//                        likesPost.setImageResource(R.drawable.post_like_pressed);
//                        addNewLike();
//                        getLikesString();
//                        break;
//
//                    }
                    else if (!mLikedByCurrentUser) {
                        addNewLike();
                        likesPost.setImageResource(R.drawable.post_like_pressed);
                        break;
                    }

                }
                if (!dataSnapshot.exists()) {
                    addNewLike();
                    likesPost.setImageResource(R.drawable.post_like_pressed);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d(TAG, "onCancelled: Query Cancelled");

            }
        });
    }

}
