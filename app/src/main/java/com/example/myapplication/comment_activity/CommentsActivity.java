package com.example.myapplication.comment_activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.home.HomeActivity;
import com.example.myapplication.models.Comment;
import com.example.myapplication.models.Post;
import com.example.myapplication.utility_classes.BottomNavigationViewHelper;
import com.example.myapplication.utility_classes.FirebaseMethods;
import com.example.myapplication.utility_classes.ListViewAdapter;
import com.example.myapplication.utility_classes.OurAlertDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommentsActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "CommentsActivity";
    private final int ACTIVITY_NUM1 = 1, ACTIVITY_NUM2 = 2, ACTIVITY_NUM3 = 3, ACTIVITY_NUM4 = 4;
    private final List<Integer> act = new ArrayList<>();

    //firebase
    private DatabaseReference mPostReference, commentRef, selectedCommentRef;
    private DatabaseReference mUserReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseMethods mFirebaseMethods;
    //view
    private ListViewAdapter mAdapter;
    private ListView listView;
    private EditText writeComment;
    private Button addComment;
    private ArrayList<Comment> commentsList;
    private FloatingActionButton fab;

    // Model data
    private Post currentPost;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        mFirebaseMethods = FirebaseMethods.getInstance(getApplicationContext());
        mAuth = FirebaseMethods.getAuth();
        currentUser = mAuth.getCurrentUser();
        mFirebaseMethods.autoDisctonnec(getApplicationContext());
        findWidgets();

        try {
            Bundle bundle = getIntent().getExtras();
            currentPost = bundle.getParcelable("currentPost");
            Log.d(TAG, "onCreate: currentPost: " + currentPost + "  " + "currentPost.getcommentLIst(): " + currentPost.getCommentList());
        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), "nothing attached", Toast.LENGTH_LONG).show();
            Log.e(TAG, "onCreate: error: " + e.getMessage());
        }
