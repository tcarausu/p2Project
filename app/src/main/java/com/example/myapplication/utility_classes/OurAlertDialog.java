package com.example.myapplication.utility_classes;

import android.app.AlertDialog;
import android.content.Context;

public class OurAlertDialog  {

    private AlertDialog.Builder  mBuilder ;

    public OurAlertDialog(Context context) {
        mBuilder = new AlertDialog.Builder(context);
    }


    public void setTitle(String s){
        mBuilder.setTitle(s);
    }

    public void setIcone(int drawable){
        mBuilder.setIcon(drawable);
    }



}
