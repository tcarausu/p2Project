package com.example.myapplication.postActivity;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.home.HomeActivity;
import com.example.myapplication.models.User;
import com.example.myapplication.user_profile.UserProfileActivity;
import com.example.myapplication.utility_classes.FirebaseMethods;
import com.example.myapplication.utility_classes.OurAlertDialog;
import com.example.myapplication.utility_classes.Permissions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.melnykov.fab.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


public class SelectPictureFragment extends Fragment implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    //constants
    private static final String TAG = "SelectPictureFragment";
    private static final int CAMERA_REQUEST = 11;
    private static final int GALLERY_REQUEST = 22;
    private static final int PERMISSION = 123;
    private int batteryLevel;

    //firebase
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;

    //layout-intents
    private ImageView galleryImageView;
    private FloatingActionButton mSelectPicButton;
    private Uri mUri, savedUri;
    private Intent intent;
    private TextView nextText;
    private ImageView closePost;
    private CircleImageView circular_pic;
    private BroadcastReceiver broadcastReceiver ;
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_picture, container, false);
        registerBroadCastReceiever();
        connectFirebase();
        setUserProfilePic();
        setLayout(view);
        getSavedInstances(savedInstanceState);
        buttonSListeners();

        return view;
    }

    private void registerBroadCastReceiever() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                batteryLevel = (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -3));//here i set the int battery level
            }
        };
        getActivity().registerReceiver(this.broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    private void getSavedInstances(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedUri = Uri.parse((String) savedInstanceState.get("img"));
            mUri = savedUri ;
            galleryImageView.setImageURI(savedUri);
        }
    }

    private void connectFirebase() {
        mFirebaseMethods = FirebaseMethods.getInstance(getActivity());
        intent = new Intent(getActivity(), NextActivity.class);
        mFirebaseDatabase = FirebaseMethods.getmFirebaseDatabase();
        mAuth = FirebaseMethods.getAuth();
        myRef = mFirebaseDatabase.getReference("users").child(mAuth.getCurrentUser().getUid());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState.isEmpty()) {
            outState.putString("img", String.valueOf(mUri));
        }


    }

    private void setUserProfilePic() {
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final User user = dataSnapshot.getValue(User.class);
                    if (mAuth.getCurrentUser().getPhotoUrl().equals("")|| mAuth.getCurrentUser().getPhotoUrl().equals("photo") ) {
                        Glide.with(getApplicationContext()).load(R.drawable.my_avatar).centerCrop().into(circular_pic);

                    } else
                        Glide.with(getApplicationContext()).load(user.getProfile_photo()).centerCrop().into(circular_pic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

    private void setLayout(View view) {
        galleryImageView = view.findViewById(R.id.imageView_gallery);
        mSelectPicButton = view.findViewById(R.id.choose_pic_button);
        closePost = view.findViewById(R.id.close_share);
        nextText = view.findViewById(R.id.textview_next);
        circular_pic = view.findViewById(R.id.circular);
    }

    private void buttonSListeners() {
        closePost.setOnClickListener(this);
        circular_pic.setOnClickListener(this);
        nextText.setOnClickListener(this);
        mSelectPicButton.setOnClickListener(this);
        galleryImageView.setOnClickListener(this);

    }

    /**
     * This method will display chosen image in the galleryImageView
     * using Glide
     *
     * @param requestCode represents the Request Code
     * @param resultCode  represents the Result Code
     * @param data        represents the Data requested for the URI
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean besoins = resultCode == RESULT_OK && requestCode == CAMERA_REQUEST && mUri != null;

        try {
            if (besoins) {
                Glide.with(this).load(mUri).fitCenter().into(galleryImageView);
                intent.putExtra("imageUri", mUri.toString());
            } else if (resultCode == RESULT_OK) {
                mUri = data.getData();
                Glide.with(this).load(mUri).fitCenter().into(galleryImageView);
                intent.putExtra("imageUri", mUri.toString());
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method that will show a AlertDialog giving user ability to choose
     * to open CAMERA, GALLERY, or Cancel
     */
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
     * Method which will open built-in camera
     */
    private void takePicture() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "NEW PICTURE");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the camerea");
        mUri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (batteryLevel > 10 && cameraIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
            Log.d(TAG, "takePicture: battery level: " + batteryLevel);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        } else Toast.makeText(getActivity(), "Battery is low...", Toast.LENGTH_SHORT).show();

    }

    /**
     * Method which will open phone gallery
     */
    private void selectPicture() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");
        photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.close_share:
                mFirebaseMethods.goToWhereverWithFlags(getActivity(), HomeActivity.class);
                getActivity().overridePendingTransition(R.anim.left_enter,R.anim.right_out);
                getActivity().finish();
                break;

            case R.id.textview_next:
                if (mUri == null) {
                    Toast.makeText(getActivity(), R.string.please_select_picture,
                            Toast.LENGTH_LONG).show();
                } else {
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                }
                break;

            case R.id.choose_pic_button:
                Log.d(TAG, "onClick: back button working");
                checkPermissions();
                break;

            case R.id.imageView_gallery:
                checkPermissions();
                break;
            case R.id.circular:
                mFirebaseMethods.goToWhereverWithFlags(getActivity(), UserProfileActivity.class);
                getActivity().overridePendingTransition(R.anim.left_out,R.anim.right_enter);
                getActivity().finish();
                break;

        }
    }

    /**
     * This method calls dialogChoice(); if permissions accepted
     * if not accepted, it will request for permissions
     */
    @AfterPermissionGranted(PERMISSION)
    private void checkPermissions() {
        if (EasyPermissions.hasPermissions(getContext(), Permissions.PERMISSIONS)) {
            dialogChoice();
        } else {
            EasyPermissions.requestPermissions(this,
                    getString(R.string.permission_needed),
                    PERMISSION, Permissions.PERMISSIONS);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(this.broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));// this to avoid leakage of intent receiver
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }


}
