package com.example.myapplication.user_profile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.models.User;
import com.example.myapplication.utility_classes.BottomNavigationViewHelper;
import com.example.myapplication.utility_classes.FirebaseMethods;
import com.example.myapplication.utility_classes.OurAlertDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * File created by tcarau18
 * Functionalism by Mo.Msaad
 **/
public class EditProfileFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "EditProfileFragment";
    private static final int REQUEST_CAMERA = 11;
    private static final int REQUEST_GALLERY = 22;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;

    // firebase storage
    private StorageReference profilePicStorage;
    private FirebaseStorage storage;
    private FirebaseDatabase database;

    //Edit Profile widgets
    private TextView mPrivateInformation;
    private FloatingActionButton mChangeProfilePhoto;
    private EditText mDisplayName, mWebsite, mAbout, mPhoneNumber;
    private TextView mEmail, mUserName;
    private CircleImageView mProfilePhoto, smallProfilePic;
    private ImageView backArrow, saveChanges;
    private User user;
    private Uri uri;

    public Uri getUri() {
        return uri;
    }

    @Exclude
    public void setUri(Uri uri) {
        this.uri = uri;
    }

    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String prof_pic_URL;
    private int batteryLevel;
    private final int ACTIVITY_NUM1 = 1, ACTIVITY_NUM2 = 2, ACTIVITY_NUM3 = 3, ACTIVITY_NUM4 = 4;
    private final List<Integer> act = new ArrayList<>();
    private BroadcastReceiver broadcastReceiver;


    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setupBroadcastReceiver();
        setupBottomNavigationView(view);
        checkPermissions();
        connectDatabase();
        initLayouts(view);
        buttonListneres();
        getUserInfo();


        return view;
    }

    private void setupBroadcastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                batteryLevel = (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1));
            }
        };
        Objects.requireNonNull(getActivity()).registerReceiver(this.broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));// this to get the batteryLevel
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void connectDatabase() {
        mFirebaseMethods = FirebaseMethods.getInstance(getActivity());
        mAuth = FirebaseMethods.getAuth();
        mFirebaseMethods.autoDisconnect(getActivity());
        currentUser = mAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseMethods.getmFirebaseDatabase();
        myRef = mFirebaseDatabase.getReference();
        storage = FirebaseMethods.getFirebaseStorage();
        profilePicStorage = storage.getReference();
    }

    public void initLayouts(View view) {
        mDisplayName = view.findViewById(R.id.display_name);
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

    }

    private void buttonListneres() {
        backArrow.setOnClickListener(this);
        saveChanges.setOnClickListener(this);
        mProfilePhoto.setOnClickListener(this);
        mChangeProfilePhoto.setOnClickListener(this);
    }

    /**
     * @param view root view that will be used to inflate the dialog layout
     * @author Mo.Msaad
     **/
    private void setupBottomNavigationView(View view) {

        BottomNavigationViewHelper bnvh = new BottomNavigationViewHelper(getActivity());
        BottomNavigationViewEx bottomNavigationViewEx = view.findViewById(R.id.bottomNavigationBar);
        bnvh.setupBottomNavigationView(bottomNavigationViewEx);
        bnvh.enableNavigation(getActivity(), bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();

        //Mo.Msaad.Modifications
        MenuItem menuItem1, menuItem2, menuItem3, menuItem4;
        act.add(ACTIVITY_NUM1);
        act.add(ACTIVITY_NUM2);
        act.add(ACTIVITY_NUM3);
        act.add(ACTIVITY_NUM4);

        switch (act.iterator().next()) {
            case 0:
                menuItem1 = menu.getItem(act.get(0));
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                menuItem1.setChecked(true);
                break;
            case 1:
                menuItem2 = menu.getItem(act.get(1));
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                menuItem2.setChecked(true);
                break;
            case 2:
                menuItem3 = menu.getItem(act.get(2));
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                menuItem3.setChecked(true);
                break;
            case 3:
                menuItem4 = menu.getItem(act.get(3));
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                menuItem4.setChecked(true);
                break;
        }

    }


    private String getProf_pic_URL() {
        return prof_pic_URL;
    }

    @Exclude
    private void setProf_pic_URL(String prof_pic_URL) {
        this.prof_pic_URL = prof_pic_URL;
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
                uploadProfilePic(getUri());
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

        View layoutView = getLayoutInflater().inflate(R.layout.dialog_wifi_check, null);
        Button wifiButton = layoutView.findViewById(R.id.WIFI);
        Button mobileDataButton = layoutView.findViewById(R.id.DATA);
        ImageButton cancelButton = layoutView.findViewById(R.id.CANCEL);

        OurAlertDialog.Builder myDialogBuilder = new OurAlertDialog.Builder(getActivity());
        myDialogBuilder.setView(layoutView);
        final AlertDialog alertDialog = myDialogBuilder.create();
        WindowManager.LayoutParams wlp = alertDialog.getWindow().getAttributes();
        wlp.windowAnimations = R.style.AlertDialogAnimation;
        wlp.gravity = Gravity.CENTER;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;

        alertDialog.getWindow().setAttributes(wlp);
        alertDialog.setCanceledOnTouchOutside(true);
        // Setting transparent the background (layout) of alert dialog
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        wifiButton.setOnClickListener(v -> {
            Intent wifiIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            getActivity().startActivityForResult(wifiIntent, 233);
            alertDialog.dismiss();
        });
        mobileDataButton.setOnClickListener(v -> {
            uploadProfilePic(getUri());
            updateUserInfo(getProf_pic_URL());
            alertDialog.dismiss();
        });
        cancelButton.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

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
                smallProfilePic.setImageResource(R.drawable.my_avatar);
            } else
                Glide.with(getActivity().getApplicationContext()).load(profilePicURL).centerCrop().into(mProfilePhoto);
                Glide.with(getActivity().getApplicationContext()).load(profilePicURL).centerCrop().into(smallProfilePic);

        } catch (NullPointerException e) {
            Log.d(TAG, "setProfileWidgets: error " + e.getMessage());
            mProfilePhoto.setImageResource(R.drawable.my_avatar);
            smallProfilePic.setImageResource(R.drawable.my_avatar);
        }

    }

    /**
     * @param imageUrl: new produced url from the storage database.
     *                  changes the existing url in the database.
     **/
    private void updateUserInfo(String imageUrl) {
        final String about = mAbout.getText().toString();
        final String display_name = mDisplayName.getText().toString();
        long phone_number = Long.valueOf(mPhoneNumber.getText().toString().trim());
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
                    mFirebaseMethods.updateUsername(currentUser.getUid(), username, display_name, website, about, phone_number, imageUrl);
                    Log.d(TAG, "onDataChange: datasnapshot exissts: " + dataSnapshot.exists());
                    Log.d(TAG, "onDataChange: user updated with:\n " + "name: " + username
                            + "\n" + "displayName: " + display_name + "\n" + "website: " + website + "\n"
                            + "about: " + about + "\n" + "phone: " + phone_number + "\n" + "URL: " + imageUrl);
                } else {
                    mFirebaseMethods.updateUsername(currentUser.getUid(), username, display_name, website, about, phone_number, "");
                    mProfilePhoto.setImageResource(R.drawable.my_avatar);
                    smallProfilePic.setImageResource(R.drawable.my_avatar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mProfilePhoto.setImageResource(R.drawable.my_avatar);
                smallProfilePic.setImageResource(R.drawable.my_avatar);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onPause() {
        super.onPause();
        Objects.requireNonNull(getActivity()).unregisterReceiver(broadcastReceiver);//this to destroy the intent to stop leakage

    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).registerReceiver(this.broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));// we create it back in onResume

    }

    /**
     * method  created by Mo.Msaad
     *
     * @param uri: this is the received uri from the onActivity result, via setters and getters to avoid getting null if the user
     *             presses the upload button without chosing a photo
     **/
    private void uploadProfilePic(Uri uri) {

        if (getUri() == null) {
            Toast.makeText(getActivity(), "No image is selected ", Toast.LENGTH_SHORT).show();// this to handle in case uri or bitmap is null
            Glide.with(this).load(user.getProfile_photo()).centerInside().into(mProfilePhoto);

        } else {

            Glide.with(this).load(uri).centerInside().into(mProfilePhoto);
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("uploading, please wait...");
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
                        Glide.with(this).load(getProf_pic_URL()).centerCrop().into(mProfilePhoto);
                        goBack();
//                        Picasso.get().load(getProf_pic_URL()).resize(mProfilePhoto.getWidth(), mProfilePhoto.getHeight()).centerCrop().into(mProfilePhoto);
                    }).addOnFailureListener(e ->
                            Toast.makeText(EditProfileFragment.this.getActivity(), "Failed, " + e.getMessage(), Toast.LENGTH_SHORT).show());
                } else
                    Toast.makeText(EditProfileFragment.this.getActivity(), "Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "upload failed, " + e.getMessage(), Toast.LENGTH_SHORT).show();

            }).addOnCanceledListener(() -> {


            }).addOnProgressListener(taskSnapshot -> {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("uploaded " + (int) progress + "%");
                progressDialog.setProgressDrawable(getResources().getDrawable(R.drawable.simo_progress_bar));

            });
        }

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

        View layoutView = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        ImageButton cameraButton = layoutView.findViewById(R.id.simo_dialog_camera_button);
        ImageButton galleryButton = layoutView.findViewById(R.id.simo_dialog_gallery_button);
        ImageButton cancelButton = layoutView.findViewById(R.id.simo_dialog_cancel_button);

        OurAlertDialog.Builder myDialogBuilder = new OurAlertDialog.Builder(getActivity());
        myDialogBuilder.setView(layoutView);
        myDialogBuilder.setIcon(R.mipmap.chefood_icones);
        final AlertDialog alertDialog = myDialogBuilder.create();
        WindowManager.LayoutParams wlp = alertDialog.getWindow().getAttributes();
        wlp.windowAnimations = R.style.AlertDialogAnimation;
        wlp.gravity = Gravity.CENTER;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;

        alertDialog.getWindow().setAttributes(wlp);
        alertDialog.setCanceledOnTouchOutside(true);
        // Setting transparent the background (layout) of alert dialog
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        cameraButton.setOnClickListener(v -> {
            takePicture();
            alertDialog.dismiss();
        });
        galleryButton.setOnClickListener(v -> {
            selectPicture();
            alertDialog.dismiss();
        });
        cancelButton.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

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
        if (batteryLevel > 10) {
            Toast.makeText(getActivity(), "Battery is low...", Toast.LENGTH_SHORT).show();
        } else if (cameraIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
            Log.d(TAG, "takePicture: battery level: " + batteryLevel);
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        }

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
        boolean besoins = resultCode == RESULT_OK && requestCode == REQUEST_CAMERA && data.getData() != null;
        boolean besoins1 = resultCode == RESULT_OK && requestCode == REQUEST_GALLERY && data.getData() != null;

        try {
            if (besoins) {
                setUri(data.getData());
                Glide.with(this).load(this.getUri()).centerCrop().into(mProfilePhoto);
                mProfilePhoto.refreshDrawableState();
            } else if (besoins1) {

                setUri(data.getData());
                Glide.with(this).load(this.getUri()).centerCrop().into(mProfilePhoto);
                mProfilePhoto.refreshDrawableState();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void getUserInfo() {

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // retrive user information from the database
                if (isAdded())
                    setProfileWidgets(mFirebaseMethods.getUserSettings(dataSnapshot));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: ");
            }
        });
    }

    private void goBack() {
        mFirebaseMethods.goToWhereverWithFlags(getActivity(), UserProfileActivity.class);
        getActivity().overridePendingTransition(R.anim.right_enter, R.anim.right_out);
        getActivity().finish();
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
            case R.id.change_profile_photo:
                dialogChoice();
                break;
        }
    }

}

//TODO this if we dont want to display the dialog everytime and save the user choice for next use.
//    private void verifyFirstTry() {
//        SharedPreferences prefs = getActivity().getSharedPreferences("data prefs", MODE_PRIVATE);
//        boolean First_data_prefs = prefs.getBoolean("data prefs", true);
//        if (First_data_prefs) {//if its the first run we change the boolean to false
//            openDialogChoice();
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putBoolean("data prefs", false);
//            editor.apply();
//        } else {// then if boolean is false we skip the slides
//            uploadProfilePic(uri);
//            updateUserInfo(getProf_pic_URL());
//        }
//    }