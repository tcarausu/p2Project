package com.example.myapplication.user_profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * File created by tcarau18
 **/
public class SignOutFramgnet extends Fragment {

    private static final String TAG = "SignOutFramgnet";
    private FirebaseAuth mAuth ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth =  FirebaseAuth.getInstance() ;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_out, container, false);











        return view;
    }

    @Override
    public void onStart() {

        super.onStart();
//

    }


}
