package com.example.myapplication.utility_classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.myapplication.R;
import com.example.myapplication.history_log.HistoryLogActivity;
import com.example.myapplication.home.HomeActivity;
import com.example.myapplication.postActivity.AddPostActivity;
import com.example.myapplication.search.SearchActivity;
import com.example.myapplication.user_profile.UserProfileActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * File created by tcarau18
 * <p>
 * Modified by Mo.Msaad
 * added constructor to enable inheritance. extended activity to inherit overridePendingTransition() from super class Activity
 * removed static from methods, bc no need to do so and as well i cant call overridePendingTransition inside the switch.
 **/
public class BottomNavigationViewHelper extends Activity {

    private static final String TAG = "BottomNavigationViewHel";
    private Context mContext;
    private Intent trafficIntent;

    public BottomNavigationViewHelper(Context context) {
        super();
        mContext = context;
    }

    /**
     * Bottom Navigation View settup
     */
    public void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx) {
        Log.d(TAG, "setupBottomNavigationView: Setting up  BottomNavigationViewEx ");
        bottomNavigationViewEx.enableShiftingMode(0, false);
        bottomNavigationViewEx.setItemHorizontalTranslationEnabled(false);
        bottomNavigationViewEx.setTextVisibility(false);
        bottomNavigationViewEx.enableAnimation(false);
    }

    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {}

    public void enableNavigation(final Context context, BottomNavigationViewEx viewEx) {
        viewEx.setOnNavigationItemSelectedListener(menuItem -> {

            switch (menuItem.getItemId()) {

                case R.id.ic_home:
                    trafficIntent = new Intent(context, HomeActivity.class); //ACTIVITY_NUM = 0
                    context.startActivity(trafficIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                    break;

                case R.id.ic_search:
                    trafficIntent = new Intent(context, SearchActivity.class);//ACTIVITY_NUM = 1
                    context.startActivity(trafficIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                    break;

                case R.id.ic_add_post:
                    trafficIntent = new Intent(context, AddPostActivity.class);//ACTIVITY_NUM = 2
                    context.startActivity(trafficIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                    break;

                case R.id.ic_like_posts:
                    trafficIntent = new Intent(context, HistoryLogActivity.class);//ACTIVITY_NUM = 3
                    context.startActivity(trafficIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                    break;

                case R.id.ic_user_profile:
                    trafficIntent = new Intent(context, UserProfileActivity.class);//ACTIVITY_NUM = 4
                    context.startActivity(trafficIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                    break;
            }
            return false;
        });

    }


}