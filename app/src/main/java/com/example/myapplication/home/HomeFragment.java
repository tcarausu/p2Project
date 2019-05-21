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
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.models.Like;
import com.example.myapplication.models.Post;
import com.example.myapplication.models.User;
import com.example.myapplication.utility_classes.FirebaseMethods;
import com.example.myapplication.utility_classes.RecyclerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;

    private DatabaseReference mDatabasePostRef;
    private FirebaseDatabase firebasedatabase;
    private DatabaseReference mDatabaseUserRef;
    private DatabaseReference mDatabaseUserPostRef;
    private FirebaseMethods mFirebaseMethods ;

    private FirebaseUser current_user ;
    private FirebaseAuth mAuth ;
    private List<Post> mPosts;
    private List<User> mUsers;

    private String mUsername;
    private String mProfilePhoto;
    private String mUserId;


    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser();
        mFirebaseMethods = new FirebaseMethods(getContext());
        mUserId = current_user.getUid();
        firebasedatabase = FirebaseDatabase.getInstance();
        mDatabasePostRef = firebasedatabase.getReference("posts");

        mRecyclerView = view.findViewById(R.id.recyclerViewID);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                recyclerView.refreshDrawableState();

            }
        });

        mPosts = new ArrayList<>();
        mUsers = new ArrayList<>();


        GetData getData = new GetData();
        getData.execute();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseMethods.checkUserStateIfNull();
        GetData getData = new GetData();
        getData.execute();

    }

    @Override
    public void onPause() {
        super.onPause();
        mFirebaseMethods.checkUserStateIfNull();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseMethods.checkUserStateIfNull();
    }

    public class GetData extends AsyncTask<Void,Void,Void> {

        private GetData() {}

        private void getPostsInfo() {
            try{
                mDatabasePostRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String postUserId = userSnapshot.getKey();
                            mDatabaseUserRef = firebasedatabase.getReference("users/" + mUserId);
                            mDatabaseUserPostRef = firebasedatabase.getReference("users/" + postUserId);

                            Log.d(TAG, "onDataChange: mUserId :" + mUserId);

                            for (DataSnapshot postSnapshot : userSnapshot.getChildren()) {

                                // NOW JUST CREATE A USER IN THE DATABASE AND TEST
                                final Post post = postSnapshot.getValue(Post.class);
                                Log.d(TAG, "onDataChange: uid for user from post : " + post.getUserId());

                                Log.d(TAG, "onDataChange: username and profile pic : >>>> : " + mUsername + " " + mProfilePhoto);

                                List<Like> likeList = new ArrayList<>();

                                for (DataSnapshot ds : postSnapshot.child("mLikes").getChildren()) {
                                    // TODO, likes if no like yet, it crushes,
                                    Like like = new Like();
                                    like.setUser_id(ds.getValue(Like.class).getUser_id());
                                    likeList.add(like);
                                }
                                post.setLikes(likeList);

                                mDatabaseUserRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (mUserId.equals(postUserId)) {
                                            final User user = dataSnapshot.getValue(User.class);
                                            if (user.getUsername()== null){
                                                mPosts.remove(post);
                                                mDatabasePostRef.removeValue() ;
                                            }

                                            else
                                                mUsername = user.getUsername();
                                            mProfilePhoto = user.getProfile_photo();

                                            mAdapter.setUserForPost(post, user);

                                            Log.d(TAG, "onDataChange: profilePic and username :" + mProfilePhoto + " " + mUsername);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                                mDatabaseUserPostRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (!mUserId.equals(postUserId)) {
                                            final User user = dataSnapshot.getValue(User.class);
                                            try {
                                                if (user.getUsername()== null){
                                                    mPosts.remove(post);
                                                    mDatabasePostRef.removeValue() ;
                                                }
                                                else
                                                    mUsername = user.getUsername();
                                                mProfilePhoto = user.getProfile_photo();

                                                mAdapter.setUserForPost(post, user);
                                            }catch (NullPointerException e){
                                                Log.d(TAG, "onDataChange: profilePic and username :" + mProfilePhoto + " " + mUsername);

                                            }

                                            Log.d(TAG, "onDataChange: profilePic and username :" + mProfilePhoto + " " + mUsername);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                mAdapter = new RecyclerViewAdapter(getContext(), mPosts);
                                mPosts.add(post);
                                mAdapter.setPostsList(mPosts);


                            }
                        }

                        mRecyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: Canceled.");

                    }
                });

            }catch (Exception e){
                Toast.makeText(getContext(),"Nothing to display! create a Post",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getPostsInfo();
            return null;
        }
    }
}
