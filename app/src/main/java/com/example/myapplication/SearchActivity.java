package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.myapplication.utility_classes.BottomNavigationViewHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * File created by tcarau18
 **/
public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        initLayout();
        buttonListners();
        setupBottomNavigationView();
    }


    public void initLayout() {


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
