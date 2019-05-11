package com.example.myapplication.post;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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
import com.example.myapplication.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class NextActivity<breaks> extends AppCompatActivity {


    private static final String TAG = "NextActivity";

    private EditText mImageDesc;
    private EditText mImageIngredients;
    private EditText mImageRecipe;
    private ImageView mImageViewfood;
    private TextView mUploadTextView;
    private ImageView mBackImageView;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef , postRef;
    private DatabaseReference mDatabaseReferenceUserInfo;
    private FirebaseAuth mAuth;
    private FirebaseUser current_user ;
    private String imageUri;
    private String URL;
    private String username;
    private String profilePicUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        findWidgets();


        String URL = "";

        Intent intent = getIntent();
        imageUri = getIntent().getStringExtra("imageUri");
        Glide.with(getApplicationContext()).load(imageUri).fitCenter().into(mImageViewfood);
        mUploadTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: back button working");
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser() ;
        mStorageRef = FirebaseStorage.getInstance().getReference();
        String databasePath = "posts/" + mAuth.getUid() + "/";
        String databasePathPic = "users/" + mAuth.getUid();
        postRef = FirebaseDatabase.getInstance().getReference("posts").child(current_user.getUid());

        mDatabaseRef = FirebaseDatabase.getInstance().getReference(databasePath);

        mDatabaseReferenceUserInfo = FirebaseDatabase.getInstance().getReference(databasePathPic);

        // getting the username and profile picture link for current user
        mDatabaseReferenceUserInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                profilePicUrl = user.getProfile_photo();
                username = user.getUsername();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    // TODO push the new parameters from the input
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void findWidgets(){
        mImageDesc = findViewById(R.id.image_desc_edittext);
        mImageIngredients = findViewById(R.id.image_ingredients_edittext);
        mImageRecipe = findViewById(R.id.image_recipe_edittext);
        mImageViewfood = findViewById(R.id.image_tobe_shared);
        mUploadTextView = findViewById(R.id.textview_share);
        mBackImageView = findViewById(R.id.close_share);
    }

    // method for uploading image and image content to firebase storage and database
    private void uploadImage() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("uploading, please wait...");
        progressDialog.setIcon(R.drawable.chefood);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StorageReference storageReference = mStorageRef.child("post_pic/users/" + mAuth.getUid() + "/" + System.currentTimeMillis() + ".jpg");
        storageReference.putFile(Uri.parse(imageUri)).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                progressDialog.dismiss();
                storageReference.getDownloadUrl().addOnCompleteListener(task1 -> {
                    URL = task1.getResult().toString();
                    Log.d(TAG, "onComplete: post URL: "+URL);
                    String description = mImageDesc.getText().toString();
                    String ingredients = mImageIngredients.getText().toString();
                    String recipe = mImageRecipe.getText().toString();
                    Post postInfo = new Post(profilePicUrl,username,description, URL, 0, recipe, ingredients, mAuth.getUid());
                    Log.d(TAG, "onComplete: " + profilePicUrl + " " + username);

                    String uploadId = mDatabaseRef.push().getKey();
                    mDatabaseRef.child(uploadId).setValue(postInfo);

                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        Intent intent = new Intent(NextActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }, 500);

                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(NextActivity.this, "COULD NOT UPLOAD PICTURE! TRY AGAIN",
                            Toast.LENGTH_SHORT).show();
                });
            }
        }).addOnSuccessListener(taskSnapshot -> {
            progressDialog.dismiss();
            Toast.makeText(NextActivity.this, "Post added successfully.", Toast.LENGTH_SHORT).show();
        }).addOnProgressListener(taskSnapshot -> {
            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
            progressDialog.setMessage("uploaded " + (int) progress + "%");
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(NextActivity.this, "COULD NOT UPLOAD PICTURE! TRY AGAIN", Toast.LENGTH_SHORT).show();
        });
    }


}
