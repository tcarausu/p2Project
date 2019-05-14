package com.example.myapplication.welcomingSlides;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.login.LoginActivity;

/**
 * by M.MSAAD
 **/
public class SlidesActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private ViewPager slideViewPager;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private SliderAdapter sliderAdapter;
    private Button mPrevious, mNext, mSkip;
    private int mCurrentSlide;
    private Intent mIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_activity);
        mIntent = new Intent(SlidesActivity.this, LoginActivity.class);
        findWidgets();
        verifyFirstRun();

        sliderAdapter = new SliderAdapter(this);
        slideViewPager.setAdapter(sliderAdapter);
        addDots(0);
        slideViewPager.addOnPageChangeListener(vl);



        mPrevious.setOnClickListener(v -> slideViewPager.setCurrentItem(mCurrentSlide - 1));

        mNext.setOnClickListener(v -> {
            slideViewPager.setCurrentItem(mCurrentSlide + 1);
            if (mNext.getText().equals("FINISH")) {
                new Handler().postDelayed(() ->startActivity(mIntent), Toast.LENGTH_SHORT);
            }
        });

        mSkip.setOnClickListener(v -> new Handler().postDelayed(() ->startActivity(mIntent), 500));
    }
/**created by Mo.Msaad
 * */
    private void verifyFirstRun() {
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstRun = prefs.getBoolean("prefs", true);

        if (firstRun) {//if its the first run we change the boolean to false
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("prefs", false);
            editor.apply();
        } else {// then if boolean is false we skip the slides
            startActivity(mIntent);
            Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_LONG).show();
        }
    }

    private void findWidgets() {
        slideViewPager = findViewById(R.id.slides_viewPager);
        dotsLayout = findViewById(R.id.dots_layout);
        mPrevious = findViewById(R.id.previousButton_id);
        mNext = findViewById(R.id.nextButton_id);
        mSkip = findViewById(R.id.skipButton);

        sliderAdapter = new SliderAdapter(this);
        slideViewPager.setAdapter(sliderAdapter);
        addDots(0);
        slideViewPager.addOnPageChangeListener(vl);

        mIntent = new Intent(SlidesActivity.this, LoginActivity.class);

        mPrevious.setOnClickListener(this);

        mNext.setOnClickListener(this);

        mSkip.setOnClickListener(this);
    }


    @SuppressLint("ResourceAsColor")
    public void addDots(int position) {

        dots = new TextView[3];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(R.color.colorPrimaryDark);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.white));
        }
    }

    ViewPager.OnPageChangeListener vl = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(final int i) {
            addDots(i);
            mCurrentSlide = i;

            if (mCurrentSlide == 0) {

                mPrevious.setEnabled(false);
                mNext.setEnabled(true);
                mSkip.setEnabled(true);
                mPrevious.setVisibility(View.INVISIBLE);
                mSkip.setVisibility(View.VISIBLE);

            }
            else if (mCurrentSlide == dots.length - 1) {
                mNext.setEnabled(true);
                mPrevious.setEnabled(true);
                mSkip.setEnabled(true);
                mPrevious.setVisibility(View.VISIBLE);
                mNext.setText(getString(R.string.finish));
                mSkip.setVisibility(View.INVISIBLE);

            } else {
                mNext.setEnabled(true);
                mPrevious.setEnabled(true);
                mSkip.setEnabled(true);
                mPrevious.setVisibility(View.VISIBLE);
                mSkip.setVisibility(View.VISIBLE);
                mNext.setText(getString(R.string.next));
                mPrevious.setText(getString(R.string.back));
            }


        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.nextButton_id:
                slideViewPager.setCurrentItem(mCurrentSlide + 1);

                if (mNext.getText().equals("FINISH")) {

                    startActivity(mIntent);
                }
                break;

            case R.id.previousButton_id:
                slideViewPager.setCurrentItem(mCurrentSlide - 1);
                break;

            case R.id.skipButton:
                startActivity(mIntent);
                break;
        }
    }
}

