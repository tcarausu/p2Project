package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.myapplication.utility_classes.BottomNavigationViewHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class HomeActivity extends AppCompatActivity {

    private String userUID;
    private FirebaseAuth mAuth;

    /**
     * @param savedInstanceState creates the app using the Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        initLayout();
        buttonListners();
        setupBottomNavigationView();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void initLayout() {
        mAuth = FirebaseAuth.getInstance();

        Intent getLoginIntent = getIntent();

        userUID = getLoginIntent.getStringExtra("userUid");

    }

    public void buttonListners() {

    }

    /**
     * @param user is the Firebase User used to adjust/perform info exchange
     */
    private void updateUI(FirebaseUser user) {
        if (user != null) {
//            firebase_user_info.setText(getString(R.string.user_status_fmt, user.getDisplayName()));
        } else {
//            firebase_user_info.setText(userUID);

        }
    }

    /**
     * Bottom Navigation View setup
     */
    public void setupBottomNavigationView() {
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigationBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
    }
}
