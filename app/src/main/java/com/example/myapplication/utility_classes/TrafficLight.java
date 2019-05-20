//package com.example.myapplication.utility_classes;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.os.BatteryManager;
//import android.provider.MediaStore;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.widget.Toast;
//
//import com.example.myapplication.R;
//
//import java.util.Objects;
//
//public class TrafficLight extends AppCompatActivity a{
//    public static final String TAG = "TrafficLight";
//    public int REQUEST_IMAGE_GALLERY = 22;
//    public int REQUEST_CAMERA = 11;
//
//
//
//
//    public TrafficLight() {}
//
//    public void goToWithFlags(Context context, Class<? extends AppCompatActivity> cl){
//        startActivity(new Intent(context,cl).addFlags(
//                Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
//        this.finish();
//    }
//
//    public void goTos(Context context, Class<? extends AppCompatActivity> cl){
//        startActivity(new Intent(context,cl));
//        this.finish();
//    }
//
////    public void dialogChoice(Context context) {
////
////        final CharSequence[] options = {"CAMERA", "GALLERY", "CANCEL"};
////        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
////        builder.setTitle("Add Image");
////        builder.setIcon(R.drawable.chefood);
////        builder.setItems(options, (dialog, which) -> {
////            if (options[which].equals("CAMERA")) {
////                takePicture(context);
////
////            } else if (options[which].equals("GALLERY")) {
////                selectPicture();
////            } else if (options[which].equals("CANCEL")) {
////                dialog.dismiss();
////            }
////
////        });
////        builder.show();
////    }
//
////    public void takePicture(Context context) {
////        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////        if (getBatteryLevel() < 10 && cameraIntent.resolveActivity(Objects.requireNonNull(context).getPackageManager()) != null) {
////            startActivityForResult(cameraIntent, REQUEST_CAMERA);
////        } else Toast.makeText(context, "Battery is low...", Toast.LENGTH_SHORT).show();
////
////    }
//
//    public void selectPicture() {
//        Intent galleryIntent = new Intent();
//        galleryIntent.setType("image/*");
//        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), REQUEST_IMAGE_GALLERY);
//    }
//}
