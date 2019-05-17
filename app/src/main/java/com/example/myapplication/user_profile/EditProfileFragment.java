package com.example.myapplication.user_profile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * File created by tcarau18
 **/
public class EditProfileFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "EditProfileFragment";
    private static final int REQUEST_CAMERA = 11;
    private static final int REQUEST_GALLERY = 22;
    private static final int ACTION_WIFI_SETTINGS = 55;


    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;

    //Edit Profile widgets
    private TextView mChangeProfilePhoto, mPrivateInformation;
    private EditText mDisplayName, mWebsite, mAbout, mPhoneNumber;
    private TextView mEmail, mUserName;
    private CircleImageView mProfilePhoto , smallProfilePic;
    private ImageView backArrow, saveChanges;

    private FirebaseMethods firebaseMethods;
    private FirebaseUser currentUser;
    private Context mContext;
    private User user;
    private Uri uri, avatarUri;
    private StorageReference profilePicStorage;
    private FirebaseStorage storage;
    private FirebaseDatabase database;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setBatteryLevel(intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1));
        }
    };

    private Bitmap bitmap;
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String prof_pic_URL;
    private int batteryLevel;


    private int getBatteryLevel() {
        return batteryLevel;
    }

    private void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    private String getProf_pic_URL() {
        return prof_pic_URL;
    }

    private void setProf_pic_URL(String prof_pic_URL) {
        this.prof_pic_URL = prof_pic_URL;
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        checkPermissions();
        getActivity().registerReceiver(this.broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));// this to get the batteryLevel

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firebaseMethods = new FirebaseMethods(getActivity());
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        storage = FirebaseStorage.getInstance();
        profilePicStorage = storage.getReference();
        userID = mAuth.getCurrentUser().getUid();
        avatarUri = Uri.parse(String.valueOf(R.drawable.my_avatar));//"android.resource://com.example.myapplication/drawable/my_avatar"

        initLayouts(view);
        setupFirebaseAuth();

        return view;
    }

    public void initLayouts(View view) {

        mDisplayName = view.findViewById(R.id.displayName);
        smallProfilePic = view.findViewById(R.id.EditProfile_small_pic);

        mUserName = view.findViewById(R.id.username);
        mWebsite = view.findViewById(R.id.website);
        mAbout = view.findViewById(R.id.about);
        mEmail = view.findViewById(R.id.email);
        mPhoneNumber = view.findViewById(R.id.phoneNumber);
        mProfilePhoto = view.findViewById(R.id.profile_photo);
        backArrow = view.findViewById(R.id.backArrow);
        saveChanges = view.findViewById(R.id.save_changes);
        mChangeProfilePhoto = view.findViewById(R.id.change_profile_photo);
        mPrivateInformation = view.findViewById(R.id.privateInformation);
        backArrow.setOnClickListener(this);
        saveChanges.setOnClickListener(this);
        mProfilePhoto.setOnClickListener(this);
    }


    /**
     * created byMo.MSaad
     **/
    private void checkWifiState() {

        boolean isWifiConnected;
        boolean isMobileDataConnected;
        ConnectivityManager conMngr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = conMngr.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            isWifiConnected = activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            isMobileDataConnected = activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;

            if (isWifiConnected) {
                uploadProfilePic(uri);
                updateUserInfo(getProf_pic_URL());
            } else if (isMobileDataConnected) {
                //TODO add shared prefs here to allow automatic
                openDialogChoice();
            }
        }
    }

    /**
     * created byMo.MSaad
     **/


    private void openDialogChoice() {

        final CharSequence[] options = {"Mobile data", "WIFI", "CANCEL"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select network to proceed");
        builder.setIcon(R.drawable.chefood);
        builder.setItems(options, (dialog, which) -> {

            if (options[which].equals("Mobile data")) {
                uploadProfilePic(uri);
                updateUserInfo(getProf_pic_URL());

            } else if (options[which].equals("WIFI")) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

            } else if (options[which].equals("CANCEL")) {
                dialog.dismiss();
            }

        });
        builder.show();

    }

    private void setProfileWidgets(User userSettings) {

        Log.d(TAG, "setProfileWidgets: setting widgets with data, retrieving from database: " + userSettings.toString());
        user = userSettings;
        mDisplayName.setText(user.getDisplay_name());
        mUserName.setText(user.getUsername());
        mWebsite.setText(user.getWebsite());
        mAbout.setText(user.getAbout());
        mEmail.setText(String.valueOf(user.getEmail()));
        mPhoneNumber.setText(String.valueOf(user.getPhone_number()));
        String profilePicURL = user.getProfile_photo();

        try {

            if (profilePicURL == null) {
                mProfilePhoto.setImageResource(R.drawable.my_avatar);
            } else
                Glide.with(this).load(profilePicURL).centerCrop().into(mProfilePhoto);
            Glide.with(this).load(profilePicURL).centerCrop().into(smallProfilePic);

        } catch (IllegalArgumentException e) {
            mProfilePhoto.setImageResource(R.drawable.my_avatar);
            smallProfilePic.setImageResource(R.drawable.my_avatar);
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
                    Log.d(TAG, "onDataChange: datasnapshot exissts: " + dataSnapshot.exists());
                    Log.d(TAG, "onDataChange: user updated with:\n " + "name: " + username
                            + "\n" + "displayName: " + display_name + "\n" + "website: " + website + "\n"
                            + "about: " + about + "\n" + "phone: " + phone_number + "\n" + "URL: " + imageUrl);
                } else {
                    firebaseMethods.updateUsername(username, display_name, website, about, phone_number, "");
                    mProfilePhoto.setImageResource(R.drawable.my_avatar);
                    smallProfilePic.setImageResource(R.drawable.my_avatar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                mProfilePhoto.setImageURI(avatarUri);
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
                checkWifiState();

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
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user != null) {
                Log.d(TAG, "onAuthStateChanged: signed in with: " + user.getUid());
            } else Log.d(TAG, "onAuthStateChanged: signed out");
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // retrive user information from the database
                if (isAdded())
                    setProfileWidgets(firebaseMethods.getUserSettings(dataSnapshot));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (mAuth != null)
                    try {
                        Toast.makeText(getContext(), "Proccess canceled, " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.d(TAG, "onCancelled: exception: " + e.getMessage());
                    }
            }
        });
    }

    /**
     * method  created by Mo.Msaad
     **/
    private void uploadProfilePic(Uri uri) {

        if (uri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("uploading, please wait...");
            progressDialog.setIcon(R.drawable.chefood);
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

    /**
     * method  created by Mo.Msaad
     **/
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
                return;
            }
        });
    }

    /**
     * method  created by Mo.Msaad
     **/
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

    /**
     * method  created by Mo.Msaad
     **/
    private void checkPermissions() {

        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), permissions, REQUEST_GALLERY);
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), permissions, REQUEST_CAMERA);
        }
    }

    private void takePicture() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (getBatteryLevel() > 10 && cameraIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
            Log.d(TAG, "takePicture: battery level: " + getBatteryLevel());
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } else Toast.makeText(getActivity(), "Battery is low...", Toast.LENGTH_SHORT).show();

    }

    /**
     * method  created by Mo.Msaad
     **/
    private void selectPicture() {

        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), REQUEST_GALLERY);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        boolean besoins = resultCode == RESULT_OK && requestCode == REQUEST_CAMERA && data.getData() != null;
        boolean besoins1 = resultCode == RESULT_OK && requestCode == REQUEST_GALLERY && data.getData() != null;

        try {
            if (besoins) {
                uri = data.getData();
                Glide.with(this).load(data.getData()).centerCrop().into(mProfilePhoto);
            }
                else if (besoins1) {
                    uri = data.getData();
                    Glide.with(this).load(uri).centerCrop().into(mProfilePhoto);

                }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}


//    private void verifyFirstTry() {
//        SharedPreferences prefs = getActivity().getSharedPreferences("data prefs", MODE_PRIVATE);
//        boolean First_data_prefs = prefs.getBoolean("data prefs", true);
//
//        if (First_data_prefs) {//if its the first run we change the boolean to false
//            openDialogChoice();
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putBoolean("data prefs", false);
//
//            editor.apply();
//        } else {// then if boolean is false we skip the slides
//            //TODO
//            uploadProfilePic(uri);
//            updateUserInfo(getProf_pic_URL());
//        }
//    }