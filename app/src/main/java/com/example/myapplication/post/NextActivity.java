package com.example.myapplication.post;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.login.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class NextActivity extends AppCompatActivity {

    private EditText mImageDesc;
    private EditText mImageIngredients;
    private EditText mImageRecipe;
    private ImageView mImageViewfood;
    private TextView mUploadTextView;
    private ImageView mBackImageView;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private Uri imageUri;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        mImageDesc = findViewById(R.id.image_desc_edittext);
        mImageIngredients = findViewById(R.id.image_ingredients_edittext);
        mImageRecipe = findViewById(R.id.image_recipe_edittext);
        mImageViewfood = findViewById(R.id.image_tobe_shared);
        mUploadTextView = findViewById(R.id.textview_share);
        mBackImageView = findViewById(R.id.close_share);



        Intent intent = getIntent();
        Bundle receivedBundle = intent.getExtras();
        byte [] array = receivedBundle.getByteArray("pic");
        Bitmap b = BitmapFactory.decodeByteArray(array,0,array.length);
        mImageViewfood.setRotation(90);
        mImageViewfood.setImageBitmap(b);
        imageUri = intent.getParcelableExtra("imageUri");
        mUploadTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NextActivity.this, "Back button working",
                        Toast.LENGTH_LONG).show();
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        String databasePath = "posts/"+ mAuth.getUid()+"/";
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(databasePath);

    }

    private void uploadImage() {
        Toast.makeText(NextActivity.this, "Uploading...",
                Toast.LENGTH_SHORT).show();
        StorageReference storageReference = mStorageRef.child("images/users/"+mAuth.getUid()+"/" + System.currentTimeMillis() + ".jpg");
        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(NextActivity.this, "Uploaded...",
                        Toast.LENGTH_SHORT).show();
                String description = mImageDesc.getText().toString();
                String ingredients = mImageIngredients.getText().toString();
                String recipe = mImageRecipe.getText().toString();
                PostInfo postInfo = new PostInfo(description,ingredients,recipe,taskSnapshot.getMetadata().
                        getReference().getDownloadUrl().toString());
                String uploadId = mDatabaseRef.push().getKey();
                mDatabaseRef.child(uploadId).setValue(postInfo);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NextActivity.this, "COULD NOT UPLOAD PICTURE! TRY AGAIN",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
