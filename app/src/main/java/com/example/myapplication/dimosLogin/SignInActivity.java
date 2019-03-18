package com.example.myapplication.dimosLogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.R;

public class SignInActivity extends AppCompatActivity {

    private EditText email, password;
    private TextView signUp;
    private Button logIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        email = findViewById(R.id.email_id_logIn);
        password = findViewById(R.id.password_id_logIn);

        logIn = findViewById(R.id.button_id_logIn);
        signUp = findViewById(R.id.textView_id_register);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
