package com.example.myapplication.history_log;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.models.User;
import com.example.myapplication.utility_classes.BottomNavigationViewHelper;
import com.example.myapplication.utility_classes.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

public class HistoryLogActivity extends AppCompatActivity {
    private static final String TAG = "HistoryLogActivity";
    private static final int ACTIVITY_NUM = 3;


    private RecyclerView mRecyclerView;
    private RecyclerViewAdapterHistoryLogItems mAdapter; // Is the bridge between our recyclerview and our arraylist. Shows only the necessary data from the arraylist
    private RecyclerView.LayoutManager mLayoutManager;

    private DatabaseReference mPostReference, mCurrentUserReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseStorage mFirebaseStorage;

    private ArrayList<HistoryLogPostItem> mListOfPosts;
    private ArrayList<User> mUsers;
    private String mCurrentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_log);
        connectToDatabase();
        setupBottomNavigationView();
        getCurrentUserPosts();
        buildRecyclerView();
    }


    /**
     * Bottom Navigation View setup
     */
    private void setupBottomNavigationView() {
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigationBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(getApplicationContext(), bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


    /**
     * Setting Database connection and manipulating nodes
     */

    // Connection to user's posts node
    private void connectToDatabase() {
        mAuth = FirebaseMethods.getAuth();
        mCurrentUserId = mAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseMethods.getFirebaseStorage();
        // Setting the reference to posts branch
        mPostReference = firebaseDatabase.getReference("posts");
    }

    private void getCurrentUserPosts() {
        mListOfPosts = new ArrayList<>();
        // Getting the user ID branch inside posts main node
        Query query = mPostReference.child(mCurrentUserId);

        // Getting each post branch with its contents
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mListOfPosts.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    HistoryLogPostItem post = postSnapshot.getValue(HistoryLogPostItem.class);
                    mCurrentUserReference = firebaseDatabase.getReference("users/" + mCurrentUserId);
                    mCurrentUserReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            final User user = dataSnapshot.getValue(User.class);
                            post.setUser(user);
                            mListOfPosts.add(post);

                            // Checking if we got all the items from the database so we can
                            // reverse the order of the postlist and pass it in the recycler view
                            Log.d(TAG, "DataSnapshot Count  " + postSnapshot.getChildrenCount());

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Setting up recycler view
     */
    private void buildRecyclerView() {

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        // Declare how ViewHolder objects are going to be displayed inside adapter
        mLayoutManager = new LinearLayoutManager(HistoryLogActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Create an adapter and pass it a list of data, from which
        // the ViewHolder objects will be created and managed by this adapter
        mAdapter = new RecyclerViewAdapterHistoryLogItems(mListOfPosts);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnRecyclerItemClickListener(HistoryLogActivity.this, position -> {
            highlightViewItem(position, true);
            alertDialogDelete(position);
        });

    }

    /**
     * Alert Dialog:
     * Asking the user if he wants to delete an activity from his history log
     */
    private void alertDialogDelete(final int position) {
        View layoutView = getLayoutInflater().inflate(R.layout.alert_dialog_history_log, null);

        Button cancelButton = layoutView.findViewById(R.id.alertButtonCancel);
        Button deleteButton = layoutView.findViewById(R.id.alertButtonDelete);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(layoutView);

        final AlertDialog alertDialog = dialogBuilder.create();
        WindowManager.LayoutParams wlp = alertDialog.getWindow().getAttributes();

        wlp.windowAnimations = R.style.AlertDialogAnimation;
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        alertDialog.getWindow().setAttributes(wlp);
        alertDialog.setCanceledOnTouchOutside(false);
        // Setting transparent the background (layout) of alert dialog
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();


        deleteButton.setOnClickListener(v -> {
            alertDialog.dismiss();
            highlightViewItem(position, false);
            onClickRemoveItem(position);
        });

        cancelButton.setOnClickListener(v -> {
            alertDialog.dismiss();
            highlightViewItem(position, false);
        });
    }
    // Alert Dialog closes

    /**
     * Method to highlight the background of the view holder item.
     * Used when clicking show more button
     */
    private void highlightViewItem(int position, boolean setBoolean) {
        mListOfPosts.get(position).setHighlighted(setBoolean);
        mAdapter.notifyItemChanged(position);
    }

    private void onClickRemoveItem(int position) {
        // Important!!! First deleting position in the database and after that the position in our current list
        deleteUserDatabasePost(position);
    }

    private void deleteUserDatabasePost(int position) {
        // Delete selected post from the database

        StorageReference imageRef = mFirebaseStorage.getReferenceFromUrl(mListOfPosts.get(position).getmFoodImgUrl());
        imageRef.delete().addOnSuccessListener(aVoid -> {
            mPostReference.removeValue();
            mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        final User user = dataSnapshot.getValue(User.class);
                        if (user.getNrOfPosts() != 0) {
                            user.setNrPosts(user.getNrOfPosts() - 1);
                            mPostReference.setValue(user);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });

    }

}
