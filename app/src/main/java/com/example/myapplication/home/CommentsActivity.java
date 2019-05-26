package com.example.myapplication.home;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.models.Comment;
import com.example.myapplication.models.Post;
import com.example.myapplication.utility_classes.ListViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class CommentsActivity extends AppCompatActivity {

    private final String TAG = "CommentsActivity";

    private DatabaseReference mPostReference;
    private DatabaseReference mUserReference;

    private ListViewAdapter mAdapter;
    private ListView listView;
    private EditText writeComment;
    private Button addComment;

    private ArrayList<Comment> cl;

    // Model data
    private Post currentPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);


        listView = findViewById(R.id.post_comments_list);
        writeComment = findViewById(R.id.write_new_comment);
        addComment = findViewById(R.id.add_new_comment);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        currentPost = bundle.getParcelable("currentPost");

        setButtonsListeners();
        displayComments();
    }

    private void displayComments(){

        if(currentPost.getCommentList() != null) {
            // Getting the current comment list and assign to it profile photo and user name of the commenter
            ArrayList<Comment> commentsList = (ArrayList<Comment>) currentPost.getCommentList();
            setAdapter(commentsList);

            for (Comment comment : commentsList) {
                mUserReference = FirebaseDatabase.getInstance().getReference("users/" + comment.getUserId());
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
        }
    }


    private void setButtonsListeners(){
        addComment.setOnClickListener(v -> {
            if (!writeComment.getText().toString().trim().equals("")) {
                uploadComment();
            }
            else {
                Toast.makeText(getApplicationContext(), "Please insert a comment", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void uploadComment(){
        // Creating comment's content
        Comment newComment = new Comment();
        String comment = writeComment.getText().toString().trim();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        newComment.setComment(comment);
        newComment.setUserId(currentUserId);

        // ADD HERE TO THE ARRAYLIST AND RETRIEVE DATA IN HOMEFRAGMENT
        if (currentPost.getCommentList() == null) {
            ArrayList<Comment> commentsList = new ArrayList<>();
            commentsList.add(newComment);
            currentPost.setCommentList(commentsList);
            setAdapter(commentsList);
        } else {
            currentPost.addComment(newComment);
        }

        mPostReference = FirebaseDatabase.getInstance().getReference("posts/" + currentPost.getUserId()
                + "/" + currentPost.getPostId() + "/comments");
        mPostReference.child(Objects.requireNonNull(mPostReference.push().getKey())).setValue(newComment);

        mUserReference = FirebaseDatabase.getInstance().getReference("users/" +currentUserId);
        // Getting username and profile photo
        mUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                newComment.setUsername((String) dataSnapshot.child("username").getValue());
                newComment.setUserProfilePhoto((String) dataSnapshot.child("profile_photo").getValue());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setAdapter(ArrayList<Comment> list){
        mAdapter = new ListViewAdapter(getApplicationContext(),list);
        listView.setAdapter(mAdapter);
    }
}
