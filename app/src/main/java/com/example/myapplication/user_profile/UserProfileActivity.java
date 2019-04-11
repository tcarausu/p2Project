package com.example.myapplication.user_profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.myapplication.R;
import com.example.myapplication.login.LoginActivity;
import com.example.myapplication.utility_classes.BaseActivity;
import com.example.myapplication.utility_classes.BottomNavigationViewHelper;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class UserProfileActivity extends AppCompatActivity
        implements View.OnClickListener {

    private static final String TAG = "UserProfileActivity";

    private static final int ACTIVITY_NUM = 4;

    private Context mContext = UserProfileActivity.this;

    private TextView userEmail;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private ImageView mProfilePhoto;
    private android.widget.Toolbar toolbar ;
    private Button logoutButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        logoutButton = findViewById(R.id.logoutButton_id);
        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);



        initLayout();
        setListeners();
        setupBottomNavigationView();
        setupToolbar();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                mAuth.signOut();//sign out user
                goToLoging();// go back to login
                Log.d(TAG, "onClick: "+ mAuth.getCurrentUser()); // check and see if user is null

            }
        });


    }

    private void initLayout() {

        userEmail = findViewById(R.id.displayUserName);
        mProfilePhoto = findViewById(R.id.profileImage);
        findViewById(R.id.profileMenu);
    }

    private void setListeners() {
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d(TAG, "onClick: "+ mAuth.getCurrentUser().getEmail());
        Log.d(TAG, "onClick: signed in with email is verified :"+ currentUser.isEmailVerified());
        if (currentUser == null){
            goToLoging();
        }
        else return;
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

    private void goToLoging() {
        Intent i = new Intent(this , LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    private void setupToolbar() {
         toolbar =  findViewById(R.id.profileToolBar);
        setActionBar(toolbar); //better use supportActionBar

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profileMenu:
                        Log.d(TAG, "onMenuItemClick: Navigating to ProfilePreferences.");
                }
                return false;
            }
        });


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
