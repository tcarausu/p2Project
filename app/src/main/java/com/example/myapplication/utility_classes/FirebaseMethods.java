package com.example.myapplication.utility_classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.myapplication.Interfaces.TrafficLight;
import com.example.myapplication.R;
import com.example.myapplication.home.HomeActivity;
import com.example.myapplication.login.LoginActivity;
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
public class FirebaseMethods extends Activity implements TrafficLight, FirebaseAuth.AuthStateListener {

    private static final String TAG = "FirebaseMethods";
    private Context mContext;
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static FirebaseAuth.AuthStateListener mAuthListener = firebaseAuth -> {
    };
    private static FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private static FirebaseStorage sFirebaseStorage = FirebaseStorage.getInstance();
    private static DatabaseReference myRef = mFirebaseDatabase.getReference();
    private LoginManager mLoginManager = LoginManager.getInstance();

    private GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("353481374608-mg7rvo8h0kgjmkuts5dcmq65h2louus5.apps.googleusercontent.com")
            .requestEmail()
            .build();

    private GoogleSignInClient mGoogleSignInClient;

    public static FirebaseStorage getFirebaseStorage() {
        if (mAuth != null) {
            return sFirebaseStorage;
        }
        else return null;
    }

    public static FirebaseDatabase getmFirebaseDatabase() {
        if (mAuth!= null) {
            return mFirebaseDatabase;
        }
        else return null;
    }

    public LoginManager getLoginManager() {
        if (mAuth!= null) {
            return mLoginManager;
        }
        else return null;
    }

    public static FirebaseAuth getAuth() {
        if (mAuth != null) {
            return mAuth;
        }
        else return null;
    }


    private FirebaseMethods(Context context) {

        // Mo.Msaad modification modification
        synchronized (FirebaseMethods.class) {
            mContext = context;
            mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
        }
    }

    private FirebaseMethods(Context context, LoginManager loginManager, GoogleSignInOptions gso, GoogleSignInClient googleSignInClient) {

        synchronized (FirebaseMethods.class) {
            mContext = context;
            mLoginManager = loginManager;
            this.gso = gso;
            mGoogleSignInClient = googleSignInClient;
        }
    }

    public static FirebaseMethods getInstance(Context context) {
        synchronized (FirebaseMethods.class) {
            return new FirebaseMethods(context);
        }
    }

    //--------------------------------------------------------METHODS---------------------------------------------------------------------------------------------------
    public void updateUsername(String userUID, String username, String dispalyName, String website, String about, long phone, String profile_url) {
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

    @Override
    public void goToWhereverWithFlags(Context currentContext, Class<? extends AppCompatActivity> targetClass) {
        currentContext.startActivity(new Intent(currentContext, targetClass).addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    public boolean isAuthNull(FirebaseAuth auth, FirebaseUser user) {
        if (mAuth == null || mAuth.getCurrentUser() == null)
            return true;
        else return false;
    }

    @Override
    public void checkDatabase(DatabaseReference ref) {
        if (ref == null)
            ref.removeValue((databaseError, databaseReference) -> databaseReference.removeValue());
    }

    @Override
    public void addAuthLisntener() {
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void removeAuthLisntener() {
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public void autoDisctonnec(Context context) {
        if (isAuthNull(mAuth, mAuth.getCurrentUser())) {
            signOut();
            goToWhereverWithFlags(context, LoginActivity.class);
            overridePendingTransition(R.anim.left_enter, R.anim.left_out);
        }
        addAuthLisntener();
    }

    @Override
    public void loginChecker(Context context) {
        if (isAuthNull(mAuth, mAuth.getCurrentUser())) {
            signOut();
        } else goToWhereverWithFlags(context, HomeActivity.class);
    }

    @Override
    public void signOut() {
        {
            removeAuthLisntener();
            mAuth.signOut();
            mGoogleSignInClient.signOut();
            mLoginManager.logOut();
        }
    }


    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (isAuthNull(mAuth, mAuth.getCurrentUser())) {
            removeAuthLisntener();
        } else addAuthLisntener();
    }

}
