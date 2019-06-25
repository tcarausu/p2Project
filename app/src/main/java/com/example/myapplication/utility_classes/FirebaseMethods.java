package com.example.myapplication.utility_classes;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.myapplication.Interfaces.TrafficLight;
import com.example.myapplication.R;
import com.example.myapplication.home.HomeActivity;
import com.example.myapplication.login.LoginActivity;
import com.example.myapplication.models.Post;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * File created by tcarau18
 **/
public class FirebaseMethods implements TrafficLight, FirebaseAuth.AuthStateListener {

    private static final String TAG = "FirebaseMethods";
    private Context mContext;
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private static FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private static FirebaseStorage sFirebaseStorage = FirebaseStorage.getInstance();
    private static DatabaseReference myRef = mFirebaseDatabase.getReference();
    private LoginManager mLoginManager = LoginManager.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();

    private GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("353481374608-mg7rvo8h0kgjmkuts5dcmq65h2louus5.apps.googleusercontent.com")
            .requestEmail()
            .build();

    private GoogleSignInClient mGoogleSignInClient;

    public synchronized static FirebaseStorage getFirebaseStorage() {
        if (mAuth != null) {
            return sFirebaseStorage;
        } else return null;
    }

    public synchronized static FirebaseDatabase getmFirebaseDatabase() {
        if (mAuth != null) {
            return mFirebaseDatabase;
        } else return null;
    }

    public synchronized LoginManager getLoginManager() {
        if (mAuth != null) {
            return mLoginManager;
        } else return null;
    }

