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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.login.LoginActivity;
import com.example.myapplication.models.Photo;
import com.example.myapplication.models.User;
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

    OnGridImageSelectedListener onGridImageSelectedListener;

    private static final int ACTIVITY_NUM = 4;

    private static final int NUM_GRID_COLUMNS = 3;


    private BottomNavigationViewEx bottomNavigationViewEx;
    private ProgressBar mProgressBar;
    private CircleImageView mProfilePhoto;
    private ImageView profileMenu;
    private GridView gridView;
    private Toolbar toolbar;
    private FirebaseMethods firebaseMethods;

    //firebase

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private TextView mEditProfile, mPosts, mFollowers, mFollowing, mUserName, mDisplayName, mWebsite, mAbout;


    private Uri avatarUri;
    private FirebaseUser current_user;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        avatarUri = Uri.parse("android.resource://com.example.myapplication/drawable/my_avatar");
        initLayout(view);
        setListeners(view);
        setupFirebaseAuth();
        setupGridView();
        setupBottomNavigationView();

        return view;
    }

    private void initLayout(View view) {
        firebaseMethods = new FirebaseMethods(getContext());
        mDisplayName = view.findViewById(R.id.displayName);
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

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: signed in" + user.getUid());
                } else
//                    mAuth.signOut();
//                    goToLogin();
                    Log.d(TAG, "onAuthStateChanged: signed out");


            }

            private void goToLogin() {
                startActivity(new Intent(getActivity(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                |Intent.FLAG_ACTIVITY_CLEAR_TASK));
                getActivity().finish();
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 current_user = mAuth.getCurrentUser();
                String photoRef = myRef.child(current_user.getUid()).child("profile_photo").getRef().toString();
                //retrive user information from the database
                if (photoRef.equals("photo")){
                    mProfilePhoto.setImageResource(R.mipmap.simo_design_avatar);
                }
                else
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
        User settings = userSettings.getUser();
        mDisplayName.setText(settings.getDisplay_name());
        mUserName.setText(settings.getUsername());
        mWebsite.setText(settings.getWebsite());
        mAbout.setText(settings.getAbout());
        mFollowers.setText(String.valueOf(settings.getFollowers()));
        mFollowing.setText(String.valueOf(settings.getFollowing()));
        mPosts.setText(String.valueOf(settings.getPosts()));
        String  profilePicURL = settings.getProfile_photo() ;

        //check for image profile url if null, to prevent app crushing when there is no link to profile image in database
        try {
            if (profilePicURL == null) {
                mProfilePhoto.setImageResource(R.drawable.my_avatar);

            } else
             Glide.with(this).load(profilePicURL).centerCrop().into(mProfilePhoto);

            }catch (Exception e){
            Log.d(TAG, "setProfileWidgets: Error: "+e.getMessage());
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

                startActivity(new Intent(getContext(), AccountSettingsActivity.class));

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

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        onGridImageSelectedListener.onGridImageSelected(photos.get(position), ACTIVITY_NUM);
                    }
                });
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
        BottomNavigationViewHelper.enableNavigation(getContext(), bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }

    public interface OnGridImageSelectedListener {
        void onGridImageSelected(Photo photo, int activityNr);
    }

}
