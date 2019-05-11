package com.example.myapplication.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.myapplication.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class SignUpFragment extends Fragment {
    private static final String TAG = "SignUpFragment";

//--------------- widgets---------------------

    private EditText mEmail;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private Button signUpButton, goBack;
    //===============================================
    private ProgressDialog loadingBar ;
    private FirebaseAuth mAuth;
    private FirebaseAuthSettings mAuthSettings ;

    private OnFragmentInteractionListener mListener;

    // Required empty public constructor
    public SignUpFragment() { }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingBar = new ProgressDialog(this.getContext());
        mAuth = FirebaseAuth.getInstance() ;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_fragment, container, false);// Inflate the layout for this fragment
        findWidgets(view);

        goBack.setOnClickListener(v -> goToLogin());
        signUpButton.setOnClickListener(v -> createUserWithEmail());


        return view;
    }

    private void findWidgets(View view) {
        mEmail = view.findViewById(R.id.SignUpWithEmail_emailField_id);
        mPassword = view.findViewById(R.id.SignUpWithEmail_passField_id);
        mConfirmPassword = view.findViewById(R.id.SignUpWithEmail_confPassField_id);
        signUpButton = view.findViewById(R.id.SignupWithPhoneFragment_sendCodeButton);
        goBack = view.findViewById(R.id.SignUpWithEmail_goBackButton_id);
    }

    private void createUserWithEmail(){
        // getting input from device
          final String email = mEmail.getText().toString();
          String password = mPassword.getText().toString() ;
          String confPass = mConfirmPassword.getText().toString();

          //checking if any is empty or pass doesn't match, the rest mAuth takes care of
                        if (TextUtils.isEmpty(email)  ) {
                            mEmail.setError("");
                            Toast.makeText(getContext(), "Please type in email or phone", Toast.LENGTH_SHORT).show();

                        }
                        else if (TextUtils.isEmpty(password)){
                            mPassword.setError("");
                            Toast.makeText(getContext(),"Please chose password",Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(confPass)) {
                            mConfirmPassword.setError("");
                            Toast.makeText(getContext(),"Please confirm password",Toast.LENGTH_SHORT).show();
                        }
                        else if ( !password.equals(confPass)){
                            mPassword.setError("");
                            mConfirmPassword.setError("");
                            Toast.makeText(getContext(),"Error: MUST match Password",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            loadingBar.setTitle("Creating account...");
                            loadingBar.setMessage("Please wait while your account is being created...");
                            loadingBar.setIcon(R.drawable.chefood);
                            loadingBar.show();
                            loadingBar.setCanceledOnTouchOutside(false);
            //if all are fine, then try to create a user
                            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // if success
                                    if (task.isSuccessful()){
                                        loadingBar.dismiss();
                                        Toast.makeText(getContext(), R.string.registration_success, Toast.LENGTH_SHORT).show();
                                        sendVerifyEmail();
                                        mAuth.signOut();

                                        new Handler().postDelayed(() -> goToLogin(), Toast.LENGTH_SHORT);//

                                    }else {
                                        loadingBar.dismiss();
                                        Toast.makeText(getContext(),"Error: " + task.getException().getMessage() ,Toast.LENGTH_LONG).show();
                                        mAuth.signOut(); // always sign out the user if something goes wrong
                                    }
                                }
                            });
                        }
    }
// verification email
    private void sendVerifyEmail(){

        FirebaseUser user = mAuth.getCurrentUser();// check user
        if (user != null){

            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                            mAuth.signOut();// need to sign out the user every time until he confirms email
                        }
                        else {
                            String error = task.getException().getMessage();// get error from fireBase
                            Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                            mAuth.signOut();// need to sign out the user every time until he confirms email
                    }

                }
            });
        }
    }
// send user to login and erase fragment history
    private void goToLogin(){
        startActivity(new Intent(getActivity(),LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
        Objects.requireNonNull(getActivity()).finish();
 }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    }


