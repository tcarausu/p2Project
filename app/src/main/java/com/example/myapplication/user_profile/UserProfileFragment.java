package com.example.myapplication.user_profile;

import android.content.Context;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.models.User;
import com.example.myapplication.models.UserAccountSettings;
import com.example.myapplication.models.UserSettings;
import com.example.myapplication.utility_classes.BottomNavigationViewHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * File created by tcarau18
 **/
public class UserProfileFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "UserProfileFragment";

    private static final int ACTIVITY_NUM = 4;
    private static final int NUM_GRID_COLUMNS = 3;

    private Context mContext;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private TextView mEditProfile, mPosts, mFollowers, mFollowing, mUserName, mDisplayName, mWebsite, mDescription;
    private BottomNavigationViewEx bottomNavigationViewEx;
    private ProgressBar mProgressBar;
    private CircleImageView mProfilePhoto;
    private ImageView profileMenu;
    private GridView gridView;
    private Toolbar toolbar;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initLayout(view);
        setListeners(view);
        setupFirebaseAuth();
        setupBottomNavigationView();

        return view;
    }

    private void initLayout(View view) {
        mContext = getActivity();

        mDisplayName = view.findViewById(R.id.displayName);
        mUserName = view.findViewById(R.id.userName);
        mWebsite = view.findViewById(R.id.website);
        mDescription = view.findViewById(R.id.description);
        mPosts = view.findViewById(R.id.textPosts);
        mFollowers = view.findViewById(R.id.textFollowers);
        mFollowing = view.findViewById(R.id.textFollowing);

        gridView = view.findViewById(R.id.gridImageView);

        mProgressBar = view.findViewById(R.id.profile_progress_bar);
        mProgressBar.setVisibility(View.GONE);
        mProfilePhoto = view.findViewById(R.id.profileImage);

        toolbar = view.findViewById(R.id.profileToolBar);
        bottomNavigationViewEx = view.findViewById(R.id.bottomNavigationBar);

        mAuth = FirebaseAuth.getInstance();

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

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: signed in" + user.getUid());
                } else Log.d(TAG, "onAuthStateChanged: signed out");
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//retrive user information from the database

//retrive images for the user in question

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    /**
     * Retrieves the account settings for the User currently logged in
     * Database:user_account_settings node
     *
     * @param dataSnapshot represent the data from database
     * @return the User Account Settings
     */
    private UserSettings getUserSettings(DataSnapshot dataSnapshot) {
        Log.d(TAG, "getUserAccountSettings: retrieving user account settings from database");

        UserAccountSettings settings = new UserAccountSettings();
        User user = new User();
        String userID = mAuth.getCurrentUser().getUid();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {

            //User Account Settings Node
            if (ds.getKey().equals(mContext.getString(R.string.dbname_user_account_settings))) {
                Log.d(TAG, "getUserAccountSettings: dataSnapshot" + ds);
                try {
                    settings.setUsername(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getUsername()
                    );

                    settings.setDisplay_name(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getDisplay_name()
                    );

                    settings.setDescription(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getDescription()
                    );

                    settings.setWebsite(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getWebsite()
                    );

                    settings.setFollowers(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowers()
                    );

                    settings.setFollowing(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowing()
                    );

                    settings.setPosts(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getPosts()
                    );

                    settings.setProfile_photo(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getProfile_photo()
                    );

                    Log.d(TAG, "getUserAccountSettings: retrieve user account settings information: " + settings.toString());
                } catch (NullPointerException e) {
                    Log.d(TAG, "getUserAccountSettings: NullPointerException: " + e.getMessage());
                }
            }

            //Users Node
            if (ds.getKey().equals(mContext.getString(R.string.dbname_users))) {
                Log.d(TAG, "getUserAccountSettings: dataSnapshot" + ds);

                user.setUser_id(
                        ds.child(userID)
                                .getValue(User.class)
                                .getUser_id()
                );

                user.setUsername(
                        ds.child(userID)
                                .getValue(User.class)
                                .getUsername()
                );

                user.setEmail(
                        ds.child(userID)
                                .getValue(User.class)
                                .getEmail()
                );

                user.setPhone_number(
                        ds.child(userID)
                                .getValue(User.class)
                                .getPhone_number()
                );

                Log.d(TAG, "getUserInformation: retrieve user  information: " + user.toString());

            }

        }

        return new UserSettings(user, settings);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.editProfile:
                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
                intent.putExtra(getString(R.string.calling_activity), getString(R.string.profile_activity));
                startActivity(intent);

                break;
            case R.id.profileMenu:

                ((UserProfileActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
                Log.d(TAG, "onClick: navigating to account settings");

                startActivity(new Intent(mContext, AccountSettingsActivity.class));

                break;
        }

    }

    /**
     * Bottom Navigation View setup
     */
    public void setupBottomNavigationView() {
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }

}
