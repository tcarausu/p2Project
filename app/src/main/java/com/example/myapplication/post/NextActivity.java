package com.example.myapplication.post;

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
import com.example.myapplication.models.Post;
import com.example.myapplication.models.User;
import com.example.myapplication.utility_classes.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class NextActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "NextActivity";

    private EditText mImageDesc;
    private EditText mImageIngredients;
    private EditText mImageRecipe;
    private ImageView mImageViewfood;
    private TextView mUploadTextView;
    private ImageView mBackImageView;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseReferenceUserInfo;
    private FirebaseAuth mAuth;
    private String imageUri;
    private String URL;
    private String username;
    private String profilePicUrl;
    private FirebaseMethods firebaseMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        mAuth = FirebaseAuth.getInstance();
        firebaseMethods = new FirebaseMethods(getApplicationContext());

        mStorageRef = FirebaseStorage.getInstance().getReference();
        String databasePath = "posts/" + mAuth.getUid() + "/";
        String databasePathPic = "users/" + mAuth.getUid();
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

        initLayout();
    }

    private void initLayout() {

        mImageDesc = findViewById(R.id.image_desc_edittext);
        mImageIngredients = findViewById(R.id.image_ingredients_edittext);
        mImageRecipe = findViewById(R.id.image_recipe_edittext);
        mImageViewfood = findViewById(R.id.image_tobe_shared);
        mUploadTextView = findViewById(R.id.textview_share);
        mBackImageView = findViewById(R.id.close_share);

        imageUri = getIntent().getStringExtra("imageUri");
        Glide.with(getApplicationContext()).load(imageUri).fitCenter().into(mImageViewfood);

        mUploadTextView.setOnClickListener(this);
        mBackImageView.setOnClickListener(this);
    }

    // method for uploading image and image content to firebase storage and database
    private void uploadImage() {
        Toast.makeText(NextActivity.this, "Uploading...",
                Toast.LENGTH_SHORT).show();
        StorageReference storageReference = mStorageRef.child("post_pic/users/" + mAuth.getUid() + "/" + System.currentTimeMillis() + ".jpg");
        storageReference.putFile(Uri.parse(imageUri)).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                storageReference.getDownloadUrl().addOnCompleteListener(task1 -> {
                    URL = task1.getResult().toString();
                    String description = mImageDesc.getText().toString();
                    String ingredients = mImageIngredients.getText().toString();
                    String recipe = mImageRecipe.getText().toString();
                    String uploadId = mDatabaseRef.push().getKey();
                    Post postInfo = new Post(description,
                            URL, recipe, ingredients, mAuth.getUid(),
                            uploadId, firebaseMethods.getTimestamp(), null);
                    Log.d(TAG, "onComplete: " + profilePicUrl + " " + username);


                    mDatabaseRef.child(uploadId).setValue(postInfo);

                    Toast.makeText(NextActivity.this, "Uploaded...", Toast.LENGTH_SHORT).show();
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        Intent intent = new Intent(NextActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }, 500);
                }).addOnFailureListener(e -> Toast.makeText(NextActivity.this, "COULD NOT UPLOAD PICTURE! TRY AGAIN",
                        Toast.LENGTH_SHORT).show());
            }
        }).addOnSuccessListener(taskSnapshot -> {


        }).addOnFailureListener(e -> Toast.makeText(NextActivity.this, "COULD NOT UPLOAD PICTURE! TRY AGAIN",
                Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.textview_share:
                uploadImage();

                break;

            case R.id.close_share:
                Log.d(TAG, "onClick: back button working");
                finish();

                break;
        }
    }

}
