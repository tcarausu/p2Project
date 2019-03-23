package com.example.myapplication.utility_classes;

import android.util.Log;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * File created by tcarau18
 **/
public class BottomNavigationViewHelper {

    private static final String TAG = "BottomNavigationViewHel";


    /**
     * Bottom Navigation View settup
     */
    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx) {
        Log.d(TAG, "setupBottomNavigationView: Setting up  BottomNavigationViewEx ");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableShiftingMode(0,false);
        bottomNavigationViewEx.setItemHorizontalTranslationEnabled(false);
        bottomNavigationViewEx.setTextVisibility(false);

    }
}
