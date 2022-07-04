package com.tolunaymutlu.tezdeneme2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;
import com.tolunaymutlu.tezdeneme2.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;



import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private String email="",password="";
    private ProgressDialog progressDialog;
    private ActionBar actionBar;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // actionBar
        actionBar = getSupportActionBar();
        actionBar.setTitle("Login");


        //progressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Logging in");
        progressDialog.setCanceledOnTouchOutside(false);

        //firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
        //check user

        checkUser();


        binding.haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });

        binding.LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateData();
            }
        });
    }

    private void checkUser() {
        //check logged in account if its logged in to direct to profile activity
        //get current user

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            startActivity(new Intent(this,ProfileActivity.class));
            finish();

        }
    }

    private void validateData(){
        //get data

        email = binding.loginMail.getText().toString().trim();
        password = binding.loginPassword.getText().toString().trim();

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.loginMail.setError("Please use a valid mail adress");
        }
        // password check
        else if(TextUtils.isEmpty(password)){
            binding.loginPassword.setError("Please type your password");
        }
        //data valid
        else{
            firebaseLogIn();
        }


    }

    private void firebaseLogIn() {
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        String email = firebaseUser.getEmail();
                        String uid = firebaseUser.getUid();

                        Toast.makeText(LoginActivity.this,"Logged In"+email,Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(LoginActivity.this,ProfileActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,""+e.getMessage(),Toast.LENGTH_LONG).show();

                    }
                });

    }
}