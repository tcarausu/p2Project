package com.example.myapplication.welcomingSlides;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.R;

public class SliderAdapter extends PagerAdapter {
    private static final String TAG = "SliderAdapter";

    Context mContext ; //context
    LayoutInflater mLayoutInflater ; // to inflate our widgets

    public SliderAdapter(Context context) { // constructor of slidePager
        mContext = context;
    }
    //array of the images that we will display on the pages
    public int[] slideImages = {  R.drawable.profile, R.drawable.like_pressed, R.drawable.smartphone};

    //array of the titles that we will display on the pages
    public String[] slidesTitles = {"join us","share your experience\n" ,"like and follow"};

    //array of the texts that we will display on the pages
    public String[] slidesTexts = {"Sign up for FREE now",
            "Like, Share and Follow to grow your network and improve your cooking skills" ,
            "Stay updated with your friends kitchen life"};


    //for future use
    public String[] buttonText = {"next","next", "let's start"} ;


    @Override // we return the length
    public int getCount() {
        return slidesTitles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view ==  o ;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        // call for an inflater
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View v = mLayoutInflater.inflate(R.layout.slide1_layout,container,false);// create a view

        ImageView slideImage = v.findViewById(R.id.slideImage1_id); // find widgets by id
        TextView slideTitle = v.findViewById(R.id.slide1_title_id); // find widgets by id
        TextView slideText = v.findViewById(R.id.slide1_text_id); //

        slideImage.setImageResource(slideImages[position]); // setting each image according to position in the array
        slideTitle.setText(slidesTitles[position]);// same for title
        slideText.setText(slidesTexts[position]);// and texts

        container.addView(v); // then we feed the container with the view


        return v ; // we return the view
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((RelativeLayout)object);// this need to be implemented to avoid crushing the app


    }

}


