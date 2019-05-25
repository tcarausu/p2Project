package com.example.myapplication.post;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.utility_classes.SectionsPagerAdapter;

/**
 * File created by tcarau18
 **/
public class AddPostActivity extends AppCompatActivity {
    private static final String TAG = "AddPostActivity";
    private static final int ACTIVITY_NUM = 2;
    private static final int PERMISSION_REQUEST = 1;
//    private SelectPictureFragment selectPictureFragment ;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        setupViewPager();

    }

    private void setupViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SelectPictureFragment());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(adapter);

    }

}
