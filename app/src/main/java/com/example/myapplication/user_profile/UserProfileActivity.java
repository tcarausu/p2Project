package com.example.myapplication.user_profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.login.LoginActivity;
import com.example.myapplication.utility_classes.BaseActivity;
import com.example.myapplication.utility_classes.BottomNavigationViewHelper;
import com.example.myapplication.utility_classes.GridImageAdapter;
import com.example.myapplication.utility_classes.UniversalImageLoader;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class UserProfileActivity extends AppCompatActivity
        implements View.OnClickListener {

    private static final String TAG = "UserProfileActivity";

    private static final int ACTIVITY_NUM = 4;
    private static final int NUM_GRID_COLUMNS = 3;

    private Context mContext;

    private FirebaseAuth mAuth;

    private TextView displayUserName, editProfile;
    private String userUID;

    private ProgressBar mProgressBar;
    private ImageView mProfilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//        setupActivityWidgets();
//        initLayout();
//        setListeners();
//        setupBottomNavigationView();
//        setupToolbar();
//        tempGridSetup();

        init();
    }

    private void init() {
        Log.d(TAG, "init: inflating" + getString(R.string.profile_fragment));

        UserProfileFragment fragment = new UserProfileFragment();
        FragmentTransaction transaction = UserProfileActivity.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);

        transaction.addToBackStack(getString(R.string.profile_fragment));
        transaction.commit();

    }

    //    private void initLayout() {
//        mContext = UserProfileActivity.this;
//
//        displayUserName = findViewById(R.id.displayUserName);
//
//        findViewById(R.id.profileMenu);
//
//        mAuth = FirebaseAuth.getInstance();
//
//        setProfileImage(mProfilePhoto);
//
//        Intent getLoginIntent = getIntent();
//
//        userUID = getLoginIntent.getStringExtra("userUid");
//    }
//
//    private void setListeners() {
//        editProfile = findViewById(R.id.editProfile);
//        editProfile.setOnClickListener(this);
//
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }
//
//
//    private void updateUI(FirebaseUser user) {
//        if (user != null) {
//            displayUserName.setText(getString(R.string.user_status_fmt, user.getDisplayName()));
//        } else {
//            displayUserName.setText(userUID);
//        }
//    }
//
//    private void setupToolbar() {
//        Toolbar toolbar = findViewById(R.id.profileToolBar);
//        setActionBar(toolbar);
//    }
//
//    private void setProfileImage(ImageView mProfilePhoto) {
//        Log.d(TAG, "setProfileImage: setting image");
//        String imgURL = "http://stacktips.com/wp-content/uploads/2014/05/UniversalImageLoader-620x405.png";
//        UniversalImageLoader.setImage(imgURL, mProfilePhoto, null, "");
//
//    }
//
//    private void tempGridSetup() {
//        ArrayList<String> imgURLs = new ArrayList<>();
//
//        imgURLs.add("http://bit.ly/2Uk02ak");
//        imgURLs.add("http://bit.ly/2Uk02ak");
//        imgURLs.add("http://bit.ly/2FM5VVJ");
//
//        imgURLs.add("http://bit.ly/2FM5VVJ");
//        imgURLs.add("http://bit.ly/2V8NRdm");
//        imgURLs.add("http://bit.ly/2Uk02ak");
//
//        imgURLs.add("http://bit.ly/2V8NRdm");
//        imgURLs.add("http://bit.ly/2V8NRdm");
//        imgURLs.add("http://bit.ly/2FM5VVJ");
//
//        setupImageGridView(imgURLs);
//
//    }
//
//    private void setupImageGridView(ArrayList<String> imgURLs) {
//        GridView gridView = findViewById(R.id.grid_view_user_profile);
//
//        int gridWidth = getResources().getDisplayMetrics().widthPixels;
//        int imageWidth = gridWidth / NUM_GRID_COLUMNS;
//        gridView.setColumnWidth(imageWidth);
//
//        GridImageAdapter adapter = new GridImageAdapter(mContext, R.layout.layout_grid_imageview, "", imgURLs);
//        gridView.setAdapter(adapter);
//    }
//
//    private void setupActivityWidgets() {
//        mProgressBar = findViewById(R.id.profile_progress_bar);
//        mProgressBar.setVisibility(View.GONE);
//        mProfilePhoto = findViewById(R.id.profileImage);
//    }
//
    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//
//            case R.id.editProfile:
//                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
//                intent.putExtra(getString(R.string.calling_activity), getString(R.string.profile_activity));
//                startActivity(intent);
//
//                break;
//            case R.id.profileMenu:
//                Log.d(TAG, "onClick: navigating to account settings");
//
//                startActivity(new Intent(mContext, AccountSettingsActivity.class));
//
//                break;
//        }
//
    }
//
//    /**
//     * Bottom Navigation View setup
//     */
//    public void setupBottomNavigationView() {
//        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigationBar);
//        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
//        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
//        Menu menu = bottomNavigationViewEx.getMenu();
//        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
//        menuItem.setChecked(true);
//
//    }

}
