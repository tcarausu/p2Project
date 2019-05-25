package com.example.myapplication.utility_classes;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.myapplication.R;
import com.example.myapplication.home.HomeActivity;
import com.example.myapplication.models.User;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * File created by tcarau18
 **/
public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";

    private static FirebaseAuth mAuth;
    private static FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private static FirebaseStorage sFirebaseStorage = FirebaseStorage.getInstance();
    private static DatabaseReference myRef = mFirebaseDatabase.getReference();
    private GoogleSignInClient mGoogleSignInClient;


    private GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("353481374608-mg7rvo8h0kgjmkuts5dcmq65h2louus5.apps.googleusercontent.com")
            .requestEmail()
            .build();

    public static FirebaseStorage getFirebaseStorage() {
        return sFirebaseStorage;
    }

    public static FirebaseDatabase getmFirebaseDatabase() {
        return mFirebaseDatabase;
    }

    private String userUID;
    private Context mContext;
    private LoginManager mLoginManager = LoginManager.getInstance();

    public static FirebaseAuth getAuth() {
        return mAuth;
    }

    private FirebaseMethods(Context context) {
        // Mo.Msaad modification modification
        synchronized (FirebaseMethods.class) {
            mAuth = FirebaseAuth.getInstance();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            myRef = mFirebaseDatabase.getReference();
            mContext = context;
            mLoginManager = LoginManager.getInstance();
            mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

        }
    }

    public static FirebaseMethods getInstance(Context context) {

        return new FirebaseMethods(context);
    }


    //--------------------------------------------------------METHODS---------------------------------------------------------------------------------------------------
    public void updateUsername(String userUID,String username, String dispalyName, String website, String about, long phone, String profile_url) {
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
     * Retrieves the account settings for the User currently logged in
     * Database:user_account_settings node
     *
     * @param dataSnapshot represent the data from database
     * @return the User Account Settings
     */
    public User getUserSettings(DataSnapshot dataSnapshot) {
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
                                    .getNrOfPosts()
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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd__HH:mm:ss", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Copenhagen"));
        return sdf.format(new Date());
    }


    public void checkUserStateIfNull(Context context, FirebaseAuth auth, FirebaseUser user) {

        Log.d(TAG, "checkUserStateIfNull: is called");
        if (auth == null || user == null) {
            auth.signOut();
        }
    }

    public void checkAuth(Context context ,FirebaseAuth auth){
        if (auth != null || mAuth.getCurrentUser() == null ){
            context.startActivity(new Intent(context, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }
    }



}
