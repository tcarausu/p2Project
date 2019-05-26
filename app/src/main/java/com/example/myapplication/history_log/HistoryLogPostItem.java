package com.example.myapplication.history_log;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.View;

import com.example.myapplication.models.Post;

/**
 * Item that keeps the data for the creation of the view items inside recycler view.
 * It defines methods not contained inside the Post class
 */
@SuppressLint("ParcelCreator")
public class HistoryLogPostItem extends Post {

    // Default Values
    private boolean mIsHighlited = false;
    private int mTextColor = Color.BLACK;
    private int mHintColor = Color.GRAY;
    private int mMoreDotsVisibility = View.VISIBLE;

    // The mIsHighlighted variable checks whether to change or not the background of the specific object.
    // In the onBindViewHolder method of the RecyclerViewAdapterHistoryLogItems class, we are feeding this variable
    // to set the selector background of the card view item.
    public void setHighlighted(boolean setBoolean) {
        mIsHighlited = setBoolean;
        if (mIsHighlited) {
            mTextColor = Color.WHITE;
            mHintColor = Color.WHITE;
            mMoreDotsVisibility = View.INVISIBLE;
        } else {
            mTextColor = Color.BLACK;
            mHintColor = Color.GRAY;
            mMoreDotsVisibility = View.VISIBLE;
        }
    }

    public boolean isHighlighted() {
        return mIsHighlited;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public int getHintColor() {
        return mHintColor;
    }

    public int getMoreDotsVisibility() {
        return mMoreDotsVisibility;
    }
}