package com.example.myapplication.post;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.home.HomeActivity;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;

/**
 * File created by tcarau18
 **/
public class SelectPictureFragment extends Fragment {
    private static final String TAG = "SelectPictureFragment";

    private ImageView galleryImageView;
    private Button mSelectPicButton;
    private Uri mUri;
    private Intent intent;
    private TextView nextText;
    private ImageView closePost;
    ByteArrayOutputStream byteArrayOutputStream;
    Bitmap mBitmap;
    private static final int GALLERY_REQUEST = 1;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_picture, container, false);

        galleryImageView = view.findViewById(R.id.imageView_gallery);
        mSelectPicButton = view.findViewById(R.id.choose_pic_button);
        closePost = view.findViewById(R.id.close_share);
        nextText = view.findViewById(R.id.textview_next);
        intent = new Intent(getActivity(), NextActivity.class);
        byteArrayOutputStream = new ByteArrayOutputStream();

        closePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toHomeActivity = new Intent(getActivity(), HomeActivity.class);
                startActivity(toHomeActivity);
            }
        });

        nextText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (galleryImageView.getDrawable() == null) {
                    Toast.makeText(getActivity(), R.string.please_select_picture,
                            Toast.LENGTH_LONG).show();
                } else {
                    startActivity(intent);
                }
            }
        });

        mSelectPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChoice();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            mUri = data.getData();
            if (Build.VERSION.SDK_INT < 26)
                galleryImageView.setRotation(90);
            Picasso.get().load(mUri).fit().centerInside().into(galleryImageView);
            intent.putExtra("imageUri", mUri.toString());

        }
    }

    private void dialogChoice() {
        final CharSequence[] options = {"CAMERA", "GALLERY", "CANCEL"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Image");
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
        startActivityForResult(cameraIntent, GALLERY_REQUEST);

    }

    private void selectPicture() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }


}
