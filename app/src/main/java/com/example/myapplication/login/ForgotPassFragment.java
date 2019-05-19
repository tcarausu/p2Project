package com.example.myapplication.login;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;


public class ForgotPassFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ForgotPassFragment";

    private FirebaseAuth mAuth;
    private EditText emailField;
    private Button sendPassRequest, goBack;

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

        View v = inflater.inflate(R.layout.fragment_forgot_pass, container, false);
        findWidgets(v);
        goBack.setOnClickListener(this);
        sendPassRequest.setOnClickListener(this);

        return v;
    }

    private void findWidgets(View v){

        emailField = v.findViewById(R.id.email_field);
        goBack = v.findViewById(R.id.back_button);
        sendPassRequest = v.findViewById(R.id.sendPassRequest);


    }

    // sending the mail to user to reset pass
    private void sendPassResetMail() {

        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Required.");
            Toast.makeText(getContext(), "Please type a valid email", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Please check your inbox, we sent you a change password link", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                } else
                    Toast.makeText(getContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            });

        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back_button:
                getActivity().finish();


                break;

            case R.id.sendPassRequest:
                sendPassResetMail();
                break;
        }
    }

    public interface OnFragmentInteractionListener {

    }
}
