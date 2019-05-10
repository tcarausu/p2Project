package com.example.myapplication.user_profile;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.models.User;
import com.example.myapplication.models.UserSettings;
import com.example.myapplication.utility_classes.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * File created by tcarau18
 **/
public class EditProfileFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "EditProfileFragment";
    private static final int REQUEST_CAMERA = 11;
    private static final int REQUEST_IMAGE_GALLERY = 22;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;

    //Edit Profile widgets
    private TextView mChangeProfilePhoto;
    private EditText mDisplayName, mUserName, mWebsite, mAbout, mPhoneNumber;
    private TextView mEmail;
    private CircleImageView mProfilePhoto;
    private ImageView backArrow, saveChanges;

    private FirebaseMethods firebaseMethods;
    private FirebaseUser currentUser;
    private Context mContext;
    private UserSettings mUserSettings;
    private Uri uri, avatarUri;
    private StorageReference profilePicStorage;
    private FirebaseStorage storage;
    private FirebaseDatabase database;


    private Bitmap bitmap;
    private String prof_pic_URL;
    private int batteryLevel;

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setBatteryLevel(intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -33));
        }
    };


    public String getProf_pic_URL() {
        return prof_pic_URL;
    }

    public void setProf_pic_URL(String prof_pic_URL) {
        this.prof_pic_URL = prof_pic_URL;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        storage = FirebaseStorage.getInstance();
        profilePicStorage = storage.getReference();
        userID = mAuth.getCurrentUser().getUid();
        avatarUri = Uri.parse("android.resource://com.example.myapplication/drawable/my_avatar");
        initLayouts(view);
        setupFirebaseAuth();


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
        mProfilePhoto.setOnClickListener(this);


    }

    /**
     * Retrieves data from the widgets and submits it to database
     * Before doing so it checks if the username is unique
     */
    private void saveProfileSettings() {

        final String userName = mUserName.getText().toString();
        final String displayName = mDisplayName.getText().toString();
        final long phoneNumber = Long.valueOf(mPhoneNumber.getText().toString());
        final String about = mAbout.getText().toString();
        final String website = mWebsite.getText().toString();
        final String profile_url = myRef.child(currentUser.getUid()).child("profile_photo").getKey();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!mUserSettings.getUser().getUsername().equals(userName)
                        && !mUserSettings.getUser().getDisplay_name().equals(displayName)) {
                    firebaseMethods.checkIfUsernameExists(userName, displayName, website, about, phoneNumber, profile_url);
                } else
                    firebaseMethods.checkIfUsernameExists(userName, displayName, website, about, phoneNumber, profile_url);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setProfileWidgets(UserSettings userSettings) {

        Log.d(TAG, "setProfileWidgets: setting widgets with data, retrieving from database: " + userSettings.toString());
        User settings = userSettings.getUser();
        mUserSettings = userSettings;
        mDisplayName.setText(settings.getDisplay_name());

        mUserName.setText(settings.getUsername());
        mWebsite.setText(settings.getWebsite());
        mAbout.setText(settings.getAbout());
        mEmail.setText(String.valueOf(settings.getEmail()));
        mPhoneNumber.setText(String.valueOf(settings.getPhone_number()));
        String profilePicURL = settings.getProfile_photo();

        try {
            if (profilePicURL == null) {
                mProfilePhoto.setImageURI(avatarUri);
            } else
                Glide.with(this).load(profilePicURL).centerCrop().into(mProfilePhoto);

        } catch (IllegalArgumentException e) {
            mProfilePhoto.setImageURI(avatarUri);
        }

    }

    private void updateUserInfo(String imageUrl) {
        final String about = mAbout.getText().toString();
        final String display_name = mDisplayName.getText().toString();
        long phone_number = Long.valueOf(mPhoneNumber.getText().toString());
        final String username = mUserName.getText().toString();
        final String website = mWebsite.getText().toString();

        Query query = myRef
                .child(getString(R.string.dbname_users))
                .child(currentUser.getUid())
                .child("profile_photo").getRef();

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    firebaseMethods.updateUsername(username, display_name, website, about, phone_number, imageUrl);
                } else {
                    firebaseMethods.updateUsername(username, display_name, website, about, phone_number, "");
                    mProfilePhoto.setImageURI(avatarUri);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

                uploadProfilePic();
                updateUserInfo(getProf_pic_URL());

                break;
            case R.id.profile_photo:
                dialogChoice();
                break;

            case R.id.about:
                mAbout.clearComposingText();
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
                if (isAdded())
                    setProfileWidgets(firebaseMethods.getUserSettings(dataSnapshot));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Proccess canceled, " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void uploadProfilePic() {

        if (uri != null) {

            String dateStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("uploading, please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            profilePicStorage = storage.getReference().child("profile_pic/" + currentUser.getUid()).child("profile picture");

            profilePicStorage.putFile(uri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    profilePicStorage.getDownloadUrl().addOnCompleteListener(task1 -> {
                        setProf_pic_URL(task1.getResult().toString()); //prof_pic_URL = task1.getResult().toString();
                        Log.d(TAG, "uploadProfilePic: URL= " + prof_pic_URL);
                        pushImageUrl(task1.getResult().toString());// we push the url of the stored image to database so we can download it later
                        Picasso.get().load(getProf_pic_URL()).resize(mProfilePhoto.getWidth(), mProfilePhoto.getHeight()).centerCrop().into(mProfilePhoto);
                    }).addOnFailureListener(e ->
                            Toast.makeText(EditProfileFragment.this.getActivity(), "Failed, " + e.getMessage(), Toast.LENGTH_SHORT).show());
                } else
                    Toast.makeText(EditProfileFragment.this.getActivity(), "Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Failed, " + e.getMessage(), Toast.LENGTH_SHORT).show();

            }).addOnCanceledListener(() -> {
                return;

            }).addOnProgressListener(taskSnapshot -> {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("uploaded " + (int) progress + "%");
            });
        } else
            Toast.makeText(getActivity(), "Error: Profile picture is " + new NullPointerException().getMessage(), Toast.LENGTH_SHORT).show();// this to handle in case uri or bitmap is null
    }

    private void pushImageUrl(final String imageUrl) {

        myRef.child(getString(R.string.dbname_users)).child(currentUser.getUid()).child("profile_photo").setValue(imageUrl);
        myRef.push().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (isAdded())
                    Toast.makeText(getActivity(), "Image Uploaded successfully. ", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Uploading error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dialogChoice() {

        final CharSequence[] options = {"CAMERA", "GALLERY", "CANCEL"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Image");
        builder.setIcon(R.drawable.chefood);

        builder.setItems(options, (dialog, which) -> {
            if (options[which].equals("CAMERA")) {
                takePicture();

            } else if (options[which].equals("GALLERY")) {
                selectPicture();
            } else if (options[which].equals("CANCEL")) {
                dialog.dismiss();
            }

        });
        builder.show();
    }

    private void takePicture() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (getBatteryLevel() > 10 && cameraIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } else Toast.makeText(getActivity(), "Battery is low...", Toast.LENGTH_SHORT).show();

    }

    private void selectPicture() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), REQUEST_IMAGE_GALLERY);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK && requestCode == REQUEST_CAMERA && data != null && data.getData() != null) {
//
            uri = data.getData();
            Glide.with(this).load(uri).centerCrop().into(mProfilePhoto);

        } else if (requestCode == REQUEST_IMAGE_GALLERY && data != null && data.getData() != null) {
            uri = data.getData();
            Glide.with(this).load(uri).centerCrop().into(mProfilePhoto);
        } else
            mProfilePhoto.setImageResource(R.drawable.my_avatar);
        Toast.makeText(getContext(), "Something went wrong! " + new Exception().getMessage(), Toast.LENGTH_SHORT).show();
    }


