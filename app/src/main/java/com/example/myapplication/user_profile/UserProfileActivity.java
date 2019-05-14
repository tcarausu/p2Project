package com.example.myapplication.user_profile;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.myapplication.R;
import com.example.myapplication.models.Photo;
import com.example.myapplication.models.Post;
import com.google.firebase.auth.FirebaseAuth;

public class UserProfileActivity extends AppCompatActivity implements UserProfileFragment.OnGridImageSelectedListener {

    private static final String TAG = "UserProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        FirebaseAuth.getInstance();

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
    public void onGridImageSelected(Post post, int activityNr) {
        Log.d(TAG, "onGridImageSelected: selected an image gridView:" + post.toString());

        ViewPostFragment fragment = new ViewPostFragment();
        Bundle args = new Bundle();

        args.putParcelable(getString(R.string.post), post);
        args.putInt(getString(R.string.activity_number), activityNr);
        fragment.setArguments(args);

        FragmentTransaction transaction = UserProfileActivity.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);

        transaction.addToBackStack(getString(R.string.view_post_fragment));
        transaction.commit();

    }

}
