package com.example.myapplication.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.utility_classes.BottomNavigationViewHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

/*
things to do :
- implement a method to invoke the pictures from firebase
-fix the intent that invokes the correct user and shows its profile
- see if it crashesh if the user doesnt have a profile pic
* */


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Log.d(TAG, "OnCreate: started.");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
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
                Toast.makeText(mContext, "yeah i will search", Toast.LENGTH_SHORT).show();

                break;
        }
    }
}


