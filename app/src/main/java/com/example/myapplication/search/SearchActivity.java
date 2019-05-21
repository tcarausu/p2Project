package com.example.myapplication.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.models.User;
import com.example.myapplication.utility_classes.BottomNavigationViewHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;


public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SearchActivity";
    private static final int ACTIVITY_NUM = 1;

    private Context mContext = SearchActivity.this;

    //widgets
    private EditText mSearchParam;
    private ImageButton mSearchButton;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser user;
    private DatabaseReference myDatabaseUserRef;
    private DatabaseReference myDatabaseUserRefById;

    //user data strings
    private String mUsername;
    private String mProfilePhoto;
    private String mUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Log.d(TAG, "OnCreate: started.");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mUserId = user.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myDatabaseUserRef = FirebaseDatabase.getInstance().getReference("users");

        initLayout();
        buttonListeners();

        setupBottomNavigationView();

    }


    private void initLayout() {
        mSearchParam = findViewById(R.id.search_bar_id);
        mSearchButton = findViewById(R.id.search_button_id);
    }

    private void buttonListeners() {
        mSearchParam.setOnClickListener(this);
        mSearchButton.setOnClickListener(this);
    }

    public void getUserFromDatabase() {
        myDatabaseUserRefById = firebaseDatabase.getReference("users/" + mUserId);

        myDatabaseUserRefById.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.getValue(User.class);

                mUsername = user.getUsername();
                mProfilePhoto = user.getProfile_photo();

                Log.d(TAG, "onDataChange: profilePic and username :" + mProfilePhoto + " " + mUsername);
                Toast.makeText(mContext, "The username is: " + mUsername, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.search_bar_id:
                Toast.makeText(mContext, "Bulshit when  i searth", Toast.LENGTH_SHORT).show();

                break;

            case R.id.search_button_id:
                getUserFromDatabase();

                break;
        }
    }
}


