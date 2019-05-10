package com.example.myapplication.utility_classes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.myapplication.R;
import com.example.myapplication.models.User;
import com.example.myapplication.models.UserSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
//
//    private boolean checkIfUserExists(String username, DataSnapshot dataSnapshot) {
//        Log.d(TAG, "checkIfUserExists: checking if " + username + " already exists");
//
//        User user = new User();
//        for (DataSnapshot ds : dataSnapshot.child(userUID).getChildren()) {
//            Log.d(TAG, "checkIfUserNameExists: dataSnapshot: " + ds);
//
//            user.setUsername(ds.getValue(User.class).getUsername());
//            Log.d(TAG, "checkIfUserExists: username: " + user.getUsername());
//
//            if (StringManipulation.expandUserName(user.getUsername()).equals(username)) {
//                Log.d(TAG, "checkIfUserExists: Found Match" + user.getUsername());
//
//                return true;
//            }
//
//        }
//
//        return false;
//    }

    private void setupFirebaseAuth() {

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user != null) {
                Log.d(TAG, "onAuthStateChanged: signed in" + user.getUid());
            } else Log.d(TAG, "onAuthStateChanged: signed out");
        };
    }


    public void updateUsername(String username, String dispalyName, String website, String about, long phone, String profile_url) {
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
                    updateUsername(userName, displayName,website,about,phone,profile_url );
//                    Toast.makeText(mContext, "saved username and displayname", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onDataChange: saved username and displayname: "+userName+" "+displayName);

                } else {
                    Log.d(TAG, "onDataChange: Found a Match: " + dataSnapshot.getValue(User.class).getUsername());
//                    Toast.makeText(mContext, "That username already exists", Toast.LENGTH_SHORT).show();
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
     * @param dataSnapshot represent the data from database
     * @return the User Account Settings
     */
    public UserSettings getUserSettings(DataSnapshot dataSnapshot) {
        Log.d(TAG, "getUserAccountSettings: retrieving user account settings from database");

        User settings = new User();
        String userID = mAuth.getCurrentUser().getUid();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {

            //User Account Settings Node
            if (ds.getKey().equals(mContext.getString(R.string.dbname_users))) {
                Log.d(TAG, "getUserAccountSettings: dataSnapshot" + ds);
                try {
                    settings.setUsername(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getUsername()
                    );

                    settings.setDisplay_name(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getDisplay_name()
                    );

                    settings.setAbout(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getAbout()
                    );

                    settings.setWebsite(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getWebsite()
                    );

                    settings.setFollowers(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getFollowers()
                    );

                    settings.setFollowing(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getFollowing()
                    );

                    settings.setPosts(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getPosts()
                    );

                    settings.setProfile_photo(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getProfile_photo()
                    );

                    settings.setEmail(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getEmail()
                    );

                    settings.setPhone_number(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getPhone_number()
                    );

                    Log.d(TAG, "getUserAccountSettings: retrieve user account settings information: " + settings.toString());
                } catch (NullPointerException e) {
                    Log.d(TAG, "getUserAccountSettings: NullPointerException: " + e.getMessage());
                }
            }
        }

        return new UserSettings(settings);
    }

//    private String getTimestamp() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
//        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Copenhagen"));
//
//        return sdf.format(new Date());
//    }

//    public void addPhotoToDatabase(String caption, String url) {
//        Log.d(TAG, "addPhotoToDatabase: adding photo to database");
//
//        String userUID = mAuth.getCurrentUser().getUid();
//
//        String tags = StringManipulation.getTags(caption);
//        String newPhotoKey = myRef.child(mContext.getString(R.string.dbname_photos)).push().getKey();
//        Photo photo = new Photo();
//        photo.setCaption(caption);
//        photo.setDate_created(getTimestamp());
//        photo.setImage_path(url);
//        photo.setTags(tags);
//        photo.setUser_id(userUID);
//        photo.setPhoto_id(newPhotoKey);
//
//        //insert into database
//        myRef.child(mContext.getString(R.string.dbname_user_photos)).child(userUID).child(newPhotoKey).setValue(photo);
//        myRef.child(mContext.getString(R.string.dbname_photos)).child(newPhotoKey).setValue(photo);
//
//    }

}
