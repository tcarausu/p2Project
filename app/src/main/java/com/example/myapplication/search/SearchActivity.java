package com.example.myapplication.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.home.HomeActivity;
import com.example.myapplication.models.User;
import com.example.myapplication.utility_classes.BottomNavigationViewHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SearchActivity";
    private static final int ACTIVITY_NUM = 1;

    //widgets
    private EditText mSearchParam;
    private ImageView backArrow;
    private ImageButton mSearchButton;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser user;
    private DatabaseReference myDatabaseUserRef;

    //user data strings
    private String mUsername;
    private String mProfilePhoto;
    private String mUserId;

    //ListView
    private ListView search_list_view;
    private List<User> userList = new ArrayList<>();
    private SearchActivityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Log.d(TAG, "OnCreate: started.");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mUserId = user.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myDatabaseUserRef = FirebaseDatabase.getInstance().getReference("userList");

        initLayout();
        buttonListeners();

        setupBottomNavigationView();

    }


    private void initLayout() {
        mSearchParam = findViewById(R.id.search_bar_id);
        backArrow = findViewById(R.id.backArrow);
        mSearchButton = findViewById(R.id.search_button_id);
        search_list_view = findViewById(R.id.search_view_list);


    }

    private void buttonListeners() {
        backArrow.setOnClickListener(this);
        mSearchParam.setOnClickListener(this);
        mSearchButton.setOnClickListener(this);
    }

    public void getUserFromDatabase() {
        String keyword = mSearchParam.getText().toString();

        Query query = myDatabaseUserRef
//                .orderByChild(myDatabaseUserRef
//                        .child(getString(R.string.field_user_id))
//                        .child(getString(R.string.field_username))
//                        .getKey()
//                )
                ;

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: buls" + ds.toString());
                    final User user = ds.getValue(User.class);
                    mUsername = user.getUsername();
                    mProfilePhoto = user.getProfile_photo();

                    if (ds.exists()
                            && !mUsername.equals(keyword)) {

                        adapter = new SearchActivityAdapter(getApplicationContext(), userList);

                        userList.add(user);
                        search_list_view.setAdapter(adapter);

                        Toast.makeText(SearchActivity.this, "Here is the user: " + mUsername, Toast.LENGTH_SHORT).show();

                        Log.d(TAG, "onDataChange: profilePic and username :" + mProfilePhoto + " " + mUsername);


                    } else {
                        Toast.makeText(SearchActivity.this, "No such user exists", Toast.LENGTH_SHORT).show();

                    }
                }
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
        BottomNavigationViewHelper.enableNavigation(getApplicationContext(), bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.backArrow:
                startActivity(new Intent(getApplicationContext(), HomeActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                break;

            case R.id.search_button_id:
                getUserFromDatabase();

                break;
        }
    }
}


