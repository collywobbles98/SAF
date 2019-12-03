package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //Declare Widgets
    TextView usernameTextView;

    //Declare Variables
    //String username = user.getUid();



    //---Navigation---//

    //Home Activity
     public void goToHome (View view) {

         startActivity(new Intent(Profile.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));

     }

     //Selling Activity
     public void sellingClicked(View view){

         startActivity(new Intent(Profile.this, Selling.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
     }

     public void testclicked (View view){

         usernameTextView = findViewById(R.id.usernameTextView);
         usernameTextView.setText(userData.userID_Global);

         FirebaseAuth.getInstance().signOut();
         userData.userID_Global = null;
         startActivity(new Intent(Profile.this, LogIn.class));

     }

     public void personalDetailsClicked (View view){
         //Take user to edit details activity
         startActivity(new Intent(Profile.this, accountDetails.class));
     }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        usernameTextView = findViewById(R.id.usernameTextView);
        usernameTextView.setText(userData.firstname_Global + " " + userData.lastname_Global);


    }
}
