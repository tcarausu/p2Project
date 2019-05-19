package com.example.myapplication.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

/**Mo.Msaad
 * **/
public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener, SignUpFragment.OnFragmentInteractionListener, ForgotPassFragment.OnFragmentInteractionListener {

    private static final String TAG = "LoginActivity";

    private static final String Google_Tag = "GoogleActivity";
    private static final String FacebookTag = "FacebookLogin";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser ;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseMethods mFirebaseMethods ;
    private DatabaseReference user_ref;
    private DatabaseReference myRef;


    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;
    

    private TextView signUp, orView;
    private RelativeLayout loginLayout;
    private EditText mEmailField, mPasswordField;
    private FragmentManager fragmentManager;
    private boolean isVerified;

    private String avatarURL = "https://firebasestorage.googleapis.com/v0/b/p2project-2a81d.appspot.com/o/avatar_chefood%2FGroup%205.png?alt=media&token=87e74817-a27d-4a04-afa3-e7cfa1adca68";

    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mFirebaseMethods = new FirebaseMethods(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        user_ref = firebaseDatabase.getReference("users");
        myRef = firebaseDatabase.getReference();

        fragmentManager = getSupportFragmentManager();


        initLayout();
        buttonListeners();
    }


    public void initLayout() {
        mContext = LoginActivity.this;

        fragmentManager = getSupportFragmentManager();

        mEmailField = findViewById(R.id.email_id_logIn);
        mPasswordField = findViewById(R.id.password_id_logIn);
        loginLayout = findViewById(R.id.login_activity);
        signUp = findViewById(R.id.sign_up);
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


        if (mAuth != null && currentUser != null ) {
            goTosWithFlags(getApplicationContext(),HomeActivity.class);
        }
        else if (mAuth == null && currentUser!= null){

            try{
                myRef.removeValue();
                mFirebaseMethods.logOut();
            }catch (NullPointerException e){
                Toast.makeText(this,"Something went wrong, try again: "+mAuth.getCurrentUser().delete().getResult().toString(),Toast.LENGTH_SHORT).show();
                System.exit(1);
            }
        }

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
            // after checking, we try to login
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {



                if (mAuth.getCurrentUser() == null) {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                }
                // if task is successful
                else if (task.isSuccessful()) {
                    verifyAccount(email); // check if user is verified by email


                } else {
                    // otherwise we display the task error message from database
                    Toast.makeText(getApplicationContext(), "Please confirm your email address.", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();// need to keep user signed out until he confirms
                }
            });

        }
    }

    // verification  if user has validated or not
    /**@author Mo.Msaad
     * @param email : email of the registered user, allows login if & only if validated
     * **/
    private void verifyAccount(String email) {

        try{
            FirebaseUser user = mAuth.getCurrentUser();
            isVerified = user.isEmailVerified(); // getting boolean true or false from database

            if (isVerified) {
                addNewUser(email,"Chose a user name","description","website",avatarURL);
                addUserToDataBase();
                goTosWithFlags(getApplicationContext(),HomeActivity.class); // if yes goto mainActivity
            } else {
                // else we first sign out the user, until he checks his email then he can connect
                mAuth.signOut();
                Toast.makeText(getApplicationContext(), "Please verify your account.", Toast.LENGTH_SHORT).show();
            }
        }catch (NullPointerException e){
            Toast.makeText(getApplicationContext(),"Somthing went wrong...",Toast.LENGTH_SHORT).show();
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
                        final  String email = task.getResult().getUser().getEmail();
                        final   String photoURL = task.getResult().getUser().getPhotoUrl().toString();
                        final String phoneNumber = task.getResult().getUser().getPhoneNumber();
                        Log.d(TAG, "google sign in result: "+"\n"+"displayName: "+displayName+"\n"+"email: "+email
                                +"\n"+"phoneNumber: "+phoneNumber+"\n"+"PictureURL: "+photoURL);

                        verifyFirstGoogleLogin(email,displayName,photoURL);
                        addUserToDataBase();
                        Log.d(Google_Tag, "signInWithCredential:success");
                        Snackbar.make(findViewById(R.id.login_layout), "Authentication successful.", Snackbar.LENGTH_SHORT).show();
                        new Handler().postDelayed(() -> goTosWithFlags(getApplicationContext(),HomeActivity.class), Toast.LENGTH_SHORT);

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

                        verifyFirstFBLogin(email, username, url);
                        addUserToDataBase();
                        Log.d(TAG, "onComplete: uid: " + uid + "\n"
                                + "email: " + email + "\n" + "username: " + username + "\n" + "url: " + url + "\n");

                        new Handler().postDelayed(() -> goTosWithFlags(getApplicationContext(),HomeActivity.class), 0);

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(FacebookTag, "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed, " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        LoginManager.getInstance().logOut();
                    }
                });
    }
    // google sign in

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // send user to main activity without allowing to go back to login again
    /**responsible to send user to needed activities or fragment
     * @param context context of the actual actviity or fragment
     * @param cl is the destination class to load with flags to not allow go back with onBackPressed
     *
     * **/
    public void goTosWithFlags(Context context, Class<? extends AppCompatActivity> cl){
        startActivity(new Intent(context,cl).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
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
     * @author Mo.Msaad & T.Trasco
     * @param email         represents the email of the Firebase User
     * @param username      represents the username of the Firebase User
     * @param description   represents the about from the User Profile
     * @param website       represents the website from the User Profile
     * @param profile_photo represents the profile_photo from the User Profile
     */
    private void addNewUser(String email, String username, String description, String website, String profile_photo) {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        User user = new User(
                description,
                "Chose a user name",
                StringManipulation.condenseUserName(username),
                email,
                0,
                0,
                0,
                0,
                profile_photo,
                website);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(currentUser.getUid())
                .setValue(user);
    }

    /**@author Mo.Msaad
     * @param email: email fetched from the provider, used to add user email
     * @param username: name fetched from the provider, used to add user name
     * @param url: phot fetched from the provider, used to add profile pic
     * **/
    private void verifyFirstFBLogin(String email, String username, String url) {

       SharedPreferences facebookPrefs = getSharedPreferences("fbPrefs", MODE_PRIVATE);
        boolean fbFirstLogin = facebookPrefs.getBoolean("fbPrefs", true);

        //if its the first run we change the boolean to false
        if (fbFirstLogin) {
            addNewUser(email, username, "description", "website", url);
            SharedPreferences.Editor editor = facebookPrefs.edit();
            editor.putBoolean("fbPrefs", false);
            editor.apply();
            Log.d(TAG, "verifyFirstRun: boolean first run is: "+fbFirstLogin);
        }

    }

    public void onFragmentInteraction(Uri uri) {
    }

    /**@author Mo.Msaad
     * @param displayName: email fetched from the provider, used to add user email
     * @param email: name fetched from the provider, used to add user name
     * @param photoURL: phot fetched from the provider, used to add profile pic
     * **/
    private void verifyFirstGoogleLogin(String email, String displayName, String photoURL) {

        SharedPreferences ggPrefs = getSharedPreferences("ggPrefs", MODE_PRIVATE);
        boolean googleFirstLogin = ggPrefs.getBoolean("ggPrefs", true);

        //if its the first run we change the boolean to false
        if (googleFirstLogin) {
            addNewUser(email, displayName, "description", "website", photoURL);
            SharedPreferences.Editor editor = ggPrefs.edit();
            editor.putBoolean("ggPrefs", false);
            editor.apply();
            Log.d(TAG, "verifyFirstRun: boolean first run is: "+googleFirstLogin);
        }
    }
}
