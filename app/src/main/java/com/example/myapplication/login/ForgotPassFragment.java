package com.example.myapplication.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ForgotPassFragment extends Fragment {
    private static final String TAG = "ForgotPassFragment";

    private FirebaseUser user ;
    private FirebaseAuth mAuth ;
    private EditText emailField ;
    private Button sendPassRequest ;
    private Button goBack ;

    private OnFragmentInteractionListener mListener;

    public ForgotPassFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();// always call mAuth here. this is the first method called
        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_forgot_pass,container,false);

        emailField = v.findViewById(R.id.Forgot_Pass_Frag_emailField_id);
        sendPassRequest = v.findViewById(R.id.Forgot_Pass_Frag_sendMailButton_id);
        goBack = v.findViewById(R.id.ForgotPassFragment_goBackButton_id);

        // listeners
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });
        sendPassRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPassResetMail();
            }
        });

        return v;
    }
    // sending the mail to user to reset pass
    private void sendPassResetMail(){

        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email)){
            emailField.setError("");
            Toast.makeText(getContext(),"Please type a valid email",Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getContext(),"Please check your inbox, we sent you a change password link",Toast.LENGTH_SHORT).show();
                        goToLogin();
                    }
                    else
                        Toast.makeText(getContext(),"Error: "+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void goToLogin() {
       startActivity(new Intent(getActivity(),LoginActivity.class).
               addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
       getActivity().finish();
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
        void onFragmentInteraction(Uri uri);
    }
}

