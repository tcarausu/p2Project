package com.example.myapplication.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.home.HomeActivity;
import com.example.myapplication.models.User;
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

/**
 * File created by tcarau18
 **/
public class SearchFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "SearchActivity";

    //widgets
    private EditText mSearchParam;
    private ImageView backArrow;
    private ImageButton mSearchButton;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser user;
    private DatabaseReference myDatabaseUserRef;

    //user data strings
    private String username;
    private String profile_photo_url;
    private String nrOfPosts;
    private String user_id;

    //ListView
    private RecyclerView search_recycler_view;
    private List<User> userList = new ArrayList<>();
    private SearchActivityAdapter adapter;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        user_id = user.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myDatabaseUserRef = FirebaseDatabase.getInstance().getReference("users");

        initLayout(view);
        buttonListeners();

        getUserFromDatabase();

        return view;
    }

    private void initLayout(View view) {
        mSearchParam = view.findViewById(R.id.search_bar_id);
        backArrow = view.findViewById(R.id.backArrow);
        mSearchButton = view.findViewById(R.id.search_button_id);
        search_recycler_view = view.findViewById(R.id.search_recycler_view_id);
        search_recycler_view.setHasFixedSize(true);
        search_recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));


    }

    private void buttonListeners() {
        backArrow.setOnClickListener(this);
        mSearchParam.setOnClickListener(this);
        mSearchButton.setOnClickListener(this);
    }

    public void getUserFromDatabase() {
        String keyword = mSearchParam.getText().toString();

        if (!keyword.equals("")) {

            myDatabaseUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.exists()) {
                            final User user = ds.getValue(User.class);
                            username = user.getUsername();
                            profile_photo_url = user.getProfile_photo();
                            nrOfPosts = String.valueOf(user.getNrOfPosts());

                            if (keyword.equals(username)) {

                                adapter = new SearchActivityAdapter(requireContext(), userList);

                                userList.add(user);
                                adapter.setUserList(userList);

                                search_recycler_view.setAdapter(adapter);

                            } else
                                Toast.makeText(getApplicationContext(), "Incorrect reference", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "No user exists", Toast.LENGTH_SHORT).show();

                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.backArrow:
                startActivity(new Intent(getApplicationContext(), HomeActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                break;

            case R.id.search_button_id:
                getUserFromDatabase();

                break;

        }
    }

}
