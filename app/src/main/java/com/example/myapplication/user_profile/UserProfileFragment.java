package com.example.myapplication.user_profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.models.Like;
import com.example.myapplication.models.Post;
import com.example.myapplication.models.User;
import com.example.myapplication.post.AddPostActivity;
import com.example.myapplication.utility_classes.BottomNavigationViewHelper;
import com.example.myapplication.utility_classes.FirebaseMethods;
import com.example.myapplication.utility_classes.GridImageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * File created by tcarau18
 **/
public class UserProfileFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "UserProfileFragment";

    OnGridImageSelectedListener onGridImageSelectedListener;

    private static final int ACTIVITY_NUM = 4;
    private static final int NUM_GRID_COLUMNS = 3;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef, postsRef, userPostCount;
    private FirebaseMethods firebaseMethods;
    private FirebaseUser current_user;
    private String userId;

    private BottomNavigationViewEx bottomNavigationViewEx;

    private TextView mEditProfile, mPosts, mFollowers, mFollowing, mUserName, mDisplayName, mWebsite, mAbout;
    private ProgressBar mProgressBar;
    private CircleImageView mProfilePhoto;
    private ImageView profileMenu;
    private GridView gridView;
    private Toolbar toolbar;

    private User user;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseMethods = FirebaseMethods.getInstance(getContext());
        mAuth = FirebaseMethods.getAuth();
        firebaseMethods.checkUserStateIfNull(getActivity(),mAuth);

        current_user = mAuth.getCurrentUser();
        userId = current_user.getUid();

        mFirebaseDatabase = FirebaseMethods.getmFirebaseDatabase();
        myRef = mFirebaseDatabase.getReference();
        postsRef = mFirebaseDatabase.getReference("users").child(userId).child("posts");
        userPostCount = mFirebaseDatabase.getReference("posts").child(userId);
        initLayout(view);
        setListeners(view);
        setupFirebaseAuth();
        setupGridView();
        setupBottomNavigationView();
        setPostCount();

        return view;
    }

    private void initLayout(View view) {
        mDisplayName = view.findViewById(R.id.display_name);
        mUserName = view.findViewById(R.id.userName);
        mWebsite = view.findViewById(R.id.website);
        mAbout = view.findViewById(R.id.about);
        mPosts = view.findViewById(R.id.tvPosts);
        mFollowers = view.findViewById(R.id.tvFollowers);
        mFollowing = view.findViewById(R.id.tvFollowing);
        gridView = view.findViewById(R.id.grid_view_user_profile);
        mProgressBar = view.findViewById(R.id.profile_progress_bar);
        mProgressBar.setVisibility(View.GONE);
        mProfilePhoto = view.findViewById(R.id.profileImage);
        toolbar = view.findViewById(R.id.profileToolBar);
        bottomNavigationViewEx = view.findViewById(R.id.bottomNavigationBar);
    }

    private void setListeners(View view) {
        mEditProfile = view.findViewById(R.id.editProfile);
        profileMenu = view.findViewById(R.id.profileMenu);
        mEditProfile.setOnClickListener(this);
        profileMenu.setOnClickListener(this);
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


    @Override
    public void onAttach(Context context) {
        try {
            onGridImageSelectedListener = (OnGridImageSelectedListener) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException" + e.getMessage());

        }
        super.onAttach(context);
    }

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user != null) {
                Log.d(TAG, "onAuthStateChanged: signed in with: " + user.getUid());
            } else Log.d(TAG, "onAuthStateChanged: signed out");
            if (user != null) {
                Log.d(TAG, "onAuthStateChanged: signed in" + user.getUid());
            } else
                Log.d(TAG, "onAuthStateChanged: signed out");

        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String photoRef = myRef.child(userId).child("profile_photo").getRef().toString();
                //retrive user information from the database
                if (photoRef.equals("photo")) {
                    mProfilePhoto.setImageResource(R.mipmap.simo_design_avatar);
                } else
                    setProfileWidgets(firebaseMethods.getUserSettings(dataSnapshot));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setProfileWidgets(User userSettings) {
        user = userSettings;
        Log.d(TAG, "setProfileWidgets: setting widgets with data, retrieving from database: " + user.toString());

        mDisplayName.setText(user.getDisplay_name());
        mUserName.setText(user.getDisplay_name());
        mWebsite.setText(user.getWebsite());
        mAbout.setText(user.getAbout());
        mFollowers.setText(String.valueOf(user.getFollowers()));
        mFollowing.setText(String.valueOf(user.getFollowing()));

        String profilePicURL = user.getProfile_photo();
        Log.d(TAG, "setProfileWidgets, PhotoURL: " + profilePicURL);

        //check for image profile url if null, to prevent app crushing when there is no link to profile image in database
        try {
            Glide.with(getActivity()).load(profilePicURL).centerCrop().into(mProfilePhoto);
            Log.d(TAG, "setProfileWidgets: mProfilePhoto.getDrawableState().length; " + mProfilePhoto.getDrawableState().length);

        } catch (Exception e) {
            Log.e(TAG, "setProfileWidgets: Error: " + e.getMessage());
            mProfilePhoto.setImageResource(R.drawable.my_avatar);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.editProfile:
                Intent intent = new Intent(getContext(), AccountSettingsActivity.class);
                intent.putExtra(getString(R.string.calling_activity), getString(R.string.profile_activity));
                startActivity(intent);

                break;
            case R.id.profileMenu:
                Log.d(TAG, "onClick: navigating to account settings");

                ((UserProfileActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
                firebaseMethods.goToWhereverWithFlags(getActivity(), getActivity(), AccountSettingsActivity.class);

                break;
        }

    }


    private void setPostCount() {
        // here i browse throught the user to get post child count.
        userPostCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot postCountDataSnapshot) {
                if (postCountDataSnapshot.exists()) {
                    Log.d(TAG, "simo: dataSnapshotCount: " + postCountDataSnapshot.getChildrenCount());
                    // here i browse throught the user to get post ref count.
                    if (postCountDataSnapshot.exists()) {
                        mPosts.setText(String.valueOf(postCountDataSnapshot.getChildrenCount()));

                    } else
                        mPosts.setText("0");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    /**
     * In this method we setup the Grid View base on an Object Map of the Post entity for our database
     * <p>
     * It creates a list of likes, which is later on used in the View Post Fragment
     * <p>
     * Based on the number of posts it will display the post, but it will be limited to 3 per Row.
     */
    private void setupGridView() {
        Log.d(TAG, "setupGridView: Setting up GridView");

        try {
            final ArrayList<Post> posts = new ArrayList<>();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

            Query query = reference
                    .child(getString(R.string.dbname_posts))
                    .child(mAuth.getCurrentUser().getUid());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        Post post = new Post();
                        Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();
                        post.setmDescription(objectMap.get(getString(R.string.field_description)).toString());
                        post.setmFoodImgUrl(objectMap.get(getString(R.string.field_food_photo)).toString());
                        post.setUserId(objectMap.get(getString(R.string.field_user_id)).toString());
                        post.setmRecipe(objectMap.get(getString(R.string.field_recipe)).toString());
                        post.setmIngredients(objectMap.get(getString(R.string.field_ingredients)).toString());
                        post.setDate_created(objectMap.get(getString(R.string.field_date_created)).toString());
                        post.setPostId(objectMap.get(getString(R.string.field_post_id)).toString());

                        List<Like> likeList = new ArrayList<>();
                        for (DataSnapshot ds : singleSnapshot
                                .child(getString(R.string.field_likes)).getChildren()) {
                            Like like = new Like();
                            like.setUser_id(ds.getValue(Like.class).getUser_id());
                            likeList.add(like);
                        }
                        post.setLikeList(likeList);
                        posts.add(post);
                    }

                    //setup  our grid image
                    int gridWidth = getResources().getDisplayMetrics().widthPixels;
                    int imageWidth = gridWidth / NUM_GRID_COLUMNS;

                    gridView.setColumnWidth(imageWidth);

                    ArrayList<String> imgURLs = new ArrayList<>();

                    for (int i = 0; i < posts.size(); i++) {
                        imgURLs.add(posts.get(i).getmFoodImgUrl());
                    }

                    GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_imageview,
                            "", imgURLs);

                    gridView.setAdapter(adapter);
                    gridView.setOnItemClickListener((parent, view, position, id) -> onGridImageSelectedListener.onGridImageSelected(posts.get(position), ACTIVITY_NUM));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Log.d(TAG, "onCancelled: Query Cancelled");

                }
            });
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error: Nothing to display", Toast.LENGTH_SHORT).show();

            firebaseMethods.goToWhereverWithFlags(getActivity(), getActivity(), AddPostActivity.class);
        }
    }

    /**
     * Bottom Navigation View setup
     */
    public void setupBottomNavigationView() {
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(getContext(), bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }

    public interface OnGridImageSelectedListener {
        void onGridImageSelected(Post post, int activityNr);
    }

}
