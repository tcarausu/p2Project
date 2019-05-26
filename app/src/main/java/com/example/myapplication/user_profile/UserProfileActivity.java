package com.example.myapplication.user_profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.myapplication.R;
import com.example.myapplication.models.Post;
import com.example.myapplication.utility_classes.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;

public class UserProfileActivity extends AppCompatActivity implements UserProfileFragment.OnGridImageSelectedListener {

    private static final String TAG = "UserProfileActivity";
    private FirebaseMethods mFirebaseMethods ;
    private FirebaseAuth mAuth ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mFirebaseMethods = FirebaseMethods.getInstance(getApplicationContext());
        mAuth = FirebaseMethods.getAuth();
        mFirebaseMethods.checkUserStateIfNull(getApplicationContext(),mAuth);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseMethods.checkUserStateIfNull(getApplicationContext(),mAuth);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void init() {
        Log.d(TAG, "init: inflating" + getString(R.string.profile_fragment));

        UserProfileFragment fragment = new UserProfileFragment();
        FragmentTransaction transaction = UserProfileActivity.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);

        transaction.addToBackStack(getString(R.string.profile_fragment));
        transaction.commit();

    }

    /**
     * Here we setup the View Post News Fragment, which later utilizes it to display the data,
     * providing with options such as deleting the post or reporting.
     *
     * @param post We have to Post which the User Profile Fragment and later, View Post does utilize.
     * @param activityNr the Activity Number which is displaying the post.
     */
    @Override
    public void onGridImageSelected(Post post, int activityNr) {
        Log.d(TAG, "onGridImageSelected: selected an image gridView:" + post.toString());

        ViewPostFragmentNewsFeed fragment = new ViewPostFragmentNewsFeed();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.post), post);
        args.putInt(getString(R.string.activity_number), activityNr);
        fragment.setArguments(args);
        FragmentTransaction transaction = UserProfileActivity.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(getString(R.string.view_post_fragment));
        transaction.commit();

    }


    public void gotos(Context context, Class<? extends AppCompatActivity> cl){
        startActivity(new Intent(context,cl)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));

    }

}
