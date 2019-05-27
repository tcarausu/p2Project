package com.example.myapplication.post;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.home.HomeActivity;
import com.example.myapplication.models.Post;
import com.example.myapplication.models.User;
import com.example.myapplication.utility_classes.FirebaseMethods;
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

    private EditText mImageDesc,mImageIngredients,mImageRecipe;
    private TextView mUploadTextView;
    private ImageView mImageViewFood,mBackImageView;
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

        firebaseMethods = FirebaseMethods.getInstance(getApplicationContext());
        mAuth = FirebaseMethods.getAuth();
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
        mBackImageView = findViewById(R.id.close_share);

        imageUri = getIntent().getStringExtra("imageUri");
        Glide.with(getApplicationContext()).load(imageUri).fitCenter().into(mImageViewFood);
        mUploadTextView.setOnClickListener(this);
        mBackImageView.setOnClickListener(this);

    }


    // method for uploading image and image content to firebase storage and database
    private void uploadImage() {
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
                            String description = mImageDesc.getText().toString();
                            String ingredients = mImageIngredients.getText().toString();
                            String recipe = mImageRecipe.getText().toString();

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
                                firebaseMethods.goToWhereverWithFlags(getApplicationContext(), getApplicationContext(), HomeActivity.class);
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

        final CharSequence[] options = {"Yeah", "Nope", "CANCEL"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("There is no descriptions for your post, continue?");
        builder.setIcon(R.drawable.chefood);
        builder.setItems(options, (dialog, which) -> {

            if (options[which].equals("Yeah")) {
                uploadImage();
                dialog.dismiss();

            } else if (options[which].equals("Nope")) {
                dialog.dismiss();
            }

        });
        builder.show();

    }

    /**
     * created byMo.MSaad
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

        final CharSequence[] options = {"Mobile data", "WIFI", "CANCEL"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select network to proceed");
        builder.setIcon(R.drawable.chefood);
        builder.setItems(options, (dialog, which) -> {

            if (options[which].equals("Mobile data")) {
                uploadImage();

            } else if (options[which].equals("WIFI")) {
                Intent wifiIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(wifiIntent);

            } else if (options[which].equals("CANCEL")) {
                dialog.dismiss();
            }

        });
        builder.show();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.textview_share:
                if (mImageIngredients.getText().toString().equals("")
                        || mImageDesc.getText().toString().equals("")
                        || mImageRecipe.getText().toString().equals("")) {
                    dialogConfirm();
                } else {
                    checkWifiState();
                }

                break;

            case R.id.close_share:
                Log.d(TAG, "onClick: back button working");
                finish();

                break;
        }

    }
}
