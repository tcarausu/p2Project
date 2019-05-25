package com.example.myapplication.utility_classes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public abstract class TrafficLight extends Context {
    public static final String TAG = "TrafficLight";
    private Context context ;

    public TrafficLight(Context context) {
        this.context = context;
    }

    public void goToWithFlags(Context context, Class<? extends AppCompatActivity> cl){
        startActivity(new Intent(context,cl).addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));

    }

    public void goTosWithFlags(Context context, Class<? extends AppCompatActivity> cl){
        startActivity(new Intent(context,cl));
    }

}
