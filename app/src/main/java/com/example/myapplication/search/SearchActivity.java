package com.example.myapplication.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.login.LoginActivity;
import com.example.myapplication.utility_classes.BottomNavigationViewHelper;
import com.example.myapplication.utility_classes.SectionsPagerAdapter;
import com.example.myapplication.utility_classes.UniversalImageLoader;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Objects;


public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";

    private static final int ACTIVITY_NUM = 1;

    private Context mContext;

    private String userUID;
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private FirebaseAuth.AuthStateListener mAuthListener;

    /**
     * @param savedInstanceState creates the app using the Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser();

        initLayout();
        buttonListeners();
        initImageLoader();
        setupBottomNavigationView();
        setupFirebaseAuth();
        setupViewPager();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(mAuthListener);
        checkCurrentUser();

        if (current_user == null) {
            mAuth.signOut();
            LoginManager.getInstance().logOut();
            sendUserToLogin();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void initLayout() {
        mContext = SearchActivity.this;

        mAuth = FirebaseAuth.getInstance();
        Intent getLoginIntent = getIntent();
        userUID = getLoginIntent.getStringExtra("userUid");
    }

    public void buttonListeners() {

    }

    private void sendUserToLogin() {

        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();

        mAuth.addAuthStateListener(mAuthListener);

        checkCurrentUser();
    }

    /**
     * Used for adding the tabs: Camera, Home and Direct Messages
     */
    private void setupViewPager() {

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SearchFragment()); //index 1
        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.home);
    }

    /**
     * checks to see if @param 'user'  is logged in
     */
    private void checkCurrentUser() {
        Log.d(TAG, "checkCurrentUser: checking if user is logged in");

        if (current_user == null) {
            Toast.makeText(mContext, "Your have to Authenticate first before proceeding", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            LoginManager.getInstance().logOut();
            sendUserToLogin();
        }

    }

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = firebaseAuth -> {


            checkCurrentUser();

            if (current_user != null) {
                Log.d(TAG, "onAuthStateChanged: signed in" + current_user.getUid());
            } else Log.d(TAG, "onAuthStateChanged: signed out");
        };

    }

    private void initImageLoader() {
        UniversalImageLoader imageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(imageLoader.getConfig());
    }

    /**
     * Bottom Navigation View setup
     */
    public void setupBottomNavigationView() {
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigationBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }
}


