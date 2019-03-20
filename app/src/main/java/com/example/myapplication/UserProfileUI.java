package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.fragments.UserProfileOnePostFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileUI extends AppCompatActivity implements View.OnClickListener {

    private TextView userName,textId;
    private String userUID,userEmail;
    private FirebaseAuth mAuth;
    private ImageButton firstType, secondType, thirdType, forthType;
    private UserProfileOnePostFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initLayout();
        setListeners();
    }

    private void initLayout() {

        userName = findViewById(R.id.ty_name);
        textId = findViewById(R.id.textId);

        mAuth = FirebaseAuth.getInstance();

        Intent getLoginIntent = getIntent();

        userUID = getLoginIntent.getStringExtra("userUid");
        userEmail = getLoginIntent.getStringExtra("userEmail");

        firstType = findViewById(R.id.firstTypePost);
        secondType = findViewById(R.id.secondTypePost);
        thirdType = findViewById(R.id.thirdTypePost);
        forthType = findViewById(R.id.forthTypePost);


        FragmentManager fm = getSupportFragmentManager();
        fragment = (UserProfileOnePostFragment) fm.findFragmentById(R.id.fragment);
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commit();

    }

    private void setListeners() {
        firstType.setOnClickListener(this);
        secondType.setOnClickListener(this);
        thirdType.setOnClickListener(this);
        forthType.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            userName.setText(getString(R.string.user_status_fmt, user.getDisplayName()));
            textId.setText(getString(R.string.user_status_fmt, user.getEmail()));
        } else {
            userName.setText(userUID);
            textId.setText(userEmail);

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.firstTypePost:
                Log.i("click", "menu");

                showHideFragment(fragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showHideFragment(final Fragment fragment) {

        final FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
        fragTransaction.setCustomAnimations(R.anim.left_enter, R.anim.left_out);

        if (fragment.isHidden()) {
            fragTransaction.show(fragment);
            Log.d("hidden", "Show");
        } else {
            fragTransaction.hide(fragment);
            Log.d("Shown", "Hide");
        }

        fragTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.firstTypePost:
                Toast.makeText(this,
                        "ImageButton 1 is clicked!", Toast.LENGTH_SHORT).show();


                showHideFragment(fragment);
//                Fragment fragment = new UserProfileOnePostFragment();
//
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.userProfileFragmentId, fragment ); // give your fragment container id in first parameter
//                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
//                transaction.commit();


//                transaction.replace(R.id.userProfileFragmentId, fragment).commit();


//                fm = getSupportFragmentManager();
//                UserProfileOnePostFragment fragment = new UserProfileOnePostFragment();
//                fm.beginTransaction().add(R.id.userProfileFragmentId, fragment).commit();

                break;
            case R.id.secondTypePost:
                Toast.makeText(this,
                        "ImageButton 2 is clicked!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.thirdTypePost:
                Toast.makeText(this,
                        "ImageButton 3 is clicked!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.forthTypePost:
                Toast.makeText(this,
                        "ImageButton 4 is clicked!", Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
