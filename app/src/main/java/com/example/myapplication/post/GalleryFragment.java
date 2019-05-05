package com.example.myapplication.post;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.utility_classes.DirectorySearch;
import com.example.myapplication.utility_classes.FilePath;
import com.example.myapplication.utility_classes.GridImageAdapter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * File created by tcarau18
 **/
public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";

    private ImageView galleryImageView;
    private Button mGalleryButton;
    private Uri mUri;
    private Intent intent;
    private TextView nextText;
    private ImageView closePost;
    /*ByteArrayOutputStream byteArrayOutputStream;
    Bitmap mBitmap;*/

    private static final int GALLERY_REQUEST = 1;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_gallery,container,false);

        galleryImageView = view.findViewById(R.id.imageView_gallery);
        mGalleryButton = view.findViewById(R.id.open_gallery_button);
        intent = new Intent(getActivity(), NextActivity.class);
        closePost = view.findViewById(R.id.close_share);
        nextText = view.findViewById(R.id.textview_next);
       // byteArrayOutputStream = new ByteArrayOutputStream();

        closePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        nextText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });


        mGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);

            }
        });

        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
          mUri = data.getData();
          galleryImageView.setImageURI(mUri);
          intent.putExtra("imageUri", mUri);
         /*   mBitmap = (Bitmap) data.getExtras().get("data");
            mUri = data.getData();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100,baos);
            byte [] bImage = baos.toByteArray();
            Bundle b = new Bundle();
            b.putByteArray("pic",bImage);
            intent.putExtra("imageUri", mUri);
            intent.putExtras(b);*/

        }
    }
}
