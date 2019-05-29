package com.example.myapplication.utility_classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.home.HomeActivity;
import com.example.myapplication.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Traffic extends Activity implements Runnable  {
    private Context mContext ;


    public Traffic(Context context) {
        mContext = context;
    }

    public void checkAuthLogin(FirebaseAuth auth, FirebaseUser user, R.anim enter,R.anim exit){
        if (auth != null || user != null){
            traffic(mContext, HomeActivity.class);
        }
        else{
            auth.signOut();
            traffic(mContext, LoginActivity.class);
            overridePendingTransition(enter.alert_dialog_slide_enter,exit.alert_dialog_slide_exit);

        }


    }


    public void traffic(Context requiredContext, Class<? extends AppCompatActivity> cl ){
        mContext.startActivity(new Intent(requiredContext,cl));
    }

    @Override
    public void run() {

    }
}
