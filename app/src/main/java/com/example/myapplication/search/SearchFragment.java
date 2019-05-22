package com.example.myapplication.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.home.HomeActivity;
import com.example.myapplication.models.User;
import com.example.myapplication.utility_classes.BottomNavigationViewHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * File created by tcarau18
 **/
public class SearchFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "SearchActivity";
    private static final int ACTIVITY_NUM = 1;

    //widgets
    private EditText mSearchParam;
    private ImageView backArrow;
    private ImageButton mSearchButton;
    private BottomNavigationViewEx bottomNavigationViewEx;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser user;
    private DatabaseReference myDatabaseUserRef;

    //user data strings
    private String mUsername;
    private String mProfilePhoto;
    private String mUserId;

    //ListView
    private RecyclerView search_recycler_view;
    private List<User> userList = new ArrayList<>();
    private SearchActivityAdapter adapter;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mUserId = user.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myDatabaseUserRef = FirebaseDatabase.getInstance().getReference("users");


        search_recycler_view = view.findViewById(R.id.search_recycler_view_id);
        search_recycler_view.setHasFixedSize(true);
        search_recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));

        initLayout(view);
        buttonListeners();
        setupBottomNavigationView();

        getUserFromDatabase();

//        GetData getData = new GetData();
//        getData.execute();


        return view;
    }

    private void initLayout(View view) {
        mSearchParam = view.findViewById(R.id.search_bar_id);
        backArrow = view.findViewById(R.id.backArrow);
        mSearchButton = view.findViewById(R.id.search_button_id);
        search_recycler_view = view.findViewById(R.id.search_recycler_view_id);
        bottomNavigationViewEx = view.findViewById(R.id.bottomNavigationBar);

    }

    private void buttonListeners() {
        backArrow.setOnClickListener(this);
        mSearchParam.setOnClickListener(this);
        mSearchButton.setOnClickListener(this);
    }

    public void getUserFromDatabase() {
        String keyword = mSearchParam.getText().toString();

//        Query query = myDatabaseUserRef
//                .orderByChild(myDatabaseUserRef
//                        .child(getString(R.string.field_user_id))
//                        .child(getString(R.string.field_username))
//                        .getKey()
//                )
//                ;

        myDatabaseUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    final User user = ds.getValue(User.class);
                    mUsername = user.getUsername();
                    mProfilePhoto = user.getProfile_photo();

                    if (ds.exists()
                            && mUsername.equals(keyword)) {

                        adapter = new SearchActivityAdapter(getApplicationContext(), userList);

                        userList.add(user);
                        adapter.setUserList(userList);
                        Log.d(TAG, "onDataChange: profilePic and username :" + mProfilePhoto + " " + mUsername);


                    } else {
                        Toast.makeText(getApplicationContext(), "No such user exists", Toast.LENGTH_SHORT).show();

                    }
                }
                search_recycler_view.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    /**
     * Bottom Navigation View setup
     */
    public void setupBottomNavigationView() {
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(getContext(), bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }
}
