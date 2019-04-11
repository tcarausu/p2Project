package com.example.myapplication.login;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.home.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SignupWithPhoneFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "SignupWithPhone";
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;


    public SignupWithPhoneFragment() {

    }

    private EditText phoneNumber, codeField;
    private Button sendCodeButton, confirmButton;
    private ProgressDialog loadingBar;


    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        loadingBar = new ProgressDialog(getContext());



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.signup_withphone_fragment, container, false);

        phoneNumber = v.findViewById(R.id.SignupWithPhoneFragment_phoneField);
        sendCodeButton = v.findViewById(R.id.SignupWithPhoneFragment_sendCodeButton);
        codeField = v.findViewById(R.id.SignupWithPhoneFragment_receivedCodeField);
        confirmButton = v.findViewById(R.id.SignupWithPhoneFragment_enterCodeButton);

        // send code button listener
        sendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createUSerWithPhone();
            }
        });
        // confirm code listener
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // just to be sure that the other unwanted fields are invisible
                phoneNumber.setVisibility(View.INVISIBLE);
                sendCodeButton.setVisibility(View.INVISIBLE);
                String verifCode =  codeField.getText().toString();// getting verification code from input

                if (TextUtils.isEmpty(verifCode)) {
                    Toast.makeText(getContext(),"Please enter your verification code",Toast.LENGTH_SHORT).show();

                }
                else {

                    loadingBar.setTitle("Code verification");
                    loadingBar.setMessage("Please wait while we verify your code.");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    // we allow to sign in with provided data,
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verifCode);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });


//this is process of the given phone validity
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                // if verification was successful, sign in the user with phone Auth credential
                Log.d(TAG, "onVerificationCompleted: " + phoneAuthCredential);
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // display the error
                Log.d(TAG, "onVerificationFailed:" + e.getMessage());
                // this executed only if there verification failed, we get our input back so the user can try again
                // set fields invisible. the ones we don't need ...
                phoneNumber.setVisibility(View.VISIBLE);
                sendCodeButton.setVisibility(View.VISIBLE);

                // // set fields visible. the ones we need
                codeField.setVisibility(View.INVISIBLE);
                confirmButton.setVisibility(View.INVISIBLE);
                loadingBar.dismiss();
                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                Log.d(TAG, "onCodeSent:" + s);
                // Save verification ID and resending token so we can use them later
                mVerificationId = s;
                mResendToken = forceResendingToken;

                // this executed only if there is no exception and the code was sent
                phoneNumber.setVisibility(View.INVISIBLE);
                sendCodeButton.setVisibility(View.INVISIBLE);
                codeField.setVisibility(View.VISIBLE);
                confirmButton.setVisibility(View.VISIBLE);
                loadingBar.dismiss();
                Toast.makeText(getContext(),"Verification is successful, a code has been sent...",Toast.LENGTH_SHORT).show();


            }
        };

        return v;
    }

    private void createUSerWithPhone() {

        String phone = phoneNumber.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            phoneNumber.setError("");
            Toast.makeText(getContext(), "Please enter a valid phone number first. ", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Phone verification");
            loadingBar.setMessage("Please wait while we verify your number.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            PhoneAuthProvider.getInstance().verifyPhoneNumber(phone,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    getActivity(),               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks
        }
    }

    //this method is generated by Fire-base, to sign in with phone number
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            loadingBar.dismiss();
                            Toast.makeText(getActivity(),"Congratulations, you are logged in successfully...",Toast.LENGTH_SHORT).show();
                            FirebaseUser user = task.getResult().getUser();
                            goToMain();


                        }
                        else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getActivity(),"Error: "+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();

//                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                Toast.makeText(getActivity(),"Invalid code, check and try again",Toast.LENGTH_SHORT).show();
//                            }
                        }
                    }
                });
    }

    private void goToMain() {
        startActivity(new Intent(getActivity(), HomeActivity.class).
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
        getActivity().finish();

    }
}

