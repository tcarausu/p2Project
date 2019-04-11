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
 *   by M.MSAAD
 **/
public class SlidesActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ViewPager slideViewPager;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private SliderAdapter sliderAdapter;
    private Button mpreviouis,mNexxt ,mSkip ;
    private int mCurrentSlide  ;
    private Intent mIntent ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_activity);

        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        Boolean firstRun = prefs.getBoolean("prefs",true);

        if (firstRun ) {//if its the first run we change the boolean to false
            SharedPreferences.Editor editor = prefs.edit() ;
            editor.putBoolean("prefs",false);
            editor.apply();
        }

        else {// then if boolean is false we skip the slides
            startActivity(new Intent(SlidesActivity.this, LoginActivity.class));
            Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_LONG)
                    .show();
        }

        slideViewPager = findViewById(R.id.slide1_viewPager);
        dotsLayout = findViewById(R.id.dots_layout);
        mpreviouis = findViewById(R.id.previousButton_id);
        mNexxt = findViewById(R.id.nextButton_id);
        mSkip = findViewById(R.id.skipButton);

        sliderAdapter = new SliderAdapter(this);
        slideViewPager.setAdapter(sliderAdapter);
        addDots(0);
        slideViewPager.addOnPageChangeListener(vl);

        mIntent  = new Intent(SlidesActivity.this,LoginActivity.class);

        mpreviouis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideViewPager.setCurrentItem(mCurrentSlide+1);
            }
        });

        mNexxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideViewPager.setCurrentItem(mCurrentSlide+1);

                if (mNexxt.getText().equals("FINISH")){
                    startActivity(mIntent);
                }

            }
        });

        mSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(mIntent);
            }
        });
    }






    @SuppressLint("ResourceAsColor")
    public void addDots(int position){

        dots = new TextView[3] ;
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length ; i ++){

            dots[i] = new TextView(this) ;
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(R.color.colorPrimaryDark);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0){
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

                mpreviouis.setEnabled(false);
                mNexxt.setEnabled(true);
                mSkip.setEnabled(true);
                mpreviouis.setVisibility(View.INVISIBLE);
                mSkip.setVisibility(View.VISIBLE);

            } else if (mCurrentSlide == dots.length - 1) {
                mNexxt.setEnabled(true);
                mpreviouis.setEnabled(true);
                mSkip.setEnabled(true);
                mpreviouis.setVisibility(View.VISIBLE);
                mNexxt.setText("FINISH");
                mSkip.setVisibility(View.INVISIBLE);

            } else {
                mNexxt.setEnabled(true);
                mpreviouis.setEnabled(true);
                mSkip.setEnabled(true);
                mpreviouis.setVisibility(View.VISIBLE);
                mSkip.setVisibility(View.VISIBLE);
                mNexxt.setText("Next");
                mpreviouis.setText("Back");
            }


        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };




}

