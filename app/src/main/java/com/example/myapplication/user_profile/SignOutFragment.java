package com.example.myapplication.user_profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.login.LoginActivity;
import com.example.myapplication.utility_classes.FirebaseMethods;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * File created by tcarau18
 **/
public class SignOutFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "SignOutFragment";

    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressBar mProgressBar;
    private TextView tvSignOut, tvSigningOut;
    private Button btnConfirmingSignOut;

    private FirebaseMethods firebaseMethods;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_out, container, false);

        firebaseMethods = new FirebaseMethods(getActivity());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("353481374608-mg7rvo8h0kgjmkuts5dcmq65h2louus5.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        tvSignOut = view.findViewById(R.id.tvConfirmSignOut);
        tvSigningOut = view.findViewById(R.id.tvSigningOut);
        mProgressBar = view.findViewById(R.id.progress_bar);
        btnConfirmingSignOut = view.findViewById(R.id.btnConfirmSignOut);

        mProgressBar.setVisibility(View.GONE);
        tvSigningOut.setVisibility(View.GONE);

        btnConfirmingSignOut.setOnClickListener(this);

        setupFirebaseAuth();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnConfirmSignOut) {
            Log.d(TAG, "onClick: attempting to Sing Out");
            mProgressBar.setVisibility(View.VISIBLE);
            tvSigningOut.setVisibility(View.VISIBLE);

            mAuth.signOut();
            mGoogleSignInClient.signOut();
            LoginManager.getInstance().logOut();

            getActivity().finish();

            Toast.makeText(getActivity(), "Successful Sign Out", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    ------------------------------------------------- FIREBASE SETUP -------------------------------------------------
     */
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user != null) {
                Log.d(TAG, "onAuthStateChanged: signed in with " + user.getUid());

            } else {
                Log.d(TAG, "onAuthStateChanged: signed out");
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
            }
        };

    }
   /*
    ------------------------------------------------- FIREBASE SETUP -------------------------------------------------
     */

}
