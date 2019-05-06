package com.example.myapplication.user_profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.utility_classes.ViewPostFragment;
import com.example.myapplication.models.Photo;
import com.google.firebase.auth.FirebaseAuth;

public class UserProfileActivity extends AppCompatActivity implements UserProfileFragment.OnGridImageSelectedListener {

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
        mAuth = FirebaseAuth.getInstance();
        mContext = getApplicationContext();

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

    @Override
    public void onGridImageSelected(Photo photo, int activityNr) {
        Log.d(TAG, "onGridImageSelected: selected an image gridView:" + photo.toString());

        ViewPostFragment fragment = new ViewPostFragment();
        Bundle args = new Bundle();

        args.putParcelable(getString(R.string.photo), photo);
        args.putInt(getString(R.string.activity_number), activityNr);
        fragment.setArguments(args);

        FragmentTransaction transaction = UserProfileActivity.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);

        transaction.addToBackStack(getString(R.string.view_post_fragment));
        transaction.commit();

    }
}
