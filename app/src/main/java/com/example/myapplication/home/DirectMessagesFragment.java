package com.example.myapplication.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;

/**
 * File created by tcarau18
 **/
public class DirectMessagesFragment extends Fragment {
    private static final String TAG = "DirectMessagesFragment";

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_dirrect_messages,container,false);
        return view;
    }
}
