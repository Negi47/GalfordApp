package com.example.galeford;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.galeford.models.Users;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    Button googlebtn,emailbtn;
    TextView signup, termcondi;

    private final static int RC_SIGN_IN = 2;
    private FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        googlebtn = findViewById(R.id.logingoogle);
        emailbtn = findViewById(R.id.loginemail);

        signup=findViewById(R.id.signup);
        termcondi = findViewById(R.id.termcondi);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }

        // Configure Google Sign In
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);



        //Google Signin
        googlebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

        emailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signinemailpage = new Intent(LoginActivity.this, EmailSignInActivity.class);
                startActivity(signinemailpage);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signuppage = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(signuppage);
            }
        });


    }

    //Google SignIn Code
    public void signInWithGoogle(){
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
                // ...
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("TAG", "firebaseAuthWithGoogle: " + account.getEmail() + " " + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            final Users user = new Users(
                                    task.getResult().getUser().getEmail().split("@")[0],
                                    task.getResult().getUser().getEmail(),
                                    "",
                                    task.getResult().getUser().getPhoneNumber(),
                                    new ArrayList<String>(),
                                    task.getResult().getUser().getPhotoUrl().toString());

                            FirebaseFirestore.getInstance().collection(TableNames.USERS)
                                    .document(task.getResult().getUser().getUid())
                                    .set(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            finish();
                                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                        }
                                    });
                        }
                        else {
                            Log.d("TAG", "SignIn ERROR" + task.getException());
                            Toast.makeText(LoginActivity.this, "Signin ERRRORRRR", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
// Code Ends Here **


}