//       

        setButtonsListeners();
        setupBottomNavigationView();

        try {
            commentsList = (ArrayList<Comment>) currentPost.getCommentList();
            mAdapter = new ListViewAdapter(getApplicationContext(), commentsList);
            listView.setAdapter(mAdapter);
            displayComments();
            optionsButton();
        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), "No comments yet, Add one.", Toast.LENGTH_LONG).show();
        }


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                ProgressBar progressBar = findViewById(R.id.simo_progressBar);
//                View progreesssLayout = findViewById(R.id.simoProgressBar_layout);
//                progressBar.setVisibility(View.VISIBLE);
//                progreesssLayout.setVisibility(View.VISIBLE);
//                listView.removeAllViews();
//                listView.setAdapter(mAdapter);
//                mAdapter.notifyDataSetChanged();
//                progressBar.setVisibility(View.GONE);
//                progreesssLayout.setVisibility(View.GONE);

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseMethods.autoDisctonnec(getApplicationContext());
    }

    private void hideKeyboard() {
        View v = this.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }


    private void findWidgets() {

        listView = findViewById(R.id.post_comments_list);
        writeComment = findViewById(R.id.write_new_comment);
        addComment = findViewById(R.id.add_new_comment);
        fab = findViewById(R.id.floatingBar);
        fab.attachToListView(listView);

    }

    private void displayComments() {

        if (currentPost.getCommentList().size() > 0) {
            // Getting the current comment list and assign to it profile photo and user name of the commenter
            for (Comment comment : commentsList) {
                mUserReference = FirebaseMethods.getmFirebaseDatabase().getReference("users/" + comment.getUserId());
                mUserReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String username = (String) dataSnapshot.child("username").getValue();
                        String userProfileImage = (String) dataSnapshot.child("profile_photo").getValue();
                        comment.setUsername(username);
                        comment.setUserProfilePhoto(userProfileImage);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(CommentsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else
            Toast.makeText(getApplicationContext(), "No comments, add one", Toast.LENGTH_LONG).show();
    }

    private void setButtonsListeners() {
        addComment.setOnClickListener(this);
        fab.setOnClickListener(this);
    }

    private void addNewComment() {
        String comment = writeComment.getText().toString();
        if (!TextUtils.isEmpty(comment)) {
            uploadComment();
        } else {
            Toast.makeText(getApplicationContext(), "Please type a comment", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadComment() {
        // Creating comment's content
        Comment newComment = new Comment();
        String comment = writeComment.getText().toString().trim();
        String currentUserId = mAuth.getCurrentUser().getUid();
        newComment.setComment(comment);
        newComment.setUserId(currentUserId);

        // ADD HERE TO THE ARRAYLIST AND RETRIEVE DATA IN HOMEFRAGMENT
        if (currentPost.getCommentList().size() == 0) {
            commentsList.add(newComment);
            currentPost.setCommentList(commentsList);
            mAdapter.notifyDataSetChanged();


        } else {
            currentPost.getCommentList();
            currentPost.addComment(newComment);
        }

        mPostReference = FirebaseMethods.getmFirebaseDatabase().getReference("posts/" + currentPost.getUserId() + "/" + currentPost.getPostId() + "/comments");
        mPostReference.child(Objects.requireNonNull(mPostReference.push().getKey())).setValue(newComment);
        newComment.setCommentId(mPostReference.child(Objects.requireNonNull(mPostReference.push().getKey())).getKey());

        mUserReference = FirebaseMethods.getmFirebaseDatabase().getReference("users/" + currentUserId);
        // Getting username and profile photo
        mUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newComment.setUsername(dataSnapshot.child("username").getValue().toString());
                newComment.setUserProfilePhoto(dataSnapshot.child("profile_photo").getValue().toString());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void optionsButton() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            int commentId = position;
            commentRef = FirebaseMethods.getmFirebaseDatabase().getReference("posts").child(currentPost.getUserId()).child(currentPost.getPostId()).getRef();
            Comment comment = commentsList.get(position);
            ArrayList<DatabaseReference> refList = new ArrayList<>();
            ArrayList<DataSnapshot> snapList = new ArrayList<>();

            View layoutView = getLayoutInflater().inflate(R.layout.dialog_deletepost_layout, null);
            Button deletePostButton = layoutView.findViewById(R.id.deletePostButton);
            Button reportButton = layoutView.findViewById(R.id.reportPostButton);
            ImageButton cancelButton = layoutView.findViewById(R.id.cencelDeletePost);

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
            mAdapter.notifyDataSetChanged();

            deletePostButton.setOnClickListener(v -> {
                deleteComment(snapList,position,comment);
                alertDialog.dismiss();
            });
            reportButton.setOnClickListener(v -> {
                reportPost();
                alertDialog.dismiss();
            });
            cancelButton.setOnClickListener(v -> {
                alertDialog.dismiss();
            });
//
//            final CharSequence[] options = {"Delete", "CANCEL"};
//            final AlertDialog.Builder builder = new AlertDialog.Builder(CommentsActivity.this);
//            builder.setTitle("Add Image");
//            builder.setIcon(R.drawable.chefood);
//            builder.setItems(options, (dialog, which) -> {
//
//                if (options[which].equals("Delete")) {
//
//                } else if (options[which].equals("CANCEL")) {
//                    dialog.dismiss();
//                }
//
//            });
//            mAdapter.notifyDataSetChanged();
//            builder.create();
//            builder.show();

        });


//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                final CharSequence[] options = {"Delete", "CANCEL"};
//                final AlertDialog.Builder builder = new AlertDialog.Builder(CommentsActivity.this);
//                builder.setTitle("Add Image");
//                builder.setIcon(R.drawable.chefood);
//                builder.setItems(options, (dialog, which) -> {
//
//                    if (options[which].equals("Delete")) {
//                        //TODO make the delete void for the selected item
//
//                        int commentId =  view.getFocusable();
//                        Log.d(TAG, "onItemClick: comment ID: " +commentId);
////                        mPostReference.addListenerForSingleValueEvent(new ValueEventListener() {
////                            @Override
////                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                                for (DataSnapshot ds : dataSnapshot.getChildren()){}
////
////                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//
//                    } else if (options[which].equals("CANCEL")) {
//                        dialog.dismiss();
//                    }
//
//                });
//                builder.create();
//                builder.show();
//
//            }
//        });

    }

    private void reportPost() {
        Toast.makeText(getApplicationContext(),"report pressed",Toast.LENGTH_SHORT).show();
    }

    private void deleteComment(List<DataSnapshot> snapList, int position, Comment comment) {
        commentRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onItemClick DatasnapshotCount: " + dataSnapshot.getChildrenCount());
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot dss : ds.getChildren())
                        snapList.add(dss);
                }
                Log.d(TAG, "onItemClick snapList: " + snapList.size());

                for (DataSnapshot dsss : snapList) {
                    selectedCommentRef = snapList.get(position).getRef();
                    Log.d(TAG, "onItemClick refList.get(position): " + selectedCommentRef);
                }

                if (comment.getUserId().equals(currentUser.getUid())) {
                    selectedCommentRef.removeValue((databaseError, databaseReference) -> {
                        databaseReference.removeValue();
                        Toast.makeText(getApplicationContext(), "Comment deleted", Toast.LENGTH_SHORT).show();
                    });
                }
                else
                    Toast.makeText(getApplicationContext(), "Can not delete other users's comment", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }
//
//    private void openDialog() {
//
//        String [] options = {"Delete","Cancel"};
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setAdapter(mAdapter, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                builder.setTitle("Delete");
//                builder.setMessage("are you sure you want to delete?");
//            }
//        });
//
//        builder.show();
//
//    }
    // this if we wanna display the
//    private void openDialog() {
//
//        String [] options = {"Delete","Cancel"};
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setAdapter(mAdapter, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                builder.setTitle("Delete");
//                builder.setMessage("are you sure you want to delete?");
//            }
//        });
//
//        builder.show();
//
//    }

//    private void setAdapter(ArrayList<Comment> list) {
//
//    }


    private void setupBottomNavigationView() {
        //Mo.Msaad.Modifications
        BottomNavigationViewHelper bnvh = new BottomNavigationViewHelper(getApplicationContext());
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigationBar);
        bnvh.setupBottomNavigationView(bottomNavigationViewEx);
        bnvh.enableNavigation(getApplicationContext(), bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        //Mo.Msaad.Modifications
        MenuItem menuItem1, menuItem2, menuItem3, menuItem4;
        act.add(ACTIVITY_NUM1);
        act.add(ACTIVITY_NUM2);
        act.add(ACTIVITY_NUM3);
        act.add(ACTIVITY_NUM4);


        switch (act.iterator().next()) {
            case 0:
                menuItem1 = menu.getItem(act.get(0));
                bnvh.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                menuItem1.setChecked(true);
                break;

            case 1:
                menuItem2 = menu.getItem(act.get(1));
                bnvh.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                menuItem2.setChecked(true);
                break;
            case 2:
                menuItem3 = menu.getItem(act.get(2));
                bnvh.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                menuItem3.setChecked(true);
                break;
            case 3:
                menuItem4 = menu.getItem(act.get(3));
                bnvh.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                menuItem4.setChecked(true);
                break;
        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.floatingBar:
                startActivity(new Intent(CommentsActivity.this, HomeActivity.class));
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                break;

            case R.id.add_new_comment:
                addNewComment();
                hideKeyboard();
                writeComment.getText().clear();
                break;

        }

    }
}
