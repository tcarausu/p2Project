package com.example.myapplication.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.models.Post;
import com.example.myapplication.models.User;
import com.example.myapplication.utility_classes.RecyclerViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
    private List<Post> mPosts;


    private String mUsername;
    private String mProfilePhoto;
    private String mUserId;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        firebasedatabase = FirebaseDatabase.getInstance();
        mRecyclerView = view.findViewById(R.id.recyclerViewID);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mPosts = new ArrayList<>();

        mDatabasePostRef = firebasedatabase.getReference("posts");

        mDatabasePostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    mUserId = userSnapshot.getKey();

                    mDatabaseUserRef = firebasedatabase.getReference("users/" + mUserId);

                    mDatabaseUserRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            mUsername = user.getUsername();
                            mProfilePhoto = user.getProfile_photo();
                            Log.d(TAG, "onDataChange: profilePic and username :" + mProfilePhoto + " " + mUsername);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                    Log.d(TAG, "onDataChange: mUserId :" + mUserId);

                    for (DataSnapshot postSnapshot : userSnapshot.getChildren()) {

                        // NOW JUST CREATE A USER IN THE DATABASE AND TEST 
                        final Post post = postSnapshot.getValue(Post.class);
                        Log.d(TAG, "onDataChange: uid for user from post : " + post.getmUserId());


                        Log.d(TAG, "onDataChange: username and profile pic : >>>> : " + mUsername + " " + mProfilePhoto);

                        post.setmProfileImgUrl(mProfilePhoto);
                        mPosts.add(post);
                    }
                }

                mAdapter = new RecyclerViewAdapter(getContext(), mPosts);

                mRecyclerView.setAdapter(mAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
