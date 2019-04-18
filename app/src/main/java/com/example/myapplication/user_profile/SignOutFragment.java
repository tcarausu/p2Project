package com.example.myapplication.user_profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * File created by tcarau18
 **/
public class SignOutFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "SignOutFragment";

    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private ProgressBar mProgressBar;
    private TextView tvSignOut, tvSigningOut;
    private Button btnConfirmingSignOut;

    private FirebaseMethods firebaseMethods;
    private Context context;

    private String username = "toader carausu";
    private String append = " ";

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_out, container, false);

        context = getActivity();
        firebaseMethods = new FirebaseMethods(context);

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

    /*
    ------------------------------------------------- FIREBASE SETUP -------------------------------------------------
     */


    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: signed in" + user.getUid());

                } else {
                    Log.d(TAG, "onAuthStateChanged: signed out");
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intent);
                }
            }
        };
    }

//    private void setupFirebaseAuth() {
//        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");
//
//        mAuth = FirebaseAuth.getInstance();
//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        myRef = mFirebaseDatabase.getReference();
//
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//
//                if (user != null) {
//                    Log.d(TAG, "onAuthStateChanged: signed in" + user.getUid());
//
//                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            if (firebaseMethods.checkIfUserExists(username, dataSnapshot)) {
//                                append = myRef.push().getKey().substring(3, 10); //generates a unique key (method from Firebase Database length of it is 7 chars
//                                Log.d(TAG, "onDataChange: username already exists. Appending random string to name");
//                            }
//
//                            username = username + append;
//                            firebaseMethods.addNewUser("t@gg.com", username, "slim shady", "", "");
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//
//                } else {
//                    Log.d(TAG, "onAuthStateChanged: signed out");
//                }
//            }
//        };
//
//    }

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
        switch (v.getId()) {

            case R.id.btnConfirmSignOut:
                Log.d(TAG, "onClick: attempting to Sing Out");
                mProgressBar.setVisibility(View.VISIBLE);
                tvSigningOut.setVisibility(View.VISIBLE);

                mAuth.signOut();
                mGoogleSignInClient.signOut();
                LoginManager.getInstance().logOut();

                getActivity().finish();

                Toast.makeText(getActivity(),"Successful Sign Out",Toast.LENGTH_SHORT).show();

                break;
        }
    }
}
