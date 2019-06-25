package com.example.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

/**
 * File created by tcarau18
 **/
public class Like  implements Parcelable {

    private String user_id;

    public Like(String user_id) {
        this.user_id = user_id;
    }

    public Like() {}


    protected Like(Parcel in) {
        user_id = in.readString();
    }

    public String getUser_id() {
        return user_id;
    }
    @Exclude
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Like{" +
                "user_id='" + user_id + '\'' +
                '}';
    }

    public static final Creator<Like> CREATOR = new Creator<Like>() {
        @Override
        public Like createFromParcel(Parcel in) {
            return new Like(in);
        }

        @Override
        public Like[] newArray(int size) {
            return new Like[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_id);
    }
}
