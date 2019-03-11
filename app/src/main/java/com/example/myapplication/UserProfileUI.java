package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileUI extends AppCompatActivity {

    private TextView ty_name;
    private String userUID;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        ty_name = findViewById(R.id.ty_name);

        mAuth = FirebaseAuth.getInstance();

        Intent getLoginIntent = getIntent();

        userUID = getLoginIntent.getStringExtra("userUid");
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
            ty_name.setText(getString(R.string.user_status_fmt, user.getDisplayName()));
        } else {
            ty_name.setText(userUID);

        }
    }
}
