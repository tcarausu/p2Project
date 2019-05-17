package com.example.myapplication.post;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.home.HomeActivity;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

/**
 * File created by tcarau18
 **/
public class SelectPictureFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "SelectPictureFragment";
    private static final int CAMERA_REQUEST = 44;
    private static final int GALLERY_REQUEST = 11;

    private ImageView galleryImageView;
    private Button mSelectPicButton;
    private Uri mUri;
    private Intent intent;
    private TextView nextText;
    private ImageView closePost;
    ByteArrayOutputStream byteArrayOutputStream;
    Bitmap mBitmap;


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setBatteryLevel(intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -3));//here i set the int battery level
        }
    };
    private int batteryLevel;


    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_picture, container, false);

        /**14

         You should unregister the receivers in onPause() and register them in onResume().
         This way, when Android destroys and recreates the activity for the configuration change,
         or for any reason you will still have receivers set up.
         * **/
        getActivity().registerReceiver(this.broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));


        intent = new Intent(getActivity(), NextActivity.class);

        setLayout(view);

        return view;
    }

    private void setLayout(View view) {

        galleryImageView = view.findViewById(R.id.imageView_gallery);
        mSelectPicButton = view.findViewById(R.id.choose_pic_button);
        closePost = view.findViewById(R.id.close_share);
        nextText = view.findViewById(R.id.textview_next);
        byteArrayOutputStream = new ByteArrayOutputStream();

        closePost.setOnClickListener(this);
        nextText.setOnClickListener(this);
        mSelectPicButton.setOnClickListener(this);

    }

    // showing picture taken from camera in ImageView
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST ) {
            mUri = data.getData();
            Glide.with(getContext()).load(mUri).fitCenter().centerCrop().into(galleryImageView);
            intent.putExtra("imageUri", mUri.toString());
        }

    }

    // Alert dialog
    private void dialogChoice() {
        final CharSequence[] options = {"CAMERA", "GALLERY", "CANCEL"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
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

    // open camera method
    private void takePicture() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (getBatteryLevel() > 10 && cameraIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
            Log.d(TAG, "takePicture: battery level: " + getBatteryLevel());
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        } else Toast.makeText(getActivity(), "Battery is low...", Toast.LENGTH_SHORT).show();

    }

    // open gallery method
    private void selectPicture() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.close_share:
                Intent toHomeActivity = new Intent(getActivity(), HomeActivity.class);
                startActivity(toHomeActivity);

                break;

            case R.id.textview_next:
                if (galleryImageView.getDrawable() == null) {
                    Toast.makeText(getActivity(), R.string.please_select_picture,
                            Toast.LENGTH_LONG).show();
                } else {
                    startActivity(intent);
                }

                break;

            case R.id.choose_pic_button:
                Log.d(TAG, "onClick: back button working");
                dialogChoice();

                break;
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

    // showing picture taken from camera in ImageView
//


}
