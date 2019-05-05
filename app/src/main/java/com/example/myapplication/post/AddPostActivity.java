package com.example.myapplication.post;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myapplication.R;
import com.example.myapplication.utility_classes.BottomNavigationViewHelper;
import com.example.myapplication.utility_classes.Permissions;
import com.example.myapplication.utility_classes.SectionsPagerAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * File created by tcarau18
 **/
public class AddPostActivity extends AppCompatActivity {
    private static final String TAG = "AddPostActivity";
    private static final int ACTIVITY_NUM = 2;
    private static final int PERMISSION_REQUEST = 1;

    private ViewPager mViewPager;

    private Context mContext = AddPostActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        initLayout();
        buttonListeners();
        //setupBottomNavigationView();

        if (checkPermissionsArray(Permissions.PERMISSIONS)) {
            setupViewPager();
        } else {
            verifyPermissions(Permissions.PERMISSIONS);
        }


    }

    private void setupViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GalleryFragment());
        adapter.addFragment(new PhotoFragment());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabsBottom);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setText(getString(R.string.gallery));
        tabLayout.getTabAt(1).setText(getString(R.string.photo_add));

    }

    /**
     * Used to get tabNumber in fragments
     * @return
     */
    public int getTabNumber() {
        return mViewPager.getCurrentItem();
    }

    /**
     * Checking all permissions
     * @param permissions
     * @return
     */
    public Boolean checkPermissionsArray(String[] permissions) {
        for (int i = 0; i < permissions.length; i++) {
            String check = permissions[i];
            if (!checkPermissions(check)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checking single permission
     *
     * @param permission
     * @return
     */
    public Boolean checkPermissions(String permission) {
        int permissionRequest = ActivityCompat.checkSelfPermission(AddPostActivity.this, permission);

        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    public void verifyPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(
                AddPostActivity.this,
                permissions,
                PERMISSION_REQUEST
        );
    }


    public void initLayout() {

    }

    public void buttonListeners() {

    }

    /**
     * Bottom Navigation View setup
     */

    /*
    public void setupBottomNavigationView() {
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigationBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }*/
}
