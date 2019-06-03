package com.example.myapplication.postActivity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myapplication.R;
import com.example.myapplication.utility_classes.BottomNavigationViewHelper;
import com.example.myapplication.utility_classes.SectionsPagerAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * File created by tcarau18
 **/
public class AddPostActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private static final int ACTIVITY_NUM = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        setupBottomNavigationView();
        setupViewPager();
    }

    private void setupViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SelectPictureFragment());
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(adapter);
    }

    //Mo.Msaad modifications
    private void setupBottomNavigationView() {

        BottomNavigationViewHelper bnvh = new BottomNavigationViewHelper(getApplicationContext());
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigationBar);
        bnvh.setupBottomNavigationView(bottomNavigationViewEx);
        bnvh.enableNavigation(getApplicationContext(), bottomNavigationViewEx);
        bnvh.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

}