    public synchronized static FirebaseAuth getAuth() {
        if (mAuth != null) {
            return mAuth;
        } else return null;
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
    public synchronized User getUserSettings(DataSnapshot dataSnapshot) {
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

    public synchronized String getTimestamp() {

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
    public synchronized boolean isAuthNull(FirebaseAuth auth, FirebaseUser user) {
        if (mAuth == null || mAuth.getCurrentUser() == null)
            return true;
        else return false;
    }

    @Override
    public synchronized void checkDatabase(DatabaseReference ref) {
        if (ref == null)
            ref.removeValue((databaseError, databaseReference) -> databaseReference.removeValue());
    }

    @Override
    public synchronized void addAuthLisntener() {
        mAuth.addAuthStateListener(firebaseAuth ->
                firebaseAuth.getApp().addLifecycleEventListener((s, firebaseOptions) -> {
                    firebaseAuth.getCurrentUser();
                }));
    }

    @Override
    public synchronized void removeAuthLisntener() {
        mAuth.removeAuthStateListener(firebaseAuth ->
                firebaseAuth.getApp().removeLifecycleEventListener((s, firebaseOptions) -> {
                    signOut();
                    goToWhereverWithFlags(mContext, LoginActivity.class);
                }));

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public synchronized void autoDisconnect(Context context) {

        try {
            DatabaseReference postRef = myRef.child("posts").child(currentUser.getUid()).getRef();
            DatabaseReference userRef = myRef.child("users").child(currentUser.getUid()).getRef();

            if (isAuthNull(mAuth, mAuth.getCurrentUser())) {
                context.deleteSharedPreferences("fbPrefs");
                context.deleteSharedPreferences("ggPrefs");
                postRef.removeValue((databaseError, databaseReference) -> {
                    databaseReference.removeValue();
                    userRef.removeValue((databaseError1, databaseReference1) -> {
                        databaseReference1.removeValue();
                        removeAuthLisntener();
                        goToWhereverWithFlags(context, LoginActivity.class);
                        Log.d(TAG, "autoDisconnect: context" + context.getPackageName());
                    });
                });
            } else
                addAuthLisntener();

        } catch (NullPointerException e) {
            Log.e(TAG, "autoDisconnect: ", e);
        }
    }

    @Override
    public synchronized void loginChecker(Context context) {
        if (isAuthNull(mAuth, mAuth.getCurrentUser())) {
            signOut();
        } else goToWhereverWithFlags(context, HomeActivity.class);
    }


    @Override
    public synchronized void signOut() {
        {
            mAuth.signOut();
            mGoogleSignInClient.signOut();
            mLoginManager.logOut();
        }
    }


    @Override
    public synchronized void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

        if (isAuthNull(mAuth, mAuth.getCurrentUser())) {
            removeAuthLisntener();
            firebaseAuth.signOut();
        } else addAuthLisntener();
    }

    /**
     * The method gets the proper timestamp of the post creation and make the difference between
     * current time and the time created
     *
     * @param mPost : the current viewed post
     * @return the difference
     * @author Mo.Msaad
     */
    private static int timeInMinutes(Post mPost) {

        Log.d(TAG, "timeInMinutes: getting TimeStamp Difference");
        int timeInMinuteas;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd__HH:mm:ss", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Copenhagen"));
        Date today = c.getTime();
        sdf.format(today);
        Date timeStamp = null;
        final String photoTimeStamp = mPost.getDate_created();

        try {

            timeStamp = sdf.parse(photoTimeStamp);
            timeInMinuteas = (int) (today.getTime() - timeStamp.getTime()) / 1000 / 60;
            Log.d(TAG, "timeInMinutes: " + timeInMinuteas);
            Log.d(TAG, "timeInMinutes: " + mPost.getDate_created());

        } catch (ParseException e) {
            Log.e(TAG, "timeInMinutes: ParseException " + e.getMessage());
            timeInMinuteas = (int) (today.getTime() - timeStamp.getTime()) / 1000 / 60;
        }
        return timeInMinuteas;
    }

    private static int weeks(Post post) {
        int weeks = days(post) / 7;

        return weeks;
    }

    private static int days(Post post) {
        int days = hours(post) / 24;
        return days;
    }

    private static int hours(Post post) {
        int hours = timeInMinutes(post) / 60;
        return hours;
    }


    /**
     * This Method sets the returned time from timeInMinutes() to accordingly last time posted
     *
     * @param mPostTimeStamp : the current post time stamp text view
     * @param post           : current viewed post
     * @author Mo.Msaad
     **/
    public static void setTimeStampTodays(TextView mPostTimeStamp, Post post) {
        int minutes = timeInMinutes(post);
        int hours = hours(post);
        int days = days(post);
        int weeks = weeks(post);

        boolean sameDay = hours > 1 && hours < 24;

        if (hours <= 0) {
            if (minutes<= 0){
                mPostTimeStamp.setText(R.string.now);
            }
            else
            mPostTimeStamp.setText(String.format("%d minutes ago", minutes));

        } else if (hours == 1) {
                mPostTimeStamp.setText(R.string.hour_ago);

            } else if (sameDay)
                mPostTimeStamp.setText(String.format("%d hours ago", hours));

       else if (days > 0 && days < 7) {
            if (days == 1) {
                mPostTimeStamp.setText(R.string.yesterday);
            } else {
                mPostTimeStamp.setText(String.format("%d days and %d hours ago", days, hours % 24));
            }

        } else {
            if (days == 7) {
                mPostTimeStamp.setText(R.string.one_week_ago);

            } else if (days > 7 && days < 14) {
                mPostTimeStamp.setText(String.format("%d week and %d days ago", weeks, days % 7));

            } else if (days == 14) {
                mPostTimeStamp.setText(R.string.weeks_ago);
            } else
                mPostTimeStamp.setText(String.format("%d weeks and %d days ago", weeks, days % 7));
        }
//         if (days >= 7 && days < 14){
//             if (days == 7){
//                 mPostTimeStamp.setText(R.string.one_week_ago);
//             }
//             else if (days > 7){
//                 mPostTimeStamp.setText(String.format("%d week and %d days ago", days/7 ,days%7));
//             }
//         }
//         else {
//             mPostTimeStamp.setText(String.format("%d weeks and %d days ago", days/7 ,days%7));
//         }

//            if (timeInHours / 24 == 1) {
//                mPostTimeStamp.setText(String.format("%d Day ago", timeInHours / 24));
//
//            } else if (timeInHours/24 == 7)
//                mPostTimeStamp.setText(String.format("%d week ago", 1));
//            else if (timeInHours/24 > 7 ){
//                int weeks = timeInHours/24/7 ;
//                mPostTimeStamp.setText(String.format("%d week ago", 1));
//            }
//        }
        // TODO continue to weeks, months, years ....
    }


    public static class ChefoodAuth {
        private static FirebaseAuth myAuth = FirebaseAuth.getInstance();

        private ChefoodAuth(FirebaseAuth myAuth) {
            myAuth = myAuth;
        }

        public static ChefoodAuth getInstance() {
            synchronized (FirebaseMethods.ChefoodAuth.class) {
                return new ChefoodAuth(myAuth);
            }
        }
    }

    public static class ChefoodDatabase {
        private static FirebaseDatabase myDatabase = FirebaseDatabase.getInstance();

        private ChefoodDatabase(FirebaseDatabase myDatabase) {
            myDatabase = myDatabase;
        }

        public static ChefoodDatabase getInstance() {
            synchronized (FirebaseMethods.ChefoodDatabase.class) {
                return new ChefoodDatabase(myDatabase);
            }
        }
    }

    public static class ChefoodStorage {
        private static FirebaseStorage myStorage = FirebaseStorage.getInstance();

        private ChefoodStorage(FirebaseStorage myStorage) {
            myStorage = myStorage;

        }

        public static ChefoodStorage getInstance() {
            synchronized (FirebaseMethods.ChefoodStorage.class) {
                return new ChefoodStorage(myStorage);
            }
        }
    }
}
