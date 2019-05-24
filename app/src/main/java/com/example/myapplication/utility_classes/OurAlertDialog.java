package com.example.myapplication.utility_classes;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.example.myapplication.R;

public class OurAlertDialog extends android.support.v7.app.AlertDialog {

    private AlertDialog.Builder  mBuilder ;

    public OurAlertDialog(@NonNull Context context, AlertDialog.Builder builder) {
        super(context);
        mBuilder = builder;
    }

    public void setTitle(String s){
        mBuilder.setTitle(s);
    }

    public void setIcone(int drawable){
        mBuilder.setIcon(drawable);
    }

    public void show(){
        mBuilder.show();
    }

    public void setView(View view){
        mBuilder.setView(view);
    }

    public void setCanceledOnTouchOutside(boolean b){
        mBuilder.setCancelable(b);
    }

    public void setItem(CharSequence charSequence, OnClickListener listener){

    }

    public void dismiss(){
        this.dismiss();
    }

    public void setMessaage(CharSequence charSequence){
        mBuilder.setMessage(charSequence);
    }

    public static class MyView extends View {

        public MyView(Context context) {
            super(context);
            init(null);
        }

        public MyView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(attrs);
        }

        public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init(attrs );
        }

        private void init(@NonNull AttributeSet set){}

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawColor(getResources().getColor(R.color.orange));

        }
    }



}
