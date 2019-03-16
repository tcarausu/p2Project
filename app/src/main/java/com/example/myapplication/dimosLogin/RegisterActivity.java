package com.example.myapplication.dimosLogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private final String TAG = "RegisterActivity";

    private EditText mEmail, mPass, mConfirmPass;
    private Button register;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmail = findViewById(R.id.email_id_register);
        mPass = findViewById(R.id.password_id_register);
        mConfirmPass = findViewById(R.id.password_id_registerRepeat);
        register = findViewById(R.id.button_id_register);

        String mUserEmail = mEmail.getText().toString();
        String mUserPass  = mPass.getText().toString();

        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(mUserEmail, mUserPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    // OnCreate closes



    private void createUser(){
        if(mEmail.getText().toString().equals("") || mPass.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
        }else if(!mEmail.getText().toString().contains("@")){
            Toast.makeText(getApplicationContext(), "Please insert a valid email address", Toast.LENGTH_SHORT).show();
        }else if(mEmail.getText().toString().contains(",")){
            Toast.makeText(getApplicationContext(), "Email can not contain the symbol \",\"", Toast.LENGTH_SHORT).show();
        }else{
            String mUserEmail = mEmail.getText().toString();
            String mUserPass = mPass.getText().toString();

            mAuth.createUserWithEmailAndPassword(mUserEmail, mUserPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Account created successfully",Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                    }else{
                        Toast.makeText(getApplicationContext(), "Account was not created", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(RegisterActivity.this, ProfileSetActivity.class);
                }
            });
        }
    }
}
