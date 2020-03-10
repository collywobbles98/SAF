package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    //Basket Activity
    public void basketClicked(View view){

        startActivity(new Intent(Profile.this, basket.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

     public void logoutClicked (View view){
         usernameTextView = findViewById(R.id.usernameTextView);
         usernameTextView.setText(userData.userID_Global);
         FirebaseAuth.getInstance().signOut();
         userData.userID_Global = null;
         startActivity(new Intent(Profile.this, LogIn.class));
     }

     public void CurrencyClicked (View view){
         //Take user to edit details activity
         startActivity(new Intent(Profile.this, currency.class));
     }

     public void personalDetailsClicked (View view){
         //Take user to edit details activity
         startActivity(new Intent(Profile.this, accountDetails.class));
     }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userData.userID_Global = user.getUid();
        String userID = user.getUid();

        //--Get User Email, firstname and lastname from firestore---//

        try {
            DocumentReference docRef = db.collection("user").document(userID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            //---Retrieve data and store as string---//
                            String FSUserEmail = document.getString("Email");
                            String FSFirstname = document.getString("First Name");
                            String FSLastname = document.getString("Last Name");

                            //---Save to global variable---//
                            userData.email_Global = FSUserEmail;
                            userData.firstname_Global = FSFirstname;
                            userData.lastname_Global = FSLastname;

                            //Display users name
                            usernameTextView = findViewById(R.id.usernameTextView);
                            usernameTextView.setText(FSFirstname + " " + FSLastname);



                        } else {
                            //An error has occured, display error message and remove user from page.
                            Toast.makeText(Profile.this, "Oops! Looks like something went wrong.", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(accountDetails.this, Profile.class));
                            //Log.d(TAG, "No such document");
                        }
                    } else {
                        //Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }

        catch(Exception e) {
            //Error
        }



    }
}
