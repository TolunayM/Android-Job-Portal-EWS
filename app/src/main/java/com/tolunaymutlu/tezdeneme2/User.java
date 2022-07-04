package com.tolunaymutlu.tezdeneme2;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class User {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference Databasereferen = firebaseDatabase.getReference("Users");

    private String name, surname, number,attiribute,attiribute2,attiribute3;

    public String getUserThings(){
        Query query2 = Databasereferen.orderByChild("email").equalTo(currentUser.getEmail());
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    attiribute = "" + ds.child("attiributes").getValue();
                    attiribute2 = "" + ds.child("attiribute2").getValue();
                    attiribute3 = "" + ds.child("attiribute3").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return attiribute;
    }
    public String getAttiribute(){
        return attiribute;
    }
    public String getAttiribute2(){
        return attiribute2;
    }
    public String getAttiribute3(){
        return attiribute3;
    }
}
