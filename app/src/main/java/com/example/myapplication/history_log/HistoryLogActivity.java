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
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.myapplication.R;
import com.example.myapplication.models.Photo;
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
    private CustomRecyclerViewAdapter mAdapter; // Is the bridge between our recyclerview and our arraylist. Shows only the necessary data from the arraylist
    private RecyclerView.LayoutManager mLayoutManager;

    private Button mButtonRemove, mButtonAdd;
    private EditText mItemPosition;
    private ProgressBar mProgressBar;

    private DatabaseReference mReference;
    private FirebaseAuth mAuth;

    private ArrayList<CardViewItem> mListOfItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_log);
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference("posts");

        initLayout();
        buttonListeners();
        setupBottomNavigationView();

        createListItems();
        buildRecyclerView();
        setButtons();


        /**
         * Databaase Connection
         */
//        final ArrayList<Post> posts = new ArrayList<>();

        Query query = mReference.child(mAuth.getCurrentUser().getUid());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
//                    Post post = postSnapshot.getValue(Post.class);
//                    System.out.println("\n\n"+postSnapshot.getKey()+" "+post.getDescription());
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        System.out.println("Current User: " + mAuth.getCurrentUser().getUid());
    }


    public void initLayout() {
        mContext = HistoryLogActivity.this;

    }

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
        mAdapter = new CustomRecyclerViewAdapter(mListOfItems);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnRecyclerItemClickListener(HistoryLogActivity.this, new CustomRecyclerViewAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onRecyclerCardviewClicked(int position) {
                mListOfItems.get(position).changeLineText("CLICKED");
                mItemPosition.setText("" + position);
                mAdapter.notifyItemChanged(position);
            }

            @Override
            public void onMoreDotsClicked(int position) {
                highlightViewItem(position, true);
                alertDialogDelete(position);
            }
        });
    }

    /**
     * Alert dialog popup
     */
    private void alertDialogDelete(final int position) {
        View layoutView = getLayoutInflater().inflate(R.layout.alert_dialog, null);

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
        mListOfItems.get(position).setHighlighted(setBoolean);
        mAdapter.notifyItemChanged(position);
    }

    private void onClickRemoveItem(int position) {
        mListOfItems.remove(position);
        // Updating the recycler view with animation
        mAdapter.notifyItemRemoved(position);
    }

    private void onClickAddItem(int position) {
        mListOfItems.add(position, new CardViewItem(R.drawable.ic_action_eye_open, "New item in position:", "" + position));
        mAdapter.notifyItemInserted(position);
    }


    /**
     * Setting up the buttons of the main view
     */
    private void setButtons() {
        mProgressBar = findViewById(R.id.progress_bar);
        mItemPosition = findViewById(R.id.item_position);

        mButtonRemove = findViewById(R.id.button_remove);
        mButtonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = Integer.parseInt(mItemPosition.getText().toString());
                onClickRemoveItem(itemPosition);
            }
        });

        mButtonAdd = findViewById(R.id.button_add);
        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = Integer.parseInt(mItemPosition.getText().toString());
                onClickAddItem(itemPosition);
            }
        });
    }

    /**
     * Create List of card view items
     */
    public void createListItems() {
        mListOfItems = new ArrayList<>();

        int selector = 1;
        int drawableIcon = 0;
        for (int i = 0; i < 30; i++) {
            switch (selector) {
                case 1:
                    drawableIcon = R.mipmap.ic_launcher_round;
                    break;
                case 2:
                    drawableIcon = R.mipmap.ic_launcher_round;
                    break;
                case 3:
                    drawableIcon = R.mipmap.ic_launcher_round;
                    break;
            }
            selector++;
            if (selector == 4) {
                selector = 1;
            }
            mListOfItems.add(new CardViewItem(drawableIcon, "Line" + i, "Subline" + i));
        }
    }

}
