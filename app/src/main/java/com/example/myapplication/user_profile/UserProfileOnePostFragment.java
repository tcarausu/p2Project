package com.example.myapplication.user_profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.Objects;


/**
 * File created by tcarau18
 **/
public class UserProfileOnePostFragment extends Fragment implements View.OnClickListener {

    private static View view;

    private static TextView emailId, textId;
    private static ImageView imageId;
    private static LinearLayout structuredPost, postToShow;
    private static ImageButton firstType, secondType, thirdType, forthType;
    private static FragmentManager fragmentManager;
    private static Animation shakeAnimation;

    public UserProfileOnePostFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_profile_one_post, container, false);
        initViews();
        setListeners();
        return view;
    }

    private void initViews() {
        fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();

        emailId = view.findViewById(R.id.emailId);
        textId = view.findViewById(R.id.textId);
        imageId = view.findViewById(R.id.imageId);
        structuredPost = view.findViewById(R.id.structured_posts);
        postToShow = view.findViewById(R.id.postToShow);

//        firstType = view.findViewById(R.id.firstTypePost);
//        secondType = view.findViewById(R.id.secondTypePost);
//        thirdType = view.findViewById(R.id.thirdTypePost);
//        forthType = view.findViewById(R.id.forthTypePost);


        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.shake);


    }

    // Set Listeners
    private void setListeners() {
//        firstType.setOnClickListener(this);
//        secondType.setOnClickListener(this);
//        thirdType.setOnClickListener(this);
//        forthType.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.firstTypePost:
//                Toast.makeText(getActivity(),
//                        "ImageButton 1 is clicked!", Toast.LENGTH_SHORT).show();
//
//                break;
//
//            case R.id.secondTypePost:
//
//                Toast.makeText(getActivity(),
//                        "ImageButton 2 is clicked!", Toast.LENGTH_SHORT).show();
//
//                break;
//
//            case R.id.thirdTypePost:
//                Toast.makeText(getActivity(),
//                        "ImageButton 3 is clicked!", Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.forthTypePost:
//
//                Toast.makeText(getActivity(),
//                        "ImageButton 4 is clicked!", Toast.LENGTH_SHORT).show();
//
//                fragmentManager
//                        .beginTransaction()
//                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
//                        .replace(R.id.frameContainer, new UserProfileOnePostFragment(),
//                                Utils.UserProfile_Fragment).commit();
//
//                break;
//        }

    }


}
