package com.example.myapplication.utility_classes;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.example.myapplication.R;
import com.example.myapplication.home.HomeActivity;
import com.example.myapplication.likes.LikePostActivity;
import com.example.myapplication.post.AddPostActivity;
import com.example.myapplication.search.SearchActivity;
import com.example.myapplication.user_profile.UserProfileUI;
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
        bottomNavigationViewEx.enableShiftingMode(0, false);
        bottomNavigationViewEx.setItemHorizontalTranslationEnabled(false);
        bottomNavigationViewEx.setTextVisibility(false);

    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx viewEx) {
        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.ic_home:
                        Intent homeIntent = new Intent(context, HomeActivity.class); //ACTIVITY_NUM = 0
                        context.startActivity(homeIntent);
                        break;
                    case R.id.ic_search:
                        Intent searchIntent = new Intent(context, SearchActivity.class);//ACTIVITY_NUM = 1
                        context.startActivity(searchIntent);
                        break;

                    case R.id.ic_add_post:
                        Intent addPostIntent = new Intent(context, AddPostActivity.class);//ACTIVITY_NUM = 2
                        context.startActivity(addPostIntent);
                        break;
                    case R.id.ic_like_posts:
                        Intent likePost = new Intent(context, LikePostActivity.class);//ACTIVITY_NUM = 3
                        context.startActivity(likePost);
                        break;
                    case R.id.ic_user_profile:
                        Intent userProfileIntent = new Intent(context, UserProfileUI.class);//ACTIVITY_NUM = 4
                        context.startActivity(userProfileIntent);
                        break;
                }
                return false;
            }
        });
    }
}
