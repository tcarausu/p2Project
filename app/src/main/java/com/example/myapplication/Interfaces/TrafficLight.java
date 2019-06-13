package com.example.myapplication.Interfaces;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public interface TrafficLight  { //extends Transaction.Handler


    void goToWhereverWithFlags(Context currentContext, Class<? extends AppCompatActivity> targetClass);

    boolean isAuthNull(FirebaseAuth auth, FirebaseUser user);

    void checkDatabase(DatabaseReference ref);

    void addAuthLisntener();

    void removeAuthLisntener();

    void autoDisconnect(Context context);

    void loginChecker(Context context);

    void signOut();


}
