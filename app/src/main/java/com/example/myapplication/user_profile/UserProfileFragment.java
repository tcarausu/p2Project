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
import com.example.myapplication.models.Photo;
import com.example.myapplication.models.User;
import com.example.myapplication.models.UserAccountSettings;
import com.example.myapplication.models.UserSettings;
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

    //firebase
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

    private FirebaseMethods firebaseMethods;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();

        initLayout(view);
        setListeners(view);
        setupFirebaseAuth();

//        setupGridView();

        setupBottomNavigationView();

        return view;
    }

    private void initLayout(View view) {
        mContext = getActivity();
        firebaseMethods = new FirebaseMethods(mContext);

        mDisplayName = view.findViewById(R.id.displayName);
        mUserName = view.findViewById(R.id.userName);
        mWebsite = view.findViewById(R.id.website);
        mDescription = view.findViewById(R.id.description);
        mPosts = view.findViewById(R.id.tvPosts);
        mFollowers = view.findViewById(R.id.tvFollowers);
        mFollowing = view.findViewById(R.id.tvFollowing);

        gridView = view.findViewById(R.id.gridImageView);

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

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");

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
                setProfileWidgets(firebaseMethods.getUserSettings(dataSnapshot));

                //retrive images for the user in question

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setProfileWidgets(UserSettings userSettings) {
        Log.d(TAG, "setProfileWidgets: setting widgets with data, retrieving from database: " +
                userSettings.toString());
        User user = userSettings.getUser();
        UserAccountSettings settings = userSettings.getSettings();

//        UniversalImageLoader.setImage(settings.getProfile_photo(), mProfilePhoto, null, "");

        mDisplayName.setText(settings.getDisplay_name());
        mUserName.setText(settings.getUsername());
        mWebsite.setText(settings.getWebsite());
        mDescription.setText(settings.getDescription());
        mFollowers.setText(String.valueOf(settings.getFollowers()));
        mFollowing.setText(String.valueOf(settings.getFollowing()));
        mPosts.setText(String.valueOf(settings.getPosts()));

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

    private void setupGridView() {
        Log.d(TAG, "setupGridView: Setting up GridView");

        final ArrayList<Photo> photos = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference
                .child(getString(R.string.dbname_user_photos))
                .child(mAuth.getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    photos.add(singleSnapshot.getValue(Photo.class));
                }

                //setup  our grid image
                int gridWidth = getResources().getDisplayMetrics().widthPixels;
                int imageWidth = gridWidth / NUM_GRID_COLUMNS;

                gridView.setColumnWidth(imageWidth);

                ArrayList<String> imgURLs = new ArrayList<>();

                for (int i = 0; i < photos.size(); i++) {
                    imgURLs.add(photos.get(i).getImage_path());
                }

                GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_imageview,
                        "", imgURLs);

                gridView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d(TAG, "onCancelled: Query Cancelled");

            }
        });
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
