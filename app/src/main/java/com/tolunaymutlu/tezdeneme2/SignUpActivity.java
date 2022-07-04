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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.tolunaymutlu.tezdeneme2.databinding.ActivitySignUpBinding;

import java.util.HashMap;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {

    //viewBinding
    private ActivitySignUpBinding binding;
    private String email="",password="";
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private ActionBar actionBar;
    String number,name,surname,attiribute1,attiribute2,attiribute3;
    HashMap<Object,String> hashMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        //progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Account is creating");
        progressDialog.setCanceledOnTouchOutside(false);

        //action bar
        actionBar = getSupportActionBar();
        actionBar.setTitle("Sign Up");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        hashMap = new HashMap<>();




        binding.SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void validateData() {
        email = binding.signupMail.getText().toString().trim();
        password = binding.signupPassword.getText().toString().trim();
        name = binding.userName.getText().toString().trim();
        number = binding.userScNo.getText().toString().trim();
        surname = binding.userSurname.getText().toString().trim();
        attiribute1 = binding.getAt1.getText().toString().toUpperCase();
        attiribute2 = binding.getAt2.getText().toString().toUpperCase();
        attiribute3 = binding.getAt3.getText().toString().toUpperCase();



        //check

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //email check
            binding.signupMail.setError("Please use a valid mail adress");
        }
            // password type check
        else if(TextUtils.isEmpty(password)){
            binding.signupPassword.setError("Please type your password");
        }
        else if(password.length() < 6){
            //password len check
            binding.signupMail.setError("Your password must be at least 7 characters");
        }
        //data valid
        else{
            firebaseSignUp();
        }
    }

    private void firebaseSignUp() {
        //progress dialog
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        progressDialog.dismiss();
                        //user's info
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                        String email = firebaseUser.getEmail();
                        String uid = firebaseUser.getUid();

                        //hashmap for firebase

                        hashMap.put("email",email);
                        hashMap.put("uid",uid);
                        hashMap.put("name",name);
                        hashMap.put("surname",surname);
                        hashMap.put("number",number);
                        hashMap.put("attiributes",attiribute1);
                        hashMap.put("attiribute2",attiribute2);
                        hashMap.put("attiribute3",attiribute3);
                        //database instance
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        //"Users" location
                        DatabaseReference reference = database.getReference("Users");
                        //put data
                        reference.child(uid).setValue(hashMap);




                        Toast.makeText(SignUpActivity.this, "Welcome", Toast.LENGTH_SHORT).show();

                        //activity after logged in

                        startActivity(new Intent(SignUpActivity.this,ProfileActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this , ""+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }
}