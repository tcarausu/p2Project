package com.example.myapplication.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myapplication.R;
import com.example.myapplication.utility_classes.BottomNavigationViewHelper;
import com.example.myapplication.utility_classes.FirebaseMethods;
import com.example.myapplication.utility_classes.SectionsPagerAdapter;
import com.example.myapplication.utility_classes.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;


public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";
    private static final int ACTIVITY_NUM = 1;


    private String userUID;
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods mFirebaseMethods ;

    /**
     * @param savedInstanceState creates the app using the Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mFirebaseMethods = FirebaseMethods.getInstance(getApplicationContext());
        mAuth = FirebaseMethods.getAuth();
        current_user = mAuth.getCurrentUser();

        setupFirebaseAuth();
        initLayout();
        buttonListeners();
        initImageLoader();
        setupViewPager();
        setupBottomNavigationView();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(mAuthListener);
        mFirebaseMethods.checkUserStateIfNull(getApplicationContext(),mAuth);


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseMethods.checkUserStateIfNull(getApplicationContext(),mAuth);
    }

    public void initLayout() {
        mAuth = FirebaseAuth.getInstance();
        Intent getLoginIntent = getIntent();
        userUID = getLoginIntent.getStringExtra("userUid");
    }

    public void buttonListeners() {

    }

    /**
     * Used for adding the tabs: Camera, Home and Direct Messages
     */
    private void setupViewPager() {

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SearchFragment()); //index 1

        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(adapter);

    }

    /**
     * checks to see if @param 'user'  is logged in
     */


    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = firebaseAuth -> {

            mFirebaseMethods.checkUserStateIfNull(getApplicationContext(),mAuth);

            if (current_user != null) {
                Log.d(TAG, "onAuthStateChanged: signed in" + current_user.getUid());
            } else Log.d(TAG, "onAuthStateChanged: signed out");
        };

    }

    private void initImageLoader() {
        UniversalImageLoader imageLoader = new UniversalImageLoader(getApplicationContext());
        ImageLoader.getInstance().init(imageLoader.getConfig());
    }

    /**
     * Bottom Navigation View setup
     */
    public void setupBottomNavigationView() {
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigationBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(getApplicationContext(), bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }
}