//    private void UpdateUserDataBaseInfo() {
//        final String userName = mUserName.getText().toString();
//        final String email = mEmail.getText().toString();
//        final String displayName = mDisplayName.getText().toString();
//
//        final long phoneNumber = Long.valueOf(mPhoneNumber.getText().toString());
//        final String about = mAbout.getText().toString();
//        final String website = mWebsite.getText().toString();
//        final String photo_url = "";
//        final long followers = 0;
//        final long following = 0;
//        final long posts = 0;
//
//
//        myRef.child(currentUser.getUid()).child("about").setValue(about);
//        myRef.child(currentUser.getUid()).child("display_name").setValue(displayName);
//        myRef.child(currentUser.getUid()).child("email").setValue(email);
//        myRef.child(currentUser.getUid()).child("phone_number").setValue(phoneNumber);
//        myRef.child(currentUser.getUid()).child("profile_photo").setValue(photo_url);
//        myRef.child(currentUser.getUid()).child("user_name").setValue(userName);
//        myRef.child(currentUser.getUid()).child("website").setValue(website);
//
//        myRef.push().addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Toast.makeText(getActivity(), "Data updated.", Toast.LENGTH_SHORT).show();
//                firebaseMethods.getUserSettings(dataSnapshot);
//                mAbout.setText(about);
//                mDisplayName.setText(displayName);
//                mEmail.setText(email);
//                mPhoneNumber.setText(String.valueOf(phoneNumber));
//                mUserName.setText(userName);
//                mWebsite.setText(website);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getActivity(), "Data update error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}