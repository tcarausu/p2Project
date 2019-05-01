package com.example.myapplication.models;

/**
 * File created by tcarau18
 **/
public class UserSettings {

//    private User user;
    private User settings;

    public UserSettings( User settings) {
//        this.user = user;
        this.settings = settings;
    }

    public UserSettings() {

    }

//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }

    public User getSettings() {
        return settings;
    }

    public void setSettings(User settings) {
        this.settings = settings;
    }

    @Override
    public String toString() {
        return "UserSettings{" +
//                "user=" + user +
                ", settings=" + settings +
                '}';
    }

}
