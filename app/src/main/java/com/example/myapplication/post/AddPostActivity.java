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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        setupViewPager();

    }

    private void setupViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SelectPictureFragment());

        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(adapter);

    }

}
