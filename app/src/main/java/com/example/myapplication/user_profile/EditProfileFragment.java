package com.example.myapplication.user_profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.myapplication.R;
import com.example.myapplication.utility_classes.UniversalImageLoader;

import java.util.Objects;

/**
 * File created by tcarau18
 **/
public class EditProfileFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "EditProfileFragment";

    private ImageView mProfilePhoto;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        initLayouts(view);
        setProfileImage();

        return view;
    }

    public void initLayouts(View view) {

        mProfilePhoto = view.findViewById(R.id.profile_photo);
        view.findViewById(R.id.backArrow);


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
//                getActivity().finish();
                break;
        }
    }
}
