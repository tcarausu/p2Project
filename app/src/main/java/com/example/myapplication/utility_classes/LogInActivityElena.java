package com.example.myapplication.utility_classes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;

public class LogInActivityElena extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private Button mLogIn;
    private Button mSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginElana);
        mEmail = findViewById(R.id.email_id);
        mPassword = findViewById(R.id.password_id);
        mLogIn = findViewById(R.id.log_in_button_id);
        mSignUp = findViewById(R.id.sign_up_button1_id);


        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent secondActivity = new Intent(LogInActivityElena.this, RegisterActivity.class);
//                startActivity(secondActivity);
            }
        });

    }
}
