package com.example.myapplication.utility_classes;

import android.Manifest;

/**
 * Class that will store the permissions in an array
 */

public class Permissions {
    public static final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    public static final String[] PERMISSIONS_CAMERA = {
            Manifest.permission.CAMERA
    };
}
