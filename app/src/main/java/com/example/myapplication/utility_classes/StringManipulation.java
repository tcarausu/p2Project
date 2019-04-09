package com.example.myapplication.utility_classes;

/**
 * File created by tcarau18
 **/
public class StringManipulation {

    public static String expandUserName(String username) {
        return username.replace(".", " ");
    }

    public static String condenseUserName(String username) {
        return username.replace(" ", ".");
    }

}
