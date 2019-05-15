package com.example.myapplication.history_log;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.myapplication.R;
//import com.example.myapplication.models.Post;
import com.example.myapplication.utility_classes.BottomNavigationViewHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

public class HistoryLogActivity extends AppCompatActivity {
    private static final String TAG = "HistoryLogActivity";
    private static final int ACTIVITY_NUM = 3;

    private Context mContext;

    private RecyclerView mRecyclerView;
    private HistoryLogRecyclerViewAdapter mAdapter; // Is the bridge between our recyclerview and our arraylist. Shows only the necessary data from the arraylist
    private RecyclerView.LayoutManager mLayoutManager;

    private DatabaseReference mReference;
    private FirebaseAuth mAuth;

    private ArrayList<HistoryLogPostItem> mListOfPosts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_log);

        initLayout();
        buttonListeners();
        setupBottomNavigationView();
        connectToDatabase();
        getDatabasePosts();
        buildRecyclerView();
    }


    public void initLayout() {
        mContext = HistoryLogActivity.this;
    }

    /**
     * Setting up the buttons of the main view
     */
    public void buttonListeners() {

    }

    /**
     * @param user is the Firebase User used to adjust/perform info exchange
     */
    private void updateUI(FirebaseUser user) {
        if (user != null) {
        } else {

        }
    }

    /**
     * Bottom Navigation View setup
     */
    public void setupBottomNavigationView() {
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigationBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
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
        mAdapter = new HistoryLogRecyclerViewAdapter(mListOfPosts);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnRecyclerItemClickListener(HistoryLogActivity.this, new HistoryLogRecyclerViewAdapter.OnRecyclerItemClickListener() {
//            @Override
//            public void onRecyclerCardviewClicked(int position) {
//                mAdapter.notifyItemChanged(position);
//            }

            @Override
            public void onMoreDotsClicked(int position) {
                highlightViewItem(position, true);
                alertDialogDelete(position);
            }
        });
    }

    /**
     * Alert Dialog: Asking the user if he wants to delete an activity from his history log
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


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                highlightViewItem(position, true);
                onClickRemoveItem(position);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                highlightViewItem(position, false);
            }
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
        mListOfPosts.remove(position);
        // Updating the recycler view with animation
        mAdapter.notifyItemRemoved(position);
    }

   /* private void onClickAddItem(int position) {
        mListOfItems.add(position, new HistoryLogPostItem(R.drawable.ic_action_eye_open, "New item in position:", "" + position));
        mAdapter.notifyItemInserted(position);
    }*/


   /**
    * Setting Database connection and manipulating nodes
    */

   // Connection to user's posts node
   private void connectToDatabase(){
       mAuth = FirebaseAuth.getInstance();
       // Setting the reference to posts branch
       mReference = FirebaseDatabase.getInstance().getReference("posts");
   }

   private void getDatabasePosts(){

       mListOfPosts = new ArrayList<>();

       // Getting the user ID branch inside posts
       Query query = mReference.child(mAuth.getCurrentUser().getUid());

       // Getting each post branch with its contents
       query.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    HistoryLogPostItem post = postSnapshot.getValue(HistoryLogPostItem.class);
                    mListOfPosts.add(post);

//                    System.out.println("\n\n"+postSnapshot.getKey()+" "+post.getmDescription());
                }
               System.out.println("\nList Size: "+mListOfPosts.size());
               for(HistoryLogPostItem postitem : mListOfPosts){
                   System.out.println("User name: "+postitem.getmUsername()+ " Description: "+postitem.getmDescription()
                   + " Image: "+postitem.getmProfileImgUrl());
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {
               System.out.println("The read failed: " + databaseError.getCode());
           }
       });

       System.out.println("Current User: " + mAuth.getCurrentUser().getUid());
   }

//   private void deleteDatabasePost(int position){
//       mListOfPosts.get(position).get...
//   }
}
