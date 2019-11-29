package com.example.galeford;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    EditText mailid,pswd,mobile,username;
    CheckBox female,male;
    Button signupbtn;
    TextInputLayout nameLayout;
    Toolbar toolbar;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        db = FirebaseFirestore.getInstance();

        mailid = findViewById(R.id.emailid);
        pswd = findViewById(R.id.password);
        mobile = findViewById(R.id.phoneno);
        username = findViewById(R.id.username);
//
//        female = findViewById(R.id.female);
//        male = findViewById(R.id.male);
//
        signupbtn = findViewById(R.id.signupSubmitBtn);
//        nameLayout = findViewById(R.id.namelayout);

        toolbar = findViewById(R.id.galeford_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Signup");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailid = mailid.getText().toString().toLowerCase().trim();
                String password = pswd.getText().toString().trim();
                String contact = mobile.getText().toString().trim();
                String fullname = username.getText().toString().trim();

//                String chkbox;

//                if (fullname.equals("") || fullname.length() == 8)
//                    nameLayout.setError("Enter your full name.");
//
//                if(female.isChecked()){
//                    chkbox = female.getText().toString().trim();
//                }
//
//                if(male.isChecked()){
//                    chkbox = male.getText().toString().trim();
//                }

                signupDataSave(emailid,fullname,password,contact);
            }
        });
    }


    public void signupDataSave(String emailid,String fullname,String password,String contact){

        Map<String, Object> signupdb = new HashMap<>();

        signupdb.put("usrename",fullname);
        signupdb.put("emailid", emailid);
        signupdb.put("password",password);
        signupdb.put("contact",contact);

        db.collection("signup")
                .add(signupdb)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Log.d("SIGNUP LOG", "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(SignupActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Error: ", "DocumentSnapshot added with ID: ",e);
                        Toast.makeText(SignupActivity.this, "Not Saved", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
