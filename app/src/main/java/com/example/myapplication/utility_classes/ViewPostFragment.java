package com.example.myapplication.utility_classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.models.Photo;
import com.example.myapplication.models.User;
import com.example.myapplication.user_profile.AccountSettingsActivity;
import com.example.myapplication.user_profile.UserProfileActivity;
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
public class ViewPostFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ViewPostFragment";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    //widgets
    private SquareImageView mPostImage;
    private CircleImageView mProfilePhoto;
    private ImageView profileMenu, heartRed, heartWhite, comments, share, backArrow;
    private TextView mUserName, mPostLikes, mPostCaption, mPostCommentLink, mPostTimeStamp;
    private Toolbar toolbar;

    //vars
    private BottomNavigationViewEx bottomNavigationViewEx;
    private FirebaseMethods firebaseMethods;
    private Photo mPhoto;
    private Context mContext;
    private int mActivityNumber = 0;

    //vars for Query
    private String photoUsername = "", photoURL = "";
    private User user;
    private Heart heart;
    private GestureDetector mGestureDetector;

    public ViewPostFragment() {
        super();
        setArguments(new Bundle());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_post, container, false);

        mAuth = FirebaseAuth.getInstance();

        initLayout(view);
        setListeners(view);
        setupFirebaseAuth();

        setupBottomNavigationView();
        getPhotoDetails();

//         setupWidgets();

        testToggle();

        return view;
    }

    private void initLayout(View view) {
        mContext = getActivity();
        firebaseMethods = new FirebaseMethods(mContext);

        mUserName = view.findViewById(R.id.username);
        mProfilePhoto = view.findViewById(R.id.profile_photo);
        mPostImage = view.findViewById(R.id.post_image);

        mPostLikes = view.findViewById(R.id.post_likes);
        mPostCaption = view.findViewById(R.id.post_caption);
        mPostCommentLink = view.findViewById(R.id.post_comment_link);
        mPostTimeStamp = view.findViewById(R.id.post_TimeStamp);

        heartRed = view.findViewById(R.id.image_heart_red);
        heartWhite = view.findViewById(R.id.image_heart_white);

        heartRed.setVisibility(View.GONE);
        heartWhite.setVisibility(View.VISIBLE);

        bottomNavigationViewEx = view.findViewById(R.id.bottomNavigationBar);

        heart = new Heart(heartWhite, heartRed);

        mGestureDetector = new GestureDetector(getActivity(), new GestureListener());
        try {
            mPhoto = getPhotoFromBundle();
            UniversalImageLoader.setImage(mPhoto.getImage_path(), mPostImage, null, "");
            mActivityNumber = getActivityNumberBundle();

        } catch (NullPointerException e) {
            Log.e(TAG, "initLayout: NullPointerException " + e.getMessage());
        }
    }

    private void setListeners(View view) {
        profileMenu = view.findViewById(R.id.show_more);
        backArrow = view.findViewById(R.id.backArrow);

        comments = view.findViewById(R.id.speech_bubble);
        share = view.findViewById(R.id.share);

        profileMenu.setOnClickListener(this);
        backArrow.setOnClickListener(this);
        heartRed.setOnClickListener(this);
        comments.setOnClickListener(this);
        share.setOnClickListener(this);

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
                    Log.d(TAG, "onAuthStateChanged: signed in with: " + user.getUid());
                } else Log.d(TAG, "onAuthStateChanged: signed out");
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //retrive user information from the database
//                setProfileWidgets(firebaseMethods.getUserSettings(dataSnapshot));

                //retrive images for the user in question

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getPhotoDetails() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        Query query = reference.child(mAuth.getCurrentUser().getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()
                        && dataSnapshot.getKey()
                        .equals(mPhoto.getUser_id())) {
                    user = dataSnapshot.getValue(User.class);

                } else {
                    Log.d(TAG, "onDataChange: NOPE: " + dataSnapshot.getValue(User.class).getProfile_photo());
                    Toast.makeText(mContext, "trololo", Toast.LENGTH_SHORT).show();

                }
                setupWidgets();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d(TAG, "onCancelled: Query Cancelled");

            }
        });
    }

    private void setupWidgets() {
        String timeStampDiff = getTimeStampDifference();
        if (!timeStampDiff.equals(String.valueOf(0))) {
            mPostTimeStamp.setText(timeStampDiff + " Days Ago");
        } else {
            mPostTimeStamp.setText("Today");
        }

        UniversalImageLoader.setImage(user.getProfile_photo(), mProfilePhoto, null, "");
        mUserName.setText(user.getUsername());
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

        final String photoTimeStamp = mPhoto.getDate_created();
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
     * retrieves the Photo from the incoming bundle from ProfileActivity interface
     *
     * @return photo
     */
    private Photo getPhotoFromBundle() {
        Log.d(TAG, "getPhotoFromBundle: arguments: " + getArguments());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            return bundle.getParcelable(getString(R.string.photo));
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

    @SuppressLint("ClickableViewAccessibility")
    private void testToggle() {
        heartRed.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch: make white");

                return mGestureDetector.onTouchEvent(event);
            }
        });

        heartWhite.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch: make red");

                return mGestureDetector.onTouchEvent(event);

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.show_more:

                ((UserProfileActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
                Log.d(TAG, "onClick: navigating to account settings");

                startActivity(new Intent(mContext, AccountSettingsActivity.class));

                break;

            case R.id.speech_bubble:
                Toast.makeText(mContext, "cooment desu MOSHI", Toast.LENGTH_SHORT).show();

                break;

            case R.id.share:
                Toast.makeText(mContext, "share desu desu MOSHI", Toast.LENGTH_SHORT).show();

                break;

            case R.id.backArrow:
                Log.d(TAG, "onClick: navigating back to " + getActivity());

                Toast.makeText(mContext, "Boii", Toast.LENGTH_SHORT).show();
                getActivity().finish();

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
        MenuItem menuItem = menu.getItem(mActivityNumber);
        menuItem.setChecked(true);

    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return super.onDown(e);
        }

//        @Override
//        public boolean onDoubleTap(MotionEvent e) {
//            Log.d(TAG, "onTouch: double tap");
//
//            heart.toggleLike();
//
//            return true;
//        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d(TAG, "onTouch:long press");

            heart.toggleLike();

            super.onLongPress(e);
        }
    }
}
