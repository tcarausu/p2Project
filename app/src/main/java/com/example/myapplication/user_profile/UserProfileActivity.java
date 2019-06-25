package com.example.myapplication.user_profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.models.Post;
import com.example.myapplication.models.User;
import com.example.myapplication.utility_classes.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity implements UserProfileFragment.OnGridImageSelectedListener {

    private static final String TAG = "UserProfileActivity";
    private FirebaseMethods mFirebaseMethods;
    private FirebaseAuth mAuth;
    private DatabaseReference current_userRef;
    private FirebaseDatabase firebasedatabase;
    private FirebaseUser current_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mFirebaseMethods = FirebaseMethods.getInstance(getApplicationContext());
        mAuth = FirebaseMethods.getAuth();
        mFirebaseMethods.autoDisconnect(getApplicationContext());
        firebasedatabase = FirebaseMethods.getmFirebaseDatabase();
        current_user = mAuth.getCurrentUser();
        checkUserDetails();
        init();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseMethods.autoDisconnect(getApplicationContext());
    }


    private void checkUserDetails() {
        try {

            current_userRef = firebasedatabase.getReference("users").child(current_user.getUid()).getRef();
            current_userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    User user = dataSnapshot.getValue(User.class);
                    if (user.getDisplay_name().equals("Chose a user name")) {

                        EditProfileFragment fragment = new EditProfileFragment();
                        FragmentTransaction transaction = UserProfileActivity.this.getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container, fragment);
                        transaction.addToBackStack(getString(R.string.edit_profile_fragment));
                        transaction.commit();
                        Toast.makeText(getApplicationContext(), "Please chose a user name to identify yourself", Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error finding this user on the database. please login in again", Toast.LENGTH_SHORT).show();
        }
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
     * @param post       We have to Post which the User Profile Fragment and later, View Post does utilize.
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
        transaction.setCustomAnimations(R.anim.fade_out,R.anim.fade_in);
        transaction.commit();

    }
}
