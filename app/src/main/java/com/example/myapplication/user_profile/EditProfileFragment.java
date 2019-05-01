package com.example.myapplication.user_profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.models.User;
import com.example.myapplication.models.UserAccountSettings;
import com.example.myapplication.models.UserSettings;
import com.example.myapplication.utility_classes.FirebaseMethods;
import com.example.myapplication.utility_classes.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * File created by tcarau18
 **/
public class EditProfileFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "EditProfileFragment";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;

    //Edit Profile widgets
    private TextView mChangeProfilePhoto;
    private EditText mDisplayName, mUserName, mWebsite, mAbout, mEmail, mPhoneNumber;
    private CircleImageView mProfilePhoto;
    private ImageView backArrow, saveChanges;

    private FirebaseMethods firebaseMethods;
    private Context mContext;
    private UserSettings mUserSettings;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        mAuth = FirebaseAuth.getInstance();

        initLayouts(view);
        setupFirebaseAuth();

//        setProfileImage();

        return view;
    }

    public void initLayouts(View view) {
        mContext = getActivity();
        firebaseMethods = new FirebaseMethods(mContext);

        mDisplayName = view.findViewById(R.id.displayName);
        mUserName = view.findViewById(R.id.username);
        mWebsite = view.findViewById(R.id.website);
        mAbout = view.findViewById(R.id.about);
        mEmail = view.findViewById(R.id.email);
        mPhoneNumber = view.findViewById(R.id.phoneNumber);

        mProfilePhoto = view.findViewById(R.id.profile_photo);
        backArrow = view.findViewById(R.id.backArrow);
        saveChanges = view.findViewById(R.id.save_changes);

        backArrow.setOnClickListener(this);
        saveChanges.setOnClickListener(this);
    }

    /**
     * Retrieves data from the widgets and submits it to database
     * Before doing so it checks if the username is unique
     */
    private void saveProfileSettings() {
        final String userName = mUserName.getText().toString();
        final String email = mEmail.getText().toString();
        final String displayName = mDisplayName.getText().toString();
        final String phoneNumber = mPhoneNumber.getText().toString();
        final String description = mAbout.getText().toString();
        final String website = mWebsite.getText().toString();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = new User();
//                for (DataSnapshot ds : dataSnapshot.child(getString(R.string.dbname_users)).getChildren()) {
//                    if (ds.getKey().equals(userID)) {
//                        user.setUsername(ds.getValue(User.class).getUsername());
//
//                    }
//                }
//
//                Log.d(TAG, "onDataChange: Current Username: " + user.getUsername());

                if (!mUserSettings.getSettings().getUsername().equals(userName)) {
                    firebaseMethods.checkIfUsernameExists(userName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setProfileWidgets(UserSettings userSettings) {
        Log.d(TAG, "setProfileWidgets: setting widgets with data, retrieving from database: " +
                userSettings.toString());
        User user = userSettings.getUser();
        UserAccountSettings settings = userSettings.getSettings();
        mUserSettings = userSettings;

//        UniversalImageLoader.setImage(settings.getProfile_photo(), mProfilePhoto, null, "");

        mDisplayName.setText(settings.getDisplay_name());
        mUserName.setText(settings.getUsername());
        mWebsite.setText(settings.getWebsite());
        mAbout.setText(settings.getAbout());
        mEmail.setText(String.valueOf(settings.getEmail()));
        mPhoneNumber.setText(String.valueOf(settings.getPhone_number()));

    }

    private void setProfileImage() {
        Log.d(TAG, "setProfileImage: setting image");
        String imgURL = "http://stacktips.com/wp-content/uploads/2014/05/UniversalImageLoader-620x405.png";
        UniversalImageLoader.setImage(imgURL, mProfilePhoto, null, "");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.backArrow:
                Log.d(TAG, "onClick:  go to profile activity");
                Objects.requireNonNull(getActivity()).finish();
                break;

            case R.id.save_changes:
                Log.d(TAG, "onClick:  attempting to save changes");
//                Objects.requireNonNull(getActivity()).finish();
                saveProfileSettings();

                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        userID = mAuth.getCurrentUser().getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: signed in" + user.getUid());
                } else Log.d(TAG, "onAuthStateChanged: signed out");
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // retrive user information from the database
//                (firebaseMethods.getUserSettings(dataSnapshot));
                setProfileWidgets(firebaseMethods.getUserSettings(dataSnapshot));

                //retrive images for the user in question

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
