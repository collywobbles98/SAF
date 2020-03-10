package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Tutorial1 extends AppCompatActivity {

    //---Firestore---//
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //---Declare Widgets---//
    Button agreeBtn;
    Button dontAgreeBtn;

    //Consent Given (Agree Clicked)
    public void agreeClicked (View view){

        //---Create User in the Firestore User Table---//
        //User ID
        userData.userID_Global = user.getUid();
        String userID = userData.userID_Global;
        //User Email
        String email = userData.email_Global;


        Map<String, Object> user = new HashMap<>();
        user.put("userID", userID);
        user.put("Email", email);
        user.put("Currency", "GBP");//Default Currency

        db.collection("user").document(userID)
                .set(user)//, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully written!");

                        startActivity(new Intent(Tutorial1.this, accountDetails.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error writing document", e);
                    }
                });


    }

    public void dontAgreeClicked (View view) {
        //Remove User From Firebase Auth and return to login page.
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial1);


    }
}
