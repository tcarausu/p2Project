package com.example.myapplication.user_profile;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.myapplication.R;
import com.example.myapplication.login.LoginActivity;
import com.example.myapplication.utility_classes.BottomNavigationViewHelper;
import com.example.myapplication.utility_classes.FirebaseMethods;
import com.example.myapplication.utility_classes.OurAlertDialog;
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
    private FirebaseMethods mFirebaseMethods;

    //views
    private SectionsStatePagerAdapter pagerAdapter;
    private ViewPager mViewPager;
    private RelativeLayout mRelativeLayout;
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        mFirebaseMethods = FirebaseMethods.getInstance(getApplicationContext());
        mAuth = FirebaseMethods.getAuth();
        mFirebaseMethods.autoDisconnect(getApplicationContext());

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
            mFirebaseMethods.goToWhereverWithFlags(getApplicationContext(), UserProfileActivity.class);
            overridePendingTransition(R.anim.left_enter,R.anim.right_out);
        }

    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseMethods.autoDisconnect(getApplicationContext());

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void setupViewPager(int fragmentNr) {

        mRelativeLayout.setVisibility(View.GONE);
        Log.d(TAG, "setupViewPager: navigating to fragment #:" + fragmentNr);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(fragmentNr);
    }

    private void setupFragments() {
        pagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new EditProfileFragment(), getString(R.string.edit_your_profile_fragment));

    }

    private void setupSettingsList() {
        Log.d(TAG, "setupSettingsList: initializing 'Account Settings' list");
        String signOut = "Sign Out";
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, Collections.singletonList(signOut));
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Log.d(TAG, "onItemClick: navigating to fragment nr " + position);

            switch (position) {
                case 0:
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

        BottomNavigationViewHelper bnvh = new BottomNavigationViewHelper(getApplicationContext());
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigationBar);

        bnvh.setupBottomNavigationView(bottomNavigationViewEx);
        bnvh.enableNavigation(getApplicationContext(), bottomNavigationViewEx);
        bnvh.overridePendingTransition(R.anim.slide_down, R.anim.slide_up);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }

    // Alert dialog
    private void dialogChoice() {

        View layoutView = getLayoutInflater().inflate(R.layout.dialog_signout_layout, null);

        Button signOutButton = layoutView.findViewById(R.id.signOutButton);
        ImageButton cancelButton = layoutView.findViewById(R.id.cancelSignOutButton);

        OurAlertDialog.Builder myDialogBuilder = new OurAlertDialog.Builder(this);
        myDialogBuilder.setView(layoutView);
        myDialogBuilder.setIcon(R.mipmap.chefood_icones);
        final AlertDialog alertDialog = myDialogBuilder.create();
        WindowManager.LayoutParams wlp = alertDialog.getWindow().getAttributes();
        wlp.windowAnimations = R.style.AlertDialogAnimation;
        wlp.gravity = Gravity.CENTER;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;

        alertDialog.getWindow().setAttributes(wlp);
        alertDialog.setCanceledOnTouchOutside(true);
        // Setting transparent the background (layout) of alert dialog
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        signOutButton.setOnClickListener(v -> {
            mFirebaseMethods.signOut();
            mFirebaseMethods.goToWhereverWithFlags(AccountSettingsActivity.this, LoginActivity.class);
            overridePendingTransition(R.anim.left_enter,R.anim.right_out);
            alertDialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

    }
}