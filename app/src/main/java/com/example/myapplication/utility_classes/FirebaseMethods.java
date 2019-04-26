package com.example.myapplication.utility_classes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.myapplication.R;
import com.example.myapplication.models.User;
import com.example.myapplication.models.UserAccountSettings;
import com.example.myapplication.models.UserSettings;
import com.example.myapplication.user_profile.UserProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * File created by tcarau18
 **/
public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private String userUID;

    private Context mContext;

    public FirebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mContext = context;

        if (mAuth.getCurrentUser() != null) {
            userUID = mAuth.getCurrentUser().getUid();
        }
    }

    public boolean checkIfUserExists(String username, DataSnapshot dataSnapshot) {
        Log.d(TAG, "checkIfUserExists: checking if " + username + " already exists");

        User user = new User();
        for (DataSnapshot ds : dataSnapshot.child(userUID).getChildren()) {
            Log.d(TAG, "checkIfUserNameExists: dataSnapshot: " + ds);

            user.setUsername(ds.getValue(User.class).getUsername());
            Log.d(TAG, "checkIfUserExists: username: " + user.getUsername());

            if (StringManipulation.expandUserName(user.getUsername()).equals(username)) {
                Log.d(TAG, "checkIfUserExists: Found Match" + user.getUsername());

                return true;
            }

        }

        return false;
    }

    public void addNewUser(String email, String username, String description, String website, String profile_photo) {

        User user = new User(email, 1, email, StringManipulation.condenseUserName(username));

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userUID)
                .setValue(user);

        UserAccountSettings settings = new UserAccountSettings(
                description,
                username,
                username,
                0,
                0,
                0,
                profile_photo,
                website);

        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userUID)
                .setValue(settings);
    }

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");

        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: signed in" + user.getUid());
                } else Log.d(TAG, "onAuthStateChanged: signed out");
            }
        };
    }

    public void updateUsername(String username) {
        Log.d(TAG, "updateUsername: updating username to:"+username);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userUID)
                .child(mContext.getString(R.string.field_username))
                .setValue(username);

        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userUID)
                .child(mContext.getString(R.string.field_username))
                .setValue(username);
    }


//    static/lambda/((UserProfileActivity.class).getctvoty)

}
