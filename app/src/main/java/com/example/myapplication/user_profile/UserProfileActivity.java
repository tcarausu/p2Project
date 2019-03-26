package com.example.myapplication.user_profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.myapplication.R;
import com.example.myapplication.utility_classes.BottomNavigationViewHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class UserProfileActivity extends AppCompatActivity
        implements View.OnClickListener {

    private static final String TAG = "UserProfileActivity";

    private static final int ACTIVITY_NUM = 4;

    private Context mContext = UserProfileActivity.this;

    private TextView displayUserName;
    private String userUID;
    private FirebaseAuth mAuth;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initLayout();
        setListeners();
        setupBottomNavigationView();
        setupToolbar();
    }

    private void initLayout() {

        displayUserName = findViewById(R.id.displayUserName);

        mProgressBar = findViewById(R.id.profile_progress_bar);
        mProgressBar.setVisibility(View.GONE);

        findViewById(R.id.profileMenu);

        mAuth = FirebaseAuth.getInstance();

        Intent getLoginIntent = getIntent();

        userUID = getLoginIntent.getStringExtra("userUid");
    }

    private void setListeners() {
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.profileMenu:
                Log.d(TAG, "onClick: navigating to account settings");

                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
                startActivity(intent);

                break;
        }

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            displayUserName.setText(getString(R.string.user_status_fmt, user.getDisplayName()));
        } else {
            displayUserName.setText(userUID);
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.profileToolBar);
        setActionBar(toolbar);

//
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Log.d(TAG, "onMenuItemClick: clicked menu item" + item);
//
//                switch (item.getItemId()) {
//                    case R.id.profileMenu:
//                        Log.d(TAG, "onMenuItemClick: Navigating to ProfilePreferences.");
//
//                }
//
//                return false;
//            }
//        });


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
