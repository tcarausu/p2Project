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
import com.example.myapplication.utility_classes.SectionsStatePagerAdapter;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

/**
 * File created by tcarau18
 **/
public class AccountSettingsActivity extends AppCompatActivity
        implements View.OnClickListener {
    private static final String TAG = "AccountSettingsActivity";
    private static final int ACTIVITY_NUM = 4;

    private Context mContext;

    //firebase
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private ListView listView;

    private SectionsStatePagerAdapter pagerAdapter;
    private ViewPager mViewPager;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("353481374608-mg7rvo8h0kgjmkuts5dcmq65h2louus5.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);

        initLayout();
        setupBottomNavigationView();
        setupSettingsList();
        setupFragments();
        getIncomingIntent();
    }

    private void initLayout() {
        mContext = AccountSettingsActivity.this;

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
            finish();
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            mAuth.signOut();
            goToLogin();
        }
    }

    public void goToLogin() {
        startActivity(new Intent(AccountSettingsActivity.this, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
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

        ArrayList<String> options = new ArrayList<>();
        options.add(getString(R.string.edit_your_profile_fragment));
        options.add(getString(R.string.sign_out_fragment));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, options);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Log.d(TAG, "onItemClick: navigating to fragment nr " + position);
            switch (position) {
                case 0:
                    setupViewPager(position);
                    break;

                case 1:
                    Log.d(TAG, "onItemClick: position nr " + position);

                    dialogChoice();
                    break;
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
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }

    // Alert dialog
    private void dialogChoice() {
        final CharSequence[] options = {"SIGNOUT", "CANCEL"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sign Out");
        builder.setIcon(R.drawable.chefood);

        builder.setItems(options, (dialog, which) -> {
            if (options[which].equals("SIGNOUT")) {
                Log.d(TAG, "dialogChoice: signout");
                ProgressBar mProgressBar = new ProgressBar(this);
                mProgressBar.setVisibility(View.VISIBLE);

                mAuth.signOut();
                mGoogleSignInClient.signOut();
                LoginManager.getInstance().logOut();

                this.finish();
                goToLogin();

                Toast.makeText(getApplicationContext(), "Successful Sign Out", Toast.LENGTH_SHORT).show();

            } else if (options[which].equals("CANCEL")) {
                Log.d(TAG, "dialogChoice: cancel");
                dialog.dismiss();

            }
        });

        builder.show();

    }
}