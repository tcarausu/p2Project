package com.example.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * File created by tcarau18
 **/
public class ViewPostFragment extends Fragment {

    private static final String TAG = "ViewPostFragment";

    private TextView image_likes_TEXT, image_caption_TEXT , image_comment_link_TEXT , image_time_posted_TEXT ;
    private ImageView image_heart_white_BUTTON, speech_bubble_BUTTON ;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_post ,container,false);
        findWidgets(view);

        image_heart_white_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_heart_white_BUTTON.setImageResource(R.drawable.orange_liked);
            }
        });


        return view;
    }

    private void findWidgets(View view) {
        image_likes_TEXT = view.findViewById(R.id.image_likes);
        image_caption_TEXT = view.findViewById(R.id.image_caption);
        image_comment_link_TEXT = view.findViewById(R.id.image_comment_link);
        image_time_posted_TEXT = view.findViewById(R.id.image_time_posted);
        image_heart_white_BUTTON = view.findViewById(R.id.image_heart_white);
        speech_bubble_BUTTON = view.findViewById(R.id.speech_bubble);

    }
}
