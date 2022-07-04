package com.tolunaymutlu.tezdeneme2;



import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tolunaymutlu.tezdeneme2.databinding.ActivityProfileBinding;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private ActionBar actionBar;
    private FirebaseAuth firebaseAuth;
    DatabaseReference Databaseref;
    FirebaseUser currentUser;
    FirebaseDatabase firebaseDatabase;
    String name,surname,studentNumber,studentMail,attiribute1,attiribute2,attiribute3,newAt1,newAt2,newAt3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //actionBar
        SignUpActivity usr = new SignUpActivity();
        actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");

        //firebase auth

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        Databaseref = firebaseDatabase.getReference("Users");


        //logut
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUser();
            }
        });

        binding.jobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,JobsActivity.class));

            }
        });

        //getting data from firebase with email but we can change it to the uid
        Query query = Databaseref.orderByChild("email").equalTo(currentUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    name = "" + ds.child("name").getValue();
                    surname = "" + ds.child("surname").getValue();
                    studentNumber = "" + ds.child("number").getValue();
                    studentMail = "" + ds.child("email").getValue();
                    attiribute1 = "" + ds.child("attiributes").getValue();
                    attiribute2 = "" + ds.child("attiribute2").getValue();
                    attiribute3 = "" + ds.child("attiribute3").getValue();
                    binding.name.setText(name);
                    binding.sname.setText(surname);
                    binding.studenNo.setText(studentNumber);
                    binding.studentMail.setText(studentMail);
                    binding.att1.setText(attiribute1);
                    binding.att2.setText(attiribute2);
                    binding.att3.setText(attiribute3);

                    binding.updateAtt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Map<String,Object> newData = new HashMap<String,Object>();
                            newAt1 = binding.att1.getText().toString().toUpperCase();
                            newAt2 = binding.att2.getText().toString().toUpperCase();
                            newAt3 = binding.att3.getText().toString().toUpperCase();

                            newData.put("attiributes",newAt1);
                            newData.put("attiribute2",newAt2);
                            newData.put("attiribute3",newAt3);
                            try{
                                ds.getRef().updateChildren(newData);
                                Toast.makeText(getApplicationContext(),"Your interests are updated",Toast.LENGTH_LONG).show();

                            }catch(Exception e){
                                Toast.makeText(getApplicationContext(),"Somethings wrong\n update is completed",Toast.LENGTH_LONG).show();

                            }



                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    private void checkUser(){
        //getCurrent user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser == null){
            //user not logged in
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }
        else{
            //user logged in get info

            String email = firebaseUser.getEmail();


        }
    }
}