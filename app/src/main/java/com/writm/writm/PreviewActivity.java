package com.writm.writm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.writm.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import Network.SendRequest;
import Utils.Preference;

public class PreviewActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String name,email,pass;
    private Uri display_picture;
    Bitmap bitmap;

    private CallbackManager mCallbackManager;
    private LoginButton loginButton;
    private ImageButton googleLogin,fbLogin;
    private TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.custom_login_firebase);

        textView= (TextView) findViewById(R.id.title_writm);

        googleLogin= (ImageButton) findViewById(R.id.google_signin);
        fbLogin= (ImageButton) findViewById(R.id.fb);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "josephregular.ttf");
        textView.setTypeface(typeface);

        mCallbackManager = CallbackManager.Factory.create();
            loginButton = (LoginButton) findViewById(R.id.fb_signin);
            loginButton.setReadPermissions("email", "public_profile");
            // Callback registration
            loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // App code
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    // App code
                }

                @Override
                public void onError(FacebookException error) {

                }


            });

        // Set click listeners
        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if(firebaseUser!=null)
                {


                    Log.v("AUTHCHECK",new Preference(PreviewActivity.this).getUserid().equals("")?"Session logged out":new Preference(PreviewActivity.this).getUserid());
                    if(new Preference(PreviewActivity.this).getUserid().equals(""))
                    {


                        name = firebaseAuth.getCurrentUser().getDisplayName();
                        email = firebaseAuth.getCurrentUser().getEmail();
                        pass = getSaltString();
                        display_picture = firebaseAuth.getCurrentUser().getPhotoUrl();
                        if(display_picture!=null)
                        {
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(PreviewActivity.this.getContentResolver(), display_picture);

                            }
                            catch(IOException ex)
                            {
                                System.out.print("Invalid!!");
                            }
                        }
                        sendJson(name,email,pass);
                        onSignedinInitialize(firebaseAuth);
                    }
                    else
                    {
                        onSignedinInitialize(firebaseAuth);
                        finish();
                    }







                }

                else
                {
                    new Preference(PreviewActivity.this).updateUserID("");

                    Log.v("AUTHCHECK","firebaseNull");
                }

            }
        };

        fbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });


    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(PreviewActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }




    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGooogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(PreviewActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {

                        }
                    }
                });
    }


    private void signIn() {
        if(mGoogleApiClient!=null && mGoogleApiClient.isConnected())
            mGoogleApiClient.clearDefaultAccountAndReconnect();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed
                Log.e(TAG, "Google Sign In failed.");
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }


    private void onSignedinInitialize(FirebaseAuth auth) {


        Intent myIntent = new Intent(PreviewActivity.this, NavigationDrawerActivity.class);
        myIntent.putExtra("id",R.id.navigation_home);
        if(new Preference(PreviewActivity.this).getUserid().equals(""))
            myIntent.putExtra("progress","exist");
        if(auth.getCurrentUser().getPhotoUrl()!=null)
        {
            Uri display_picture = auth.getCurrentUser().getPhotoUrl();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), display_picture);
                myIntent.putExtra("PICTURE",bitmap);
            }
            catch(IOException ex)
            {
                System.out.print("Invalid!!");
            }
        }

        PreviewActivity.this.startActivity(myIntent);
    }

    public void sendJson(final String name, final String email, String pass) {
        String url="http://writm.com/create_user.php";
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("username",name);
        hashMap.put("email",email);
        Log.v("CHECK MAIL",name);
        Log.v("CHECK MAIL",email);
        hashMap.put("password",pass);
        //hashMap.put("token",FirebaseInstanceId.getInstance().getToken());
        new SendRequest(this).makeNetworkCall(new SendRequest.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                System.out.println("RESULT" +result);
                new Preference(PreviewActivity.this).updateUserID(result);

                updateToken();
            }

            @Override
            public void onError(String result) {
            }
        },hashMap,url);

    }
    public void updateToken()
    {
        String url ="http://writm.com/token_api.php";
        HashMap<String,String> hashMap= new HashMap<>();
        hashMap.put("user_id",new Preference(this).getUserid());
        hashMap.put("token", FirebaseInstanceId.getInstance().getToken());
        new SendRequest(this).makeNetworkCall(new SendRequest.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                System.out.println("RESPONE ON PUSH " + result);
                finish();

            }

            @Override
            public void onError(String result) {

                finish();

            }
        },hashMap,url);
    }
    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }


}
