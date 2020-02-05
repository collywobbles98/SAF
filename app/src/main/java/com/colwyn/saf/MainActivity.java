package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    String userId = user.getUid();



    //---Navigation---//
    public void sellingClicked(View view){

        startActivity(new Intent(MainActivity.this, Selling.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    public void goToProfile(View view){

        userData.userID_Global = user.getUid();

        startActivity(new Intent(MainActivity.this, Profile.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userData.userID_Global = user.getUid();

        //If no user is logged in, take user to login activity.
        if (userData.userID_Global == null){
            startActivity(new Intent(MainActivity.this, LogIn.class));
        }


    }
}
