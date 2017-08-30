package com.example.ospidalia2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "Login_Activity";
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    Button google;
    CallbackManager callbackManager;
    boolean googlesignin=false;
    boolean simpleeesignin=false;
    boolean facebooksignin=false;
    boolean isVerfied=true;
    private EditText email ;
    private EditText password ;
    private Button sign_in;
    DatabaseReference user;
    List<Patient> arr = new ArrayList<>();
    public static final String PATIENT_NAME = "patientName";
    String patientEmail;
    String patientName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.setApplicationId("480858012275958");
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);

        email = (EditText) findViewById(R.id.first_name);
        password = (EditText) findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Log.w(TAG, "Absolutly NOT");
                    Intent intent = new Intent(MainActivity.this, home_page.class);
                    FirebaseUser user10=mAuth.getCurrentUser();
                    if(googlesignin==true ||  facebooksignin==true){
                        intent.putExtra(PATIENT_NAME,user10.getDisplayName());
                        startActivity(intent);
                    }
                    else if (simpleeesignin==true){
                        Log.d("SIMPLESIGNIN","TRUE");
                        if(isVerfied==false){
                            Log.d("ISVERFIED",Boolean.toString(isVerfied));
                            return;
                        }
                        for(int i=0;i<arr.size();i++){


                            if(arr.get(i).getMail().equals(email.getText().toString())){
                                Log.d("PATIENTMAIL",email.getText().toString());
                                patientName = arr.get(i).getName();
                                break;
                            }
                        }
                        intent.putExtra(PATIENT_NAME,patientName);
                        startActivity(intent);
                    }

                }
            }
        };
        google = (Button) findViewById(R.id.google_sign_in);
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getBaseContext(), "Connection ERROR", Toast.LENGTH_SHORT).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googlesignin=true;
                facebooksignin=false;
                simpleeesignin=false;
                signIn();
            }
        });
        user = FirebaseDatabase.getInstance().getReference("Users");
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);

                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel:" + "TRUE");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d(TAG, "facebook:onError:" + "TRUE");
                    }
                });

        final Button simLogIn = (Button) findViewById(R.id.sign_in_button);
        simLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googlesignin=false;
                facebooksignin=false;
                simpleeesignin=true;
                simplesignin(email.getText().toString(), password.getText().toString());
            }
        });


/*

        sign_in = (Button)findViewById(R.id.sign_in_button);
        sign_in.setOnClickListener(new View.OnClickListener() {

        @Override

        public void onClick(View view) {
            if(email.getText().toString().equals("") || password.getText().toString().equals("")){
                Toast.makeText(getBaseContext(),"Please fill all data", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean found = false;
            int index = 0;
            for(int i=0;i<arr.size();i++){
                if(arr.get(i).getMail().equals(email.getText().toString()) &&
                        arr.get(i).getPassword().equals(password.getText().toString())){
                    found=true;
                    index = i;
                    break;
                }
            }
            if(found==true){
                Intent intent = new Intent(MainActivity.this, home_page.class);
                intent.putExtra(PATIENT_NAME,arr.get(index).getName());
                startActivity(intent);
            }else{
                Toast.makeText(getBaseContext(),"User Not Found please try again", Toast.LENGTH_SHORT).show();
            }

        }

        });
        */
        TextView signUP = (TextView)findViewById(R.id.signUP);
        signUP.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                Intent SignIntent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(SignIntent);
            }

        });

    //}


    }

    public void simplesignin(String mail,String password){
        mAuth.signInWithEmailAndPassword(mail,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(!user.isEmailVerified()){
                                Toast.makeText(MainActivity.this,"Please Verify Your Email First",Toast.LENGTH_LONG).show();
                                isVerfied = false;

                            }else{
                                isVerfied = true;

                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());


                            Toast.makeText(MainActivity.this, "E-mail or password is incorrect.",
                                    Toast.LENGTH_SHORT).show();
                        }
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
                            googlesignin=false;
                            facebooksignin=true;
                            simpleeesignin=false;

                            Log.d(TAG, "signInWithCredential:success");


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
        user= FirebaseDatabase.getInstance().getReference("Users");
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Patient patient = postSnapshot.getValue(Patient.class);
                    arr.add(patient);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void signIn() {
        Toast.makeText(getBaseContext(),"Yeah", Toast.LENGTH_SHORT).show();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
        else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
            LoginManager.getInstance().logOut();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

/*
    @Override
    protected void onStart() {
        super.onStart();
        user= FirebaseDatabase.getInstance().getReference("Users");
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Patient patient = postSnapshot.getValue(Patient.class);
                    arr.add(patient);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
*/


}
