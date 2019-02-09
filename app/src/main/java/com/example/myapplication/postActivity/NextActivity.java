package com.example.myapplication.postActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.home.HomeActivity;
import com.example.myapplication.models.Post;
import com.example.myapplication.models.User;
import com.example.myapplication.utility_classes.FirebaseMethods;
import com.example.myapplication.utility_classes.OurAlertDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class NextActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "NextActivity";

    private EditText mImageDesc, mImageIngredients, mImageRecipe;
    private TextView mUploadTextView;
    private ImageView mImageViewFood, mBackImageView;
    private FirebaseDatabase mFirebaseDatabase;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef, postRef, userRef;
    private DatabaseReference mDatabaseReferenceUserInfo;
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private String imageUri, URL, uploadId;
    private FirebaseMethods firebaseMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        findWidgets();
        connectFirebase();

        mUploadTextView.setOnClickListener(this);
        mBackImageView.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        firebaseMethods.autoDisctonnec(getApplicationContext());
    }

    private void connectFirebase() {
        firebaseMethods = FirebaseMethods.getInstance(getApplicationContext());
        mAuth = FirebaseMethods.getAuth();
        firebaseMethods.autoDisctonnec(getApplicationContext());
        current_user = mAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseMethods.getmFirebaseDatabase();
        mStorageRef = FirebaseMethods.getFirebaseStorage().getReference();
        String databasePath = "posts" + "/" + mAuth.getUid() + "/";
        String databasePathPic = "users" + "/" + mAuth.getUid();
        postRef = FirebaseDatabase.getInstance().getReference(getString(R.string.dbname_posts)).child(current_user.getUid());
        userRef = FirebaseDatabase.getInstance().getReference(getString(R.string.dbname_users)).child(current_user.getUid());
        mDatabaseRef = mFirebaseDatabase.getReference(databasePath);
        mDatabaseReferenceUserInfo = mFirebaseDatabase.getReference(databasePathPic);
    }


    private void findWidgets() {

        mImageDesc = findViewById(R.id.image_desc_edittext);
        mImageIngredients = findViewById(R.id.image_ingredients_edittext);
        mImageRecipe = findViewById(R.id.image_recipe_edittext);
        mImageViewFood = findViewById(R.id.image_tobe_shared);
        mUploadTextView = findViewById(R.id.textview_share);
        mBackImageView = findViewById(R.id.close_share_nextActivity);
        imageUri = getIntent().getStringExtra("imageUri");
        Glide.with(getApplicationContext()).load(imageUri).fitCenter().into(mImageViewFood);
    }


    // method for uploading image and image content to firebase storage and database
    private void uploadImage() {
        final String description = mImageDesc.getText().toString();
        final String ingredients = mImageIngredients.getText().toString();
        final String recipe = mImageRecipe.getText().toString();


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.setIcon(R.drawable.chefood);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        uploadId = firebaseMethods.getTimestamp() + mDatabaseRef.push().getKey();
        StorageReference storageReference = mStorageRef.child("post_pic/users/" + mAuth.getUid() + "/" + uploadId + ".jpg");

        storageReference.putFile(Uri.parse(imageUri)).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        storageReference.getDownloadUrl().addOnCompleteListener(task1 -> {
                            URL = task1.getResult().toString();
                            Log.d(TAG, "onComplete: post URL: " + URL);


                            Post postInfo = new Post(description,
                                    URL, recipe, ingredients, mAuth.getUid(),
                                    uploadId, firebaseMethods.getTimestamp(), null, null);
                            Log.d(TAG, "onComplete: upload uid: " + uploadId);

                            mDatabaseRef.child(uploadId).setValue(postInfo);
                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    User currentUser = dataSnapshot.getValue(User.class);
                                    currentUser.setNrPosts(currentUser.getNrOfPosts() + 1);
                                    userRef.setValue(currentUser);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            Toast.makeText(NextActivity.this, "Uploaded...", Toast.LENGTH_SHORT).show();
                            Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                Intent intent = new Intent(NextActivity.this, HomeActivity.class);
                                startActivity(intent);
                                firebaseMethods.goToWhereverWithFlags(getApplicationContext(), HomeActivity.class);
                                overridePendingTransition(R.anim.left_enter, R.anim.left_out);
                            }, 500);
                        }).addOnFailureListener(e ->
                                Toast.makeText(NextActivity.this, "Could not Upload the picture", Toast.LENGTH_SHORT).show());
                    }
                }
        ).addOnSuccessListener(taskSnapshot -> {

            progressDialog.dismiss();
            Toast.makeText(NextActivity.this, "Post added successfully.", Toast.LENGTH_SHORT).show();
        }).addOnCanceledListener(() -> {

                }
        ).addOnProgressListener(taskSnapshot ->
        {
            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
            progressDialog.setMessage("uploaded " + (int) progress + "%");

        }).addOnFailureListener(e ->
                Toast.makeText(NextActivity.this, "Could not Upload the picture", Toast.LENGTH_SHORT).show());

    }

    /**
     * Method which will show a AlertDialog if editTextFields are empty while uploading
     * Will ask if user wants to upload the image despite empty fields
     */
    protected void dialogConfirm() {
        View layoutView = getLayoutInflater().inflate(R.layout.dialog_addpost_layout, null);
        ImageButton cameraButton = layoutView.findViewById(R.id.SENDANYWAY);
        ImageButton cancelButton = layoutView.findViewById(R.id.CANCEL_SENDANYWAY);
        OurAlertDialog.Builder myDialogBuilder = new OurAlertDialog.Builder(this);
        myDialogBuilder.setView(layoutView);
        myDialogBuilder.setIcon(R.mipmap.chefood_icones);
        final AlertDialog alertDialog = myDialogBuilder.create();
        WindowManager.LayoutParams wlp = alertDialog.getWindow().getAttributes();
        wlp.windowAnimations = R.style.AlertDialogAnimation;
        wlp.gravity = Gravity.CENTER;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;

        alertDialog.getWindow().setAttributes(wlp);
        alertDialog.setCanceledOnTouchOutside(true);
        // Setting transparent the background (layout) of alert dialog
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        cameraButton.setOnClickListener(v -> {
            uploadImage();
            alertDialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            hideKeyboard();
            alertDialog.dismiss();
        });
    }

    /**
     * created byMo.Msaad
     * checks wifi or data, displays dialog in case of data. sends auto in case of wifi.
     **/
    private void checkWifiState() {

        boolean isWifiConnected;
        boolean isMobileDataConnected;
        ConnectivityManager connManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            isWifiConnected = activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            isMobileDataConnected = activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;

            if (isWifiConnected) {
                uploadImage();
            } else if (isMobileDataConnected) {
                openDialogChoice();
            }
        }
    }

    /**
     * created byMo.MSaad
     **/

    private void openDialogChoice() {


        View layoutView = getLayoutInflater().inflate(R.layout.dialog_wifi_check, null);
        Button wifiButton = layoutView.findViewById(R.id.WIFI);
        Button mobileDataButton = layoutView.findViewById(R.id.DATA);
        ImageButton cancelButton = layoutView.findViewById(R.id.CANCEL);

        OurAlertDialog.Builder myDialogBuilder = new OurAlertDialog.Builder(this);
        myDialogBuilder.setView(layoutView);
        myDialogBuilder.setIcon(R.mipmap.chefood_icones);
        final AlertDialog alertDialog = myDialogBuilder.create();
        WindowManager.LayoutParams wlp = alertDialog.getWindow().getAttributes();
        wlp.windowAnimations = R.style.AlertDialogAnimation;
        wlp.gravity = Gravity.CENTER;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;

        alertDialog.getWindow().setAttributes(wlp);
        alertDialog.setCanceledOnTouchOutside(true);
        // Setting transparent the background (layout) of alert dialog
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        wifiButton.setOnClickListener(v -> {
            uploadImage();
            alertDialog.dismiss();
        });
        mobileDataButton.setOnClickListener(v -> {
            checkWifiState();
            alertDialog.dismiss();
        });
        cancelButton.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

    }

    private void hideKeyboard() {

        View v = this.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {

        String ingredients = mImageIngredients.getText().toString();
        String descriptions = mImageDesc.getText().toString();
        String recipe = mImageRecipe.getText().toString();

        switch (v.getId()) {
            case R.id.textview_share:
                hideKeyboard();
                if (TextUtils.isEmpty(ingredients) || TextUtils.isEmpty(descriptions) || TextUtils.isEmpty(recipe))
                    dialogConfirm();

                else {
                    hideKeyboard();
                    checkWifiState();
                }
                break;

            case R.id.close_share_nextActivity:
                hideKeyboard();
                Log.d(TAG, "onClick: back button working");
                overridePendingTransition(R.anim.left_out, R.anim.right_enter);
                finish();
                break;
        }

    }
}
