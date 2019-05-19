package com.example.myapplication.utility_classes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.myapplication.R;
import com.example.myapplication.models.User;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * File created by tcarau18
 **/
public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private GoogleSignInClient mGoogleSignInClient;


    private GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("353481374608-mg7rvo8h0kgjmkuts5dcmq65h2louus5.apps.googleusercontent.com")
            .requestEmail()
            .build();

    private String userUID;
    private Context mContext;
    private LoginManager mLoginManager ;

    // overloaded constructors for multiple use and cases
    public FirebaseMethods(FirebaseAuth auth, Context context) {
        mAuth = auth;
        mContext = context;
    }

    public FirebaseMethods(FirebaseAuth auth, FirebaseDatabase firebaseDatabase, GoogleSignInClient googleSignInClient, GoogleSignInOptions gso, Context context, LoginManager loginManager) {
        mAuth = auth;
        mFirebaseDatabase = firebaseDatabase;
        mGoogleSignInClient = googleSignInClient;
        this.gso = gso;
        mContext = context;
        mLoginManager = loginManager;
    }

    public  FirebaseMethods(Context context) {
        // Mo.Msaad modification modification
        synchronized (FirebaseMethods.class) {
            mAuth = FirebaseAuth.getInstance();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            myRef = mFirebaseDatabase.getReference();
            mContext = context;
            mLoginManager = LoginManager.getInstance();
            mGoogleSignInClient = GoogleSignIn.getClient(mContext, gso);

            if (mAuth.getCurrentUser() != null) {
                userUID = mAuth.getCurrentUser().getUid();
            }
        }
    }


    public synchronized void updateUsername(String username, String dispalyName, String website, String about, long phone, String profile_url) {
        Log.d(TAG, "updateUsername: updating username to:" + username);
        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userUID)
                .child(mContext.getString(R.string.field_username))
                .setValue(username);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userUID)
                .child(mContext.getString(R.string.field_display_name))
                .setValue(dispalyName);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userUID)
                .child("website")
                .setValue(website);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userUID)
                .child("about")
                .setValue(about);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userUID)
                .child("phone_number")
                .setValue(phone);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userUID)
                .child("profile_photo")
                .setValue(profile_url);
    }

    /**
     * Check if @param username already exists
     *
     * @param userName of the User from Firebase database
     */
    public void checkIfUsernameExists(final String userName, final String displayName,
                                      final String website, final String about, final long phone, final String profile_url) {
        Log.d(TAG, "checkIfUsernameExists: checking if " + userName + " already exists");

        DatabaseReference reference = mFirebaseDatabase.getReference("users");

        Query query = reference
                .orderByChild(reference.child(userUID).child(mContext.getString(R.string.field_username)).getKey())
                .equalTo(userName);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    //add username
                    updateUsername(userName, displayName, website, about, phone, profile_url);
                    Log.d(TAG, "onDataChange: saved username and displayname: " + userName + " " + displayName);

                } else {
                    Log.d(TAG, "onDataChange: Found a Match: " + dataSnapshot.getValue(User.class).getUsername());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /**
     * Retrieves the account settings for the User currently logged in
     * Database:user_account_settings node
     *
     * @param dataSnapshot represent the data from database
     * @return the User Account Settings
     */
    public
    User getUserSettings(DataSnapshot dataSnapshot) {
        Log.d(TAG, "getUserAccountSettings: retrieving user account settings from database");

        User user = new User();
        String userID = mAuth.getCurrentUser().getUid();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {

            //User Account Settings Node
            if (ds.getKey().equals(mContext.getString(R.string.dbname_users))) {
                Log.d(TAG, "getUserAccountSettings: dataSnapshot" + ds);
                try {
                    user.setUsername(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getUsername()
                    );

                    user.setDisplay_name(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getDisplay_name()
                    );

                    user.setAbout(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getAbout()
                    );

                    user.setWebsite(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getWebsite()
                    );

                    user.setFollowers(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getFollowers()
                    );

                    user.setFollowing(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getFollowing()
                    );

                    user.setNrPosts(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getNrPosts()
                    );

                    user.setProfile_photo(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getProfile_photo()
                    );

                    user.setEmail(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getEmail()
                    );

                    user.setPhone_number(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getPhone_number()
                    );

                    Log.d(TAG, "getUserAccountSettings: retrieve user account settings information: " + user.toString());
                } catch (NullPointerException e) {
                    Log.d(TAG, "getUserAccountSettings: NullPointerException: " + e.getMessage());
                }
            }
        }

        return user;
    }

    public String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Copenhagen"));

        return sdf.format(new Date());
    }

    public  void logOut(){
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        mGoogleSignInClient.signOut();
    }


    public  boolean checkUserStateIfNull() {

        Log.d(TAG, "checkUserStateIfNull: is called");
        if (mAuth == null || mAuth.getCurrentUser() == null) {

            return true;
        }
        else return false;

    }


}
