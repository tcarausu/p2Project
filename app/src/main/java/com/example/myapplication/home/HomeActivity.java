package com.example.myapplication.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.login.LoginActivity;
import com.example.myapplication.postActivity.AddPostActivity;
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

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private final int ACTIVITY_NUM1 = 1, ACTIVITY_NUM2 = 2, ACTIVITY_NUM3 = 3, ACTIVITY_NUM4 = 4;
    private final List<Integer> act = new ArrayList<>();

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
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

        connectDatabase();
        checkDatabaseState();
        initImageLoader();
        setupBottomNavigationView();
        setupViewPager();
    }

    private void connectDatabase() {
        mFirebaseMethods = FirebaseMethods.getInstance(getApplicationContext());
        mAuth = FirebaseMethods.getAuth();
        current_user = mAuth.getCurrentUser();
        mFirebaseMethods.autoDisconnect(getApplicationContext());
        firebasedatabase = FirebaseMethods.getmFirebaseDatabase();
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mFirebaseMethods.autoDisconnect(getApplicationContext());
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
                        mFirebaseMethods.goToWhereverWithFlags(getApplicationContext(), AddPostActivity.class);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled: ");
                }
            });
        } catch (NullPointerException e) {
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

                        mFirebaseMethods.goToWhereverWithFlags(getApplicationContext(), LoginActivity.class);
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

                    List<String> keyList = new ArrayList<>();
                    boolean exists = false;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        keyList.add(ds.getKey());
                    }
                    for (String userId : keyList) {
                        if (userId.contains(current_user.getUid()))
                            exists = true;
                    }

                    if (!exists) {

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

        try {

            current_userRef = firebasedatabase.getReference("users").child(current_user.getUid()).getRef();
            current_userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mFirebaseMethods.autoDisconnect(getApplicationContext());
                    String name = current_userRef.child("display_name").getKey();
                    if (name.equals("Chose a user name")) {
                        mFirebaseMethods.goToWhereverWithFlags(getApplicationContext(), UserProfileActivity.class);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), "what the fuck is happening?", Toast.LENGTH_LONG).show();
//            mFirebaseMethods.autoDisconnect(getApplicationContext());
        }


    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mFirebaseMethods.autoDisconnect(getApplicationContext());
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


    }

    /**
     * checks to see if @param 'user'  is logged in
     */


    private void initImageLoader() {
        UniversalImageLoader imageLoader = new UniversalImageLoader(getApplicationContext());
        ImageLoader.getInstance().init(imageLoader.getConfig());
    }

    /**
     * Bottom Navigation View setup
     */
    private void setupBottomNavigationView() {

        BottomNavigationViewHelper bnvh = new BottomNavigationViewHelper(getApplicationContext());
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigationBar);
        bnvh.setupBottomNavigationView(bottomNavigationViewEx);
        bnvh.enableNavigation(getApplicationContext(), bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();

        //Mo.Msaad.Modifications
        MenuItem menuItem1, menuItem2, menuItem3, menuItem4;
        act.add(ACTIVITY_NUM1);
        act.add(ACTIVITY_NUM2);
        act.add(ACTIVITY_NUM3);
        act.add(ACTIVITY_NUM4);


        switch (act.iterator().next()) {
            case 0:
                menuItem1 = menu.getItem(act.get(0));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                menuItem1.setChecked(true);
                break;

            case 1:
                menuItem2 = menu.getItem(act.get(1));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                menuItem2.setChecked(true);
                break;
            case 2:
                menuItem3 = menu.getItem(act.get(2));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                menuItem3.setChecked(true);
                break;
            case 3:
                menuItem4 = menu.getItem(act.get(3));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                menuItem4.setChecked(true);
                break;
        }

    }


}
