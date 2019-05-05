package com.example.myapplication.post;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.utility_classes.Permissions;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;

/**
 * File created by tcarau18
 **/
public class PhotoFragment extends Fragment {
    private static final String TAG = "PhotoFragment";

    private static final int photoFragmentNum = 1;
    private static final int CAMERA_REQUEST_CODE = 5;
    private ImageView mImageView;
    private Uri imageUri;
    private TextView nextText;
    ByteArrayOutputStream byteArrayOutputStream;
    Bitmap mBitmap;
    Intent mNextintent;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_photo,container,false);

        Button buttonCameraLaunch  = (Button) view.findViewById(R.id.button_camera_launch);
        mImageView = view.findViewById(R.id.camera_fragment_imageView);

        byteArrayOutputStream = new ByteArrayOutputStream();

        /**
         * Opening camera if permission allowed
         *
         * */
        mNextintent = new Intent(getActivity(), NextActivity.class);

        nextText = view.findViewById(R.id.textview_next);
        nextText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mImageView.getDrawable() == null){
                    Toast.makeText(getActivity(), "Please choose a picture",
                            Toast.LENGTH_LONG).show();
                }
                else {
                startActivity(mNextintent);
                }
            }
        });

        buttonCameraLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((AddPostActivity)getActivity()).getTabNumber() == photoFragmentNum){
                    if (((AddPostActivity)getActivity()).checkPermissions(Permissions.PERMISSIONS_CAMERA[0])){
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent,CAMERA_REQUEST_CODE);
                    }else {
                        Intent intent = new Intent(getActivity(), AddPostActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                }

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){
            mBitmap = (Bitmap) data.getExtras().get("data");
            imageUri = data.getData();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100,baos);
            byte [] bImage = baos.toByteArray();
            Bundle b = new Bundle();
            b.putByteArray("pic",bImage);
            mNextintent.putExtra("imageUri", imageUri);
            mNextintent.putExtras(b);

            mImageView.setImageBitmap(mBitmap);
            Log.d(TAG, "onActivityResult: " + mBitmap.getHeight() + " - "+ mBitmap.getWidth());
            Log.d(TAG, "onActivityResult: ");
            Log.d(TAG, "onActivityResult: " + mImageView.getMaxHeight()/Math.pow(10.0,6.0)
                    + " " + mImageView.getMaxWidth()/Math.pow(10.0,6.0) );
        }
    }
}
