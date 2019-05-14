package com.example.myapplication.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;

import java.util.Objects;

/**
 * File created by tcarau18
 **/
public class CameraFragment extends Fragment {
    private static final String TAG = "CameraFragment";
    private static final int REQUEST_CAMERA = 11;
    private   int batteryLevel;

    private  int getBatteryLevel() {
        return batteryLevel;
    }

    private  void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setBatteryLevel(intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1));

        }
    };



    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        getActivity().registerReceiver(this.broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
//        openCamera();
        return view;
    }

    private void openCamera() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if ( getBatteryLevel() > 10 && cameraIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
            Log.d(TAG, "takePicture1: battery level: "+getBatteryLevel());
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } else Toast.makeText(getActivity(), "Battery is low...", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }
}
