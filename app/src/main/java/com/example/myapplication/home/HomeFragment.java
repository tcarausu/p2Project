package com.example.myapplication.home;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.models.Comment;
import com.example.myapplication.models.Like;
import com.example.myapplication.models.Post;
import com.example.myapplication.models.User;
import com.example.myapplication.utility_classes.FirebaseMethods;
import com.example.myapplication.utility_classes.RecyclerViewAdapterPostItems;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapterPostItems mAdapter;

    private FirebaseDatabase firebasedatabase;
    private DatabaseReference mDatabasePostRef;
    private DatabaseReference mDatabaseUserRef;
    private DatabaseReference mDatabaseUserPostRef;
    private FirebaseMethods mFirebaseMethods;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseUser current_user;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabasePostComments;

    private List<Post> mPosts;
    private List<User> mUsers;
    private List<Comment> mComments;

    private String mUsername;
    private String mProfilePhoto;
    private String mCurrentUserId;


    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mFirebaseMethods = FirebaseMethods.getInstance(getContext());
        mAuth = FirebaseMethods.getAuth();
        mFirebaseMethods.checkUserStateIfNull(getActivity(), mAuth);


        current_user = mAuth.getCurrentUser();
        firebasedatabase = FirebaseMethods.getmFirebaseDatabase();

        mRecyclerView = view.findViewById(R.id.recyclerViewID);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        mPosts = new ArrayList<>();
        mUsers = new ArrayList<>();
        mComments = new ArrayList<>();

        mAdapter = new RecyclerViewAdapterPostItems(getContext(), mPosts, mRecyclerView);

        GetData getData = new GetData();
        getData.execute();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseMethods.checkUserStateIfNull(getApplicationContext(), mAuth);
    }

    public class GetData extends AsyncTask<Void, Void, Void> {

        private GetData() {
        }

        private void getPostsInfo() {

            try {
                mDatabasePostRef = firebasedatabase.getReference("posts");
                mDatabasePostRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mPosts.clear();
                        mUsers.clear();

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            mCurrentUserId = current_user.getUid();
                            // Getting the users that posted
                            String postUserId = postSnapshot.getKey();

                            mDatabaseUserRef = firebasedatabase.getReference("users/" + current_user.getUid());
                            mDatabaseUserPostRef = firebasedatabase.getReference("users/" + postUserId);

                            Log.d(TAG, "onDataChange: mCurrentUserId :" + mCurrentUserId);

                            // Getting each post for each user
                            for (DataSnapshot userSnapshot : postSnapshot.getChildren()) {

                                // NOW JUST CREATE A USER IN THE DATABASE AND TEST
                                final Post post = userSnapshot.getValue(Post.class);
                                Log.d(TAG, "onDataChange: uid for user from post : " + post.getUserId());

                                List<Like> likeList = new ArrayList<>();
                                List<Comment> commentList = new ArrayList<>();

                                // Getting likes from each post
                                for (DataSnapshot likeSnapshot : userSnapshot.child("mLikes").getChildren()) {
                                    // TODO, likes if no like yet, it crushes,
                                    Like like = new Like();
                                    like.setUser_id(likeSnapshot.getValue(Like.class).getUser_id());
                                    likeList.add(like);
                                }
                                post.setLikeList(likeList);

                                // Getting comments from each post
                                for (DataSnapshot commentSnapshot : userSnapshot.child("comments").getChildren()) {
                                    // TODO, Transform the constructor of the Comment class to receive data from firebase
                                    Comment comment = commentSnapshot.getValue(Comment.class);
                                    commentList.add(comment);
//                                    mDatabasePostComments = firebasedatabase.getReference("users/" + comment.getUserId());
                                }
                                post.setCommentList(commentList);

                                mDatabaseUserRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        try {
                                            if (mCurrentUserId.equals(postUserId)) {
                                                final User user = dataSnapshot.getValue(User.class);
                                                if (user.getUsername() == null) {
                                                    mPosts.remove(post);
                                                    mDatabasePostRef.removeValue();
                                                } else
                                                    mUsername = user.getUsername();
                                                mProfilePhoto = user.getProfile_photo();

                                                mAdapter.setUserForPost(post, user);
                                                mAdapter.notifyDataSetChanged();
                                                Log.d(TAG, "onDataChange: profilePic and username :" + mProfilePhoto + " " + mUsername);
                                            }
                                        } catch (NullPointerException e) {
                                            Log.e(TAG, "onDataChange: NullPointerException", e.getCause());
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                mDatabaseUserPostRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        try {
                                            if (!mCurrentUserId.equals(postUserId)) {
                                                final User user = dataSnapshot.getValue(User.class);
                                                if (user.getUsername() == null) {
                                                    mPosts.remove(post);
                                                    mDatabasePostRef.removeValue();
                                                } else
                                                    mUsername = user.getUsername();
                                                mProfilePhoto = user.getProfile_photo();

                                                mAdapter.setUserForPost(post, user);
                                                mAdapter.notifyDataSetChanged();

                                            }
                                        } catch (NullPointerException e) {
                                            Log.d(TAG, "onDataChange: profilePic and username :" + mProfilePhoto + " " + mUsername);

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }

                                });

                                mPosts.add(post);
                                mAdapter.setPostsList(mPosts);
                                mAdapter.notifyDataSetChanged();

                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: Canceled.");

                    }
                });

            } catch (
                    NullPointerException e) {
                mDatabasePostRef.removeValue();
                Log.d(TAG, "onDataChange: error: " + e.getMessage());
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {
            getPostsInfo();
            return null;
        }
    }
}
