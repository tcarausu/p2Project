package com.example.myapplication.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.home.HomeActivity;
import com.example.myapplication.home.HomeFragment;
import com.example.myapplication.models.User;
import com.example.myapplication.utility_classes.SectionsPagerAdapter;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener, SignUpFragment.OnFragmentInteractionListener, ForgotPassFragment.OnFragmentInteractionListener {

    private static final String TAG = "LoginActivity";

    private static final String Google_Tag = "GoogleActivity";
    private static final String FacebookTag = "FacebookLogin";
    private static final String Email_Tag = "EmailPassword";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference user_ref;

    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;

    private TextView signUp, orView;
    private RelativeLayout loginLayout;
    private EditText mEmailField, mPasswordField;
    private FragmentManager fragmentManager;
    private boolean isVerified;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        user_ref = firebaseDatabase.getReference("users");

        fragmentManager = getSupportFragmentManager();

        initLayout();
//        setupFirebaseAuth();
        buttonListeners();
    }


    public void initLayout() {
        mContext = LoginActivity.this;

        fragmentManager = getSupportFragmentManager();

        mEmailField = findViewById(R.id.email_id_logIn);
        mPasswordField = findViewById(R.id.password_id_logIn);
        loginLayout = findViewById(R.id.login_activity);
        signUp = findViewById(R.id.textView_id_login_layout_registerHereText);
        orView = findViewById(R.id.orView);

    }

    public void buttonListeners() {

        findViewById(R.id.button_id_logIn).setOnClickListener(this);
        findViewById(R.id.googleSignInButton).setOnClickListener(this);
        findViewById(R.id.textView_id_forgotPass_logIn).setOnClickListener(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("353481374608-mg7rvo8h0kgjmkuts5dcmq65h2louus5.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.facebookLoginButton);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(FacebookTag, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(FacebookTag, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(FacebookTag, "facebook:onError", error);
            }
        });

    }

    // we check in on start method if the user is existing otherwise sign out for preventing server issues
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            goToMainActivity();
        } else return;

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    // sign in with email method
    private void signInWithEmail() {

        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        // first check if our textFields aren't empty
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("");
            Toast.makeText(getApplicationContext(), "Please type in email or phone", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("");
            Toast.makeText(getApplicationContext(), "Please chose password", Toast.LENGTH_SHORT).show();
        } else {
            // after checking, we try to login
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    // we check First if the user, so we dont print please confirm email situation

                    if (mAuth.getCurrentUser() == null) {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    // if task is successful
                    else if (task.isSuccessful()) {
                        verifyAccount(); // check if user is verified by email

                    } else {
                        // otherwise we display the task error message from database
                        Toast.makeText(getApplicationContext(), "Please confirm your email address.", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();// need to keep user signed out until he confirms
                    }
                }
            });

        }
    }

    // verification of the user if validated or not
    private void verifyAccount() {
        FirebaseUser user = mAuth.getCurrentUser();
        isVerified = user.isEmailVerified(); // getting boolean true or false from database

        if (isVerified) {
            goToMainActivity(); // if yes goto mainActivity
        } else {
            // else we first sign out the user, until he checks his email then he can connect
            mAuth.signOut();
            Toast.makeText(getApplicationContext(), "Please verify your account.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(Google_Tag, "Google sign in failed", e);
            }
        }
    }


    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(Google_Tag, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(Google_Tag, "signInWithCredential:success");
                            Snackbar.make(findViewById(R.id.login_layout), "Authentication successful.", Snackbar.LENGTH_SHORT).show();
                            addUserToDataBase();
                            goToMainActivity();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(Google_Tag, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.login_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        }

                        if (!task.isSuccessful()) {
                            Toast.makeText(mContext, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    // Handle the access token from facebook
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(FacebookTag, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(FacebookTag, "signInWithCredential:success");
                            Toast.makeText(LoginActivity.this, "Authentication successful.",
                                    Toast.LENGTH_SHORT).show();
                            goToMainActivity();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(FacebookTag, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            LoginManager.getInstance().logOut();

                        }

                        if (!task.isSuccessful()) {
                            Toast.makeText(mContext, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // google sign in
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // send user to main activity without allowing to go back to login again
    private void goToMainActivity() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.button_id_logIn:
                signInWithEmail();

                break;
            case R.id.textView_id_forgotPass_logIn:

                Fragment fragmentForgotPass = fragmentManager.findFragmentById(R.id.useThisFragmentID);

                if (fragmentForgotPass == null) {
                    fragmentForgotPass = new ForgotPassFragment();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.add(R.id.useThisFragmentID, fragmentForgotPass).commit();
                }
                break;
            case R.id.textView_id_login_layout_registerHereText:
                Toast.makeText(this, "Register using fragment me", Toast.LENGTH_SHORT).show();
                Fragment fragmentRegister = fragmentManager.findFragmentById(R.id.useThisFragmentID);

                if (fragmentRegister == null) {
                    fragmentRegister = new SignUpFragment();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.add(R.id.useThisFragmentID, fragmentRegister).commit();
                }
                break;

            case R.id.googleSignInButton:
                signIn();
                break;

        }
    }

    private void sendUIDData(Context mContext, final Class<? extends Activity> activityToOpen) {
        FirebaseUser user = mAuth.getCurrentUser();

        String userUid = null;
        String userEmail = null;

        if (user != null) {
            userUid = user.getUid();
            userEmail = user.getEmail();
        }

        Intent sendUserUID = new Intent(mContext, activityToOpen);
        sendUserUID.putExtra("userUid", userUid);
        sendUserUID.putExtra("userEmail", userEmail);

        startActivity(sendUserUID);
    }

    /**
     * Used for adding the tabs: Camera, Home and Direct Messages
     */
    private void setupViewPager() {

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment()); //index 0
//        adapter.addFragment(new HomeFragment()); //index 1
//        adapter.addFragment(new DirectMessagesFragment()); //index 2
        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(adapter);

    }

    //    private void setupFirebaseAuth() {
//        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");
//
//        mAuth = FirebaseAuth.getInstance();
//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        myRef = mFirebaseDatabase.getReference();
//
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//
//                if (user != null) {
//                    Log.d(TAG, "onAuthStateChanged: signed in" + user.getUid());
//
//                    myRef.addListenerForSingleEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            if (firebaseMethods.checkIfUserExists(username, dataSnapshot)) {
//                                append = myRef.push().getKey.substring(3, 10); //generates a unique key (method from Firebase Database length of it is 7 chars
//                                Log.d(TAG, "onDataChange: username already exists. Appending random string to name");
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//
//                } else {
//                    Log.d(TAG, "onAuthStateChanged: signed out");
//                }
//            }
//        };
//
//    }
    private void addUserToDataBase() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            final String nodeID = user_ref.push().getKey();
            // do something with the individual "users"
            final String userID = user.getUid();
            final String userMAIL = user.getEmail();

            Query query = user_ref.child("user_id").equalTo(userID);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot addedInfo : dataSnapshot.getChildren()) {
                        User chef_food_user = new User(userID, userMAIL);
                        user_ref.child(nodeID).setValue(chef_food_user);
                        Log.d(TAG, "addUserToDataBase:  user successfully added" + chef_food_user.getEmail());
                    }
                    if (!dataSnapshot.exists()) {
                        // dataSnapshot is the "users" node
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(mContext, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
