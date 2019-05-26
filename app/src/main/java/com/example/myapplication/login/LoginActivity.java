package com.example.myapplication.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.example.myapplication.models.User;
import com.example.myapplication.utility_classes.FirebaseMethods;
import com.example.myapplication.utility_classes.StringManipulation;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
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

/**
 * Mo.Msaad
 **/
public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener, SignUpFragment.OnFragmentInteractionListener, ForgotPassFragment.OnFragmentInteractionListener {

    private static final String TAG = "LoginActivity";

    private static final String Google_Tag = "GoogleActivity";
    private static final String FacebookTag = "FacebookLogin";
    private static final int RC_SIGN_IN = 9001;
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseMethods mFirebaseMethods;
    private DatabaseReference user_ref;
    private DatabaseReference myRef;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;

    // widgets
    private LoginButton loginButton;
    private TextView signUp, orView;
    private RelativeLayout loginLayout;
    private EditText mEmailField, mPasswordField;
    private FragmentManager fragmentManager;
    private boolean isVerified;
    private Context mContext;


    // this is set to create an email_signed_in_user with default avatar. We store the picture on database an download it later.
    private String avatarURL = "https://firebasestorage.googleapis.com/v0/b/p2project-2a81d.appspot.com/o/avatar_chefood%2FGroup%205.png?alt=media&token=87e74817-a27d-4a04-afa3-e7cfa1adca68";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = LoginActivity.this;
        connectFirebase();
        mFirebaseMethods.checkAuth(getApplicationContext(), mAuth);
        initLayout();
        buttonListeners();
    }

    private void connectFirebase() {
        mFirebaseMethods = FirebaseMethods.getInstance(getApplicationContext());
        fragmentManager = getSupportFragmentManager();
        mAuth = FirebaseMethods.getAuth();
        currentUser = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseMethods.getmFirebaseDatabase();
        user_ref = firebaseDatabase.getReference("users");
        myRef = firebaseDatabase.getReference();
    }


    public void initLayout() {
        mEmailField = findViewById(R.id.email_id_logIn);
        mPasswordField = findViewById(R.id.password_id_logIn);
        loginLayout = findViewById(R.id.login_activity);
        signUp = findViewById(R.id.sign_up);
        orView = findViewById(R.id.orView);
        loginButton = findViewById(R.id.facebookLoginButton);
    }

    public void buttonListeners() {

        findViewById(R.id.button_id_log_in).setOnClickListener(this);
        findViewById(R.id.googleSignInButton).setOnClickListener(this);
        findViewById(R.id.textView_id_forgotPass_logIn).setOnClickListener(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("353481374608-mg7rvo8h0kgjmkuts5dcmq65h2louus5.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(FacebookTag, "facebook:onSuccess:" + loginResult);
                loginButton.setEnabled(false);
                loginButton.setVisibility(View.GONE);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                loginButton.setVisibility(View.VISIBLE);
                loginButton.setEnabled(true);
                Log.d(FacebookTag, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                loginButton.setVisibility(View.VISIBLE);
                loginButton.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(FacebookTag, "facebook:onError", error);
            }
        });

    }

    // we check in on start method if the user is existing otherwise sign out for preventing server issues
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.


    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseMethods.checkAuth(getApplicationContext(), mAuth);


    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    @Override
    public void onStop() {
        super.onStop();

    }

    // sign in with email method
    private void signInWithEmail() {

        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        // first check if our textFields aren't empty
        if (TextUtils.isEmpty(password) && TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            mPasswordField.setError("Required.");
            Toast.makeText(getApplicationContext(), "Please type in email or phone", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Please chose password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            Toast.makeText(getApplicationContext(), "Please type in email or phone", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            Toast.makeText(getApplicationContext(), "Please chose password", Toast.LENGTH_SHORT).show();
        } else {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Signing in");
            progressDialog.setMessage("Signing in, please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setIcon(R.drawable.chefood);
            progressDialog.show();

            // after checking, we try to login
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                // if sign in is successful
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    verifyAccount(email); // check if user is verified by email

                }
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                mAuth.signOut();
            });

        }
    }

    // verification  if user has validated or not

    /**
     * @param email : email of the registered user, allows login if & only if validated
     * @author Mo.Msaad
     **/
    private void verifyAccount(String email) {

        try {
            FirebaseUser user = mAuth.getCurrentUser();
            isVerified = user.isEmailVerified(); // getting boolean true or false from database

            if (isVerified) {
                verifyFirstEmailLogin(email, "Chose a user name", avatarURL);

                addUserToDataBase();
                mFirebaseMethods.goToWhereverWithFlags(getApplicationContext(), getApplicationContext(), HomeActivity.class); // if yes goto mainActivity
            } else {
                // else we first sign out the user, until he checks his email then he can connect
                mAuth.signOut();
                Toast.makeText(getApplicationContext(), "Please verify your account.", Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), "Somthing went wrong...", Toast.LENGTH_SHORT).show();
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
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        final String displayName = task.getResult().getUser().getDisplayName();
                        final String email = task.getResult().getUser().getEmail();
                        final String photoURL = task.getResult().getUser().getPhotoUrl().toString();

                        Log.d(TAG, "google sign in result: " + "\n" + "displayName: " + displayName + "\n" + "email: " + email
                                + "\n" + "PictureURL: " + photoURL);

                        verifyFirstGoogleLogin(email, displayName, photoURL);
                        addUserToDataBase();
                        Log.d(Google_Tag, "signInWithCredential:success");
                        Snackbar.make(findViewById(R.id.login_layout), "Authentication successful.", Snackbar.LENGTH_SHORT).show();
                        new Handler().postDelayed(() -> mFirebaseMethods.goToWhereverWithFlags(getApplicationContext(), getApplicationContext(), HomeActivity.class), Toast.LENGTH_SHORT);

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(Google_Tag, "signInWithCredential:failure", task.getException());
                        Snackbar.make(findViewById(R.id.login_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                    }

                    if (!task.isSuccessful()) {
                        Toast.makeText(mContext, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    // Handle the access token from facebook

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(FacebookTag, "handleFacebookAccessToken:" + token);


        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(LoginActivity.this, "Authentication successful.", Toast.LENGTH_SHORT).show();

                        String uid = task.getResult().getUser().getUid();
                        String email = task.getResult().getUser().getEmail();
                        String username = task.getResult().getUser().getDisplayName();
                        String url = task.getResult().getUser().getPhotoUrl().toString();
                        Log.d(TAG, "onComplete: uid: " + uid + "\n"
                                + "email: " + email + "\n" + "username: " + username + "\n" + "url: " + url + "\n");

                        verifyFirstFBLogin(email, username, url);
                        addUserToDataBase();
                        loginButton.setEnabled(false);
                        loginButton.setVisibility(View.GONE);

                        new Handler().postDelayed(() -> mFirebaseMethods.goToWhereverWithFlags(getApplicationContext(), getApplicationContext(), HomeActivity.class), 0);

                    } else {

                        // If sign in fails, display a message to the user.
                        Log.w(FacebookTag, "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed, " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        LoginManager.getInstance().logOut();
                        loginButton.setEnabled(true);
                        loginButton.setVisibility(View.VISIBLE);
                    }
                });
    }
    // google sign in

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.button_id_log_in:
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

            case R.id.sign_up:
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

    //

    private void addUserToDataBase() {
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {

            final String nodeID = user_ref.child(user.getUid()).getKey();

            final String userMAIL = user.getEmail();

            Query query = user_ref.
                    orderByKey().equalTo(nodeID);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        if (user.getDisplayName() != null) {
                            addNewUser(userMAIL, user.getDisplayName(), "about", "website", "photo");
                            Log.d(TAG, "addUserToDataBase:  user successfully added" + userMAIL);

                        } else {
                            addNewUser(userMAIL, "random username", "about", "website", "photo");
                            Log.d(TAG, "addUserToDataBase:  user successfully added" + userMAIL);

                        }

                    } else {
                        Log.d(TAG, "onDataChange: Found a Match: " + userMAIL);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(mContext, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    /**
     * Add information to the users and user account settings node
     * Database:user_account_settings
     * Database:users
     *
     * @param email         represents the email of the Firebase User
     * @param username      represents the username of the Firebase User
     * @param description   represents the about from the User Profile
     * @param website       represents the website from the User Profile
     * @param profile_photo represents the profile_photo from the User Profile
     * @author Mo.Msaad & T.Trasco
     */
    private void addNewUser(String email, String username, String description, String website, String profile_photo) {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        User user = new User(
                description,
                "Chose a user name",
                StringManipulation.condenseUserName(username),
                0,
                0,
                0,
                profile_photo,
                website,
                0,
                email
        );

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(currentUser.getUid())
                .setValue(user);
    }

    /**
     * @param email:    email fetched from the provider, used to add user email
     * @param username: name fetched from the provider, used to add user name
     * @param url:      phot fetched from the provider, used to add profile pic
     * @author Mo.Msaad
     **/
    private void verifyFirstFBLogin(@NonNull String email, @NonNull String username, @NonNull String url) {

        SharedPreferences facebookPrefs = getSharedPreferences("fbPrefs", MODE_PRIVATE);
        boolean fbFirstLogin = facebookPrefs.getBoolean("fbPrefs", true);

        //if its the first run we change the boolean to false
        if (fbFirstLogin) {
            Log.d(TAG, "verifyFirstRun: boolean first run is: " + fbFirstLogin);
            addNewUser(email, username, "description", "website", url);
            Log.d(TAG, "verifyFirstFBLogin: fetched url from google: " + url);
            SharedPreferences.Editor editor = facebookPrefs.edit();
            editor.putBoolean("fbPrefs", false);
            editor.apply();
            Log.d(TAG, "verifyFirstRun: boolean first run is: " + fbFirstLogin);
        }

    }


    /**
     * @param displayName: email fetched from the provider, used to add user email
     * @param email:       name fetched from the provider, used to add user name
     * @param photoURL:    phot fetched from the provider, used to add profile pic
     * @author Mo.Msaad
     **/
    private void verifyFirstGoogleLogin(String email, String displayName, String photoURL) {

        SharedPreferences ggPrefs = getSharedPreferences("ggPrefs", MODE_PRIVATE);
        boolean googleFirstLogin = ggPrefs.getBoolean("ggPrefs", true);

        //if its the first run we change the boolean to false
        if (googleFirstLogin) {
            addNewUser(email, displayName, "description", "website", photoURL);
            SharedPreferences.Editor editor = ggPrefs.edit();
            editor.putBoolean("ggPrefs", false);
            editor.apply();
            Log.d(TAG, "verifyFirstRun: boolean first run is: " + googleFirstLogin);
        }
    }

    private void verifyFirstEmailLogin(String email, String displayName, String photoURL) {

        SharedPreferences firstLogin = getSharedPreferences("logPrefs", MODE_PRIVATE);
        boolean googleFirstLogin = firstLogin.getBoolean("logPrefs", true);

        //if its the first run we change the boolean to false
        if (googleFirstLogin) {
            addNewUser(email, displayName, "description", "website", photoURL);
            SharedPreferences.Editor editor = firstLogin.edit();
            editor.putBoolean("logPrefs", false);
            editor.apply();
            Log.d(TAG, "verifyFirstRun: boolean first run is: " + googleFirstLogin);
        }
    }


    public void onFragmentInteraction(Uri uri) {
    }
}
