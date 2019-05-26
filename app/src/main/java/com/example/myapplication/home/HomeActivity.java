package com.example.myapplication.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.login.LoginActivity;
import com.example.myapplication.models.User;
import com.example.myapplication.post.AddPostActivity;
import com.example.myapplication.user_profile.UserProfileActivity;
import com.example.myapplication.utility_classes.BottomNavigationViewHelper;
import com.example.myapplication.utility_classes.FirebaseMethods;
import com.example.myapplication.utility_classes.SectionsPagerAdapter;
import com.example.myapplication.utility_classes.UniversalImageLoader;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private static final int ACTIVITY_NUM = 0;

    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods mFirebaseMethods;
    private DatabaseReference mDatabasePostRef, mDatabaseUserRef, current_userRef;
    private FirebaseDatabase firebasedatabase;



    /**
     * @param savedInstanceState creates the app using the Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        mFirebaseMethods = FirebaseMethods.getInstance(getApplicationContext());
        mAuth = FirebaseMethods.getAuth();
        current_user = mAuth.getCurrentUser();

        mFirebaseMethods.checkUserStateIfNull(getApplicationContext(), mAuth);
        firebasedatabase = FirebaseMethods.getmFirebaseDatabase();
        checkDatabaseState();
        initImageLoader();
        setupBottomNavigationView();
        setupFirebaseAuth();
        setupViewPager();
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mFirebaseMethods.checkUserStateIfNull(getApplicationContext(), mAuth);


    }

    @SuppressLint("RestrictedApi")
    private void checkDatabaseState() {

        try {
            mDatabasePostRef = firebasedatabase.getReference("posts").getRef();
            mDatabasePostRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "Snapshot HomeActivity, Snapshot.has children: " + dataSnapshot.hasChildren());
                    if (!dataSnapshot.exists() || !dataSnapshot.hasChildren()) {
                        Toast.makeText(getApplicationContext(), "Nothing to display, Add a post to begin", Toast.LENGTH_SHORT).show();

                        mFirebaseMethods.goToWhereverWithFlags(getApplicationContext(),getApplicationContext(), AddPostActivity.class);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled: ");
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "checkDatabaseState: error: " + e.getMessage());
        }

        try {
            mDatabaseUserRef = firebasedatabase.getReference("users").getRef();
            mDatabaseUserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //i check if there is any users on the database
                    Log.d(TAG, "Snapshot HomeActivity, Snapshot.has children: " + dataSnapshot.hasChildren());
                    if (!dataSnapshot.exists() || !dataSnapshot.hasChildren()) {
                        mAuth.signOut();
                        LoginManager.getInstance().logOut();
                        Toast.makeText(getApplicationContext(), "Error finding this user on the database. please login in again", Toast.LENGTH_SHORT).show();

                        mFirebaseMethods.goToWhereverWithFlags(getApplicationContext(),getApplicationContext(), LoginActivity.class);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            Log.d(TAG, "checkDatabaseState: error: " + e.getMessage());
        }

        try {
            mDatabaseUserRef = firebasedatabase.getReference("users");
            mDatabaseUserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    List<String > keyList = new ArrayList<>();
                    boolean exists = false ;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        keyList.add(ds.getKey());
                    }
                        for (String  userId : keyList){
                            if (userId.contains(current_user.getUid()))
                                exists =true ;
                        }

                        if (!exists){

                            mAuth.signOut();
                            LoginManager.getInstance().logOut();
                            System.exit(0);
                        }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            Log.d(TAG, "checkDatabaseState: error: " + e.getMessage());
        }
        current_userRef = firebasedatabase.getReference("users").child(current_user.getUid()).getRef();
        current_userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                if (user.getDisplay_name().equals("Chose a user name")) {
                    mFirebaseMethods.goToWhereverWithFlags(getApplicationContext(),getApplicationContext(), UserProfileActivity.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mFirebaseMethods.checkUserStateIfNull(getApplicationContext(), mAuth);
        checkDatabaseState();
    }

    /**
     * Used for adding the tabs: Camera, Home and Direct Messages
     */
    private void setupViewPager() {

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment()); //index 1
        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.home);
    }

    /**
     * checks to see if @param 'user'  is logged in
     */

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFire-base-Auth: setting up fire-base auth");
        mAuthListener = firebaseAuth -> {

            if (current_user != null) {
                Log.d(TAG, "onAuthStateChanged: signed in" + current_user.getUid());
            } else Log.d(TAG, "onAuthStateChanged: signed out");
        };

    }

    private void initImageLoader() {
        UniversalImageLoader imageLoader = new UniversalImageLoader(getApplicationContext());
        ImageLoader.getInstance().init(imageLoader.getConfig());
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


}
