package com.example.myapplication.utility_classes;

import android.os.Environment;

/**
 *
 * Class to get the filepath when accessing phone storage
 *
 */

public class FilePath {


    public String ROOT_DIRECTORY = Environment.getExternalStorageDirectory().getPath();
    public String CAMERA_DIRECTORY = ROOT_DIRECTORY + "/DCIM/camera";
    public String PICTURES_DIRECTORY = ROOT_DIRECTORY + "/Pictures";


}
