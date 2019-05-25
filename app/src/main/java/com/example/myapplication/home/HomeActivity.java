package com.example.myapplication.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.example.myapplication.post.AddPostActivity;
import com.example.myapplication.utility_classes.BottomNavigationViewHelper;
import com.example.myapplication.utility_classes.FirebaseMethods;
import com.example.myapplication.utility_classes.SectionsPagerAdapter;
import com.example.myapplication.utility_classes.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private static final int ACTIVITY_NUM = 0;

    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods mFirebaseMethods;
    private DatabaseReference mDatabasePostRef;
    private FirebaseDatabase firebasedatabase;
    private Query postQuery;


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
        firebasedatabase = FirebaseMethods.getmFirebaseDatabase();
        mDatabasePostRef = firebasedatabase.getReference("posts").getRef();

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

        mFirebaseMethods.checkUserStateIfNull(getApplicationContext(),mAuth,mAuth.getCurrentUser());
        checkDatabaseState();


    }

    @SuppressLint("RestrictedApi")
    private void checkDatabaseState() {

        try {
            mDatabasePostRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "Snapshot HomeActivity, Snapshot.has children: " + dataSnapshot.hasChildren());
                    if (!dataSnapshot.exists() || !dataSnapshot.hasChildren()) {
                        Toast.makeText(getApplicationContext(), "Nothing to display, Add a post to begin", Toast.LENGTH_SHORT).show();
                        goTosWithFlags(HomeActivity.this, AddPostActivity.class);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),"Canceled",Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "checkDatabaseState: error: "+e.getMessage());
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkDatabaseState();
        mFirebaseMethods.checkUserStateIfNull(getApplicationContext(),mAuth,mAuth.getCurrentUser());
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

    public void goTosWithFlags(Context context, Class<? extends AppCompatActivity> cl) {
        startActivity(new Intent(context, cl).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();

    }

}
