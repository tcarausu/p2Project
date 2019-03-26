package com.example.myapplication.user_profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.myapplication.R;
import com.example.myapplication.utility_classes.BottomNavigationViewHelper;
import com.example.myapplication.utility_classes.SectionsStatePagerAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

/**
 * File created by tcarau18
 **/
class AccountSettingsActivity extends AppCompatActivity
        implements View.OnClickListener {
    private static final String TAG = "AccountSettingsActivity";
    private static final int ACTIVITY_NUM = 4;

    private Context mContext;

    private SectionsStatePagerAdapter pagerAdapter;
    private ViewPager mViewPager;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        initLayout();
        setupBottomNavigationView();
        setupSettingsList();
        setupFragments();
    }

    private void initLayout() {
        mContext = AccountSettingsActivity.this;
        findViewById(R.id.backArrow);
        Log.d(TAG, "onCreate: started account");

        mViewPager = findViewById(R.id.container);
        mRelativeLayout = findViewById(R.id.relativeLayout1);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.backArrow:
                Log.d(TAG, "onClick: navigating to account settings");

                finish();

                break;
        }

    }

    private void setupViewPager(int fragmentNr) {
        mRelativeLayout.setVisibility(View.GONE);
        Log.d(TAG, "setupViewPager: navigating to fragment #:" + fragmentNr);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(fragmentNr);
    }

    private void setupFragments() {
        pagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new EditProfileFragment(), getString(R.string.edit_your_profile_fragment)); //fragment 0
        pagerAdapter.addFragment(new SignOutFramgnet(), getString(R.string.sign_out_frament)); //fragment 1
    }

    private void setupSettingsList() {
        Log.d(TAG, "setupSettingsList: initializing 'Account Settings' list");

        ListView listView = findViewById(R.id.listViewAccountSettings);

        ArrayList<String> options = new ArrayList<>();
        options.add(getString(R.string.edit_your_profile_fragment));
        options.add(getString(R.string.sign_out_frament));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, options);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: navigating to fragment nr " + position);
                setupViewPager(position);
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

}
