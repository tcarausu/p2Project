package com.example.myapplication.utility_classes;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

public class OurAlertDialog extends android.support.v7.app.AlertDialog {

    private AlertDialog.Builder  mBuilder ;

    public OurAlertDialog(@NonNull Context context, AlertDialog.Builder builder) {

        super(context);
        mBuilder = builder;
    }

    @Override
    public ListView getListView() {
        return super.getListView();
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
    }

    @Override
    public void setMessage(CharSequence message) {
        super.setMessage(message);
    }

    @Override
    public void setView(View view) {
        super.setView(view);
    }

    @Override
    public void setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom) {
        super.setView(view, viewSpacingLeft, viewSpacingTop, viewSpacingRight, viewSpacingBottom);
    }

    @Override
    public void setIcon(int resId) {
        super.setIcon(resId);
    }

    @Override
    public void setIcon(Drawable icon) {
        super.setIcon(icon);
    }

    @Override
    public void setIconAttribute(int attrId) {
        super.setIconAttribute(attrId);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Nullable
    @Override
    public <T extends View> T findViewById(int id) {
        return super.findViewById(id);
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
    }

    @Override
    public void create() {
        super.create();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
