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

    public static String getTags(String string) {
        if (string.indexOf("#") > 0) {
            StringBuilder sb = new StringBuilder();
            char[] charArry = string.toCharArray();
            boolean foundWord = false;
            for (char c : charArry) {
                if (c == '#') {
                    foundWord = true;
                    sb.append(c);
                } else {
                    if (foundWord) {
                        sb.append(c);
                    }
                }
                if (c == ' ') {
                    foundWord = false;
                }
            }
            String s = sb.toString().replace(" ", "").replace("#", ",#");
            return s.substring(1, s.length());
        }
        return string;
    }

    /*
        In-> some descriptions #tag1,#tag2
        out->  #tag1,#tag2
     */
}
