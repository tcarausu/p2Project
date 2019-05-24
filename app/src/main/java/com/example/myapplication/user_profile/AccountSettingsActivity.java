package com.example.myapplication.user_profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.login.LoginActivity;
import com.example.myapplication.utility_classes.BottomNavigationViewHelper;
import com.example.myapplication.utility_classes.FirebaseMethods;
import com.example.myapplication.utility_classes.SectionsStatePagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.Collections;

/**
 * File created by tcarau18
 **/
public class AccountSettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AccountSettingsActivity";
    private static final int ACTIVITY_NUM = 4;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseMethods mFirebaseMethods ;
//    private GoogleSignInClient mGoogleSignInClient;


    private SectionsStatePagerAdapter pagerAdapter;
    private ViewPager mViewPager;
    private RelativeLayout mRelativeLayout;
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        mFirebaseMethods =  FirebaseMethods.getInstance(getApplicationContext());
        mAuth = FirebaseMethods.getAuth();

        initLayout();
        setupBottomNavigationView();
        setupSettingsList();
        setupFragments();
        getIncomingIntent();
    }

    private void initLayout() {

        listView = findViewById(R.id.listViewAccountSettings);
        findViewById(R.id.backArrow);
        Log.d(TAG, "onCreate: started account");
        mViewPager = findViewById(R.id.container);
        mRelativeLayout = findViewById(R.id.relativeLayout1);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backArrow) {
            Log.d(TAG, "onClick: navigating to account settings");
            goTosWithFlags(getApplicationContext(),UserProfileActivity.class);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mFirebaseMethods.checkUserStateIfNull()) {
            mFirebaseMethods.logOut();
            goTosWithFlags(AccountSettingsActivity.this, LoginActivity.class);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFirebaseMethods.checkUserStateIfNull()) {
            mFirebaseMethods.logOut();
            goTosWithFlags(AccountSettingsActivity.this, LoginActivity.class);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mFirebaseMethods.checkUserStateIfNull()) {
            mFirebaseMethods.logOut();
            goTosWithFlags(AccountSettingsActivity.this, LoginActivity.class);
        }
    }

    private void setupViewPager(int fragmentNr) {

        mRelativeLayout.setVisibility(View.GONE);
        Log.d(TAG, "setupViewPager: navigating to fragment #:" + fragmentNr);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(fragmentNr);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void setupFragments() {
        pagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new EditProfileFragment(), getString(R.string.edit_your_profile_fragment));

    }

    private void setupSettingsList() {
        Log.d(TAG, "setupSettingsList: initializing 'Account Settings' list");
        ListView listView = findViewById(R.id.listViewAccountSettings);

//        ArrayList<String> options = new ArrayList<>();
//        options.add(getString(R.string.sign_out_fragment));
         String signOut = "Sign Out";

        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, Collections.singletonList(signOut));
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Log.d(TAG, "onItemClick: navigating to fragment nr " + position);

            if (position == 0) {
                dialogChoice();
            }
        });
    }

    private void getIncomingIntent() {
        Intent intent = getIntent();

        if (intent.hasExtra(getString(R.string.calling_activity))) {
            Log.d(TAG, "getIncomingIntent: received incoming intent from" + getString(R.string.profile_activity));
            setupViewPager(pagerAdapter.getFragmentNumber(getString(R.string.edit_your_profile_fragment)));
        }
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

    // Alert dialog
    private void dialogChoice() {

        final CharSequence[] options = {"SIGN-OUT", "CANCEL"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to sign out?");
        builder.setIcon(R.drawable.chefood);

        builder.setItems(options, (dialog, which) -> {
            if (options[which].equals("SIGN-OUT")) {
                Log.d(TAG, "dialogChoice: sign-out");
                mFirebaseMethods.logOut();
                ProgressBar mProgressBar = new ProgressBar(this);
                mProgressBar.setVisibility(View.VISIBLE);

                goTosWithFlags(getApplicationContext(),LoginActivity.class);
                Toast.makeText(getApplicationContext(), "Successful Sign Out", Toast.LENGTH_SHORT).show();

            } else if (options[which].equals("CANCEL")) {
                Log.d(TAG, "dialogChoice: cancel");
                dialog.dismiss();
            }
        });

        builder.show();

    }

    public void goTosWithFlags(Context applicationContext, Class<? extends AppCompatActivity> cl) {
        startActivity(new Intent(applicationContext,cl)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }
}