package com.example.myapplication.history_log;


import android.graphics.Color;

public class CardViewItem {

    private int mImageResource;
    private String mHeadLineText;
    private String mSubLineText;
    private String mMainText;

    // Default Values
    private boolean mIsHighlited = false;
    private int mTextColor = Color.BLACK;

    public CardViewItem(int imageResource, String headLineText, String subLineText){
        mImageResource = imageResource;
        mHeadLineText = headLineText;
        mSubLineText = subLineText;
        mMainText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
    }

    public void changeLineText(String text){
        mHeadLineText = text;
    }

    // The mIsHighlighted variable checks whether to change or not the background of the specific object.
    // In the onBindViewHolder method of the CustomRecyclerViewAdapter class, we are feeding this variable
    // to set the selector background of the card view item.
    public void setHighlighted(boolean setBoolean){
        mIsHighlited = setBoolean;
        if(mIsHighlited){
            mTextColor = Color.WHITE;
        }else {
            mTextColor = Color.BLACK;
        }
    }

    public boolean isHighlighted(){
        return mIsHighlited;
    }

    public int getTextColor(){
        return mTextColor;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public String getHeadLineText() {
        return mHeadLineText;
    }

    public String getSubLineText() {
        return mSubLineText;
    }

    public String getMainText(){
        return mMainText;
    }
}