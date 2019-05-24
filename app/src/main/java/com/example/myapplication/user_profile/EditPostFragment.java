package com.example.myapplication.user_profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.utility_classes.FirebaseMethods;

/**
 * File created by tcarau18
 **/
public class EditPostFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "SignOutFragment";


    private FirebaseMethods firebaseMethods;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_out, container, false);
        firebaseMethods =  FirebaseMethods.getInstance(getContext());


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onClick(View v) {

    }

//    private void setupFirebaseAuth() {
//        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");
//
//        mAuth = FirebaseAuth.getInstance();
//
//        mAuthListener = firebaseAuth -> {
//            FirebaseUser user = firebaseAuth.getCurrentUser();
//
//            if (user != null) {
//                Log.d(TAG, "onAuthStateChanged: signed in with " + user.getUid());
//
//            } else {
//                Log.d(TAG, "onAuthStateChanged: signed out");
//                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//                startActivity(intent);
//            }
//        };
//
//    }

}
