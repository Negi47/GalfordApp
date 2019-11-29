package com.example.galeford;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class EmailSignInActivity extends AppCompatActivity {

    private static FirebaseAuth mAuth;
    EditText mailid,password;
    Button loginbtn;
    TextView forgetpswd;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_sign_in);

        mAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.galeford_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Login");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmailSignInActivity.this, LoginActivity.class));
            }
        });

        mailid = findViewById(R.id.emailid);
        password = findViewById(R.id.password);
        loginbtn = findViewById(R.id.loginbtn);
        forgetpswd = findViewById(R.id.forgetpswd);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailid = mailid.getText().toString().toLowerCase().trim();
                String pswd = password.getText().toString().trim();

                LogInWithEmail(emailid,pswd);
            }
        });
    }

    public void LogInWithEmail(String emailid, String pswd){

        mAuth.signInWithEmailAndPassword(emailid, pswd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("LOGIN: ", "loginWithEmailAndPassword SUCCESS");
//                    preferencesConfig.setLoginStatus(true);
                    startActivity(new Intent(EmailSignInActivity.this, HomeActivity.class));
                    finish();
                    Toast.makeText(EmailSignInActivity.this,"Error",Toast.LENGTH_LONG).show();
                }
                else {
                    Log.d("LOGIN: ", "ERROR in loginWithEmailAndPassword", task.getException());
                    Toast.makeText(EmailSignInActivity.this, "Error in login", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
