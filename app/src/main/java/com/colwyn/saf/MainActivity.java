package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    EditText editText2;
    TextView useridTextView;
    String userId = user.getUid();

    public void getuserid(View view){
        Toast.makeText(MainActivity.this, userId + " ", Toast.LENGTH_SHORT).show();
        useridTextView = findViewById(R.id.userIdTextView);
        useridTextView.setText(userData.userID_Global);
    }

    public void taketotest(View view){

        startActivity(new Intent(MainActivity.this, testactivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    //---Navigation---//
    public void sellingClicked(View view){

        startActivity(new Intent(MainActivity.this, Selling.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    public void goToProfile(View view){

        userData.userID_Global = user.getUid();

        startActivity(new Intent(MainActivity.this, Profile.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    public void scrollingLink(View view){
        startActivity(new Intent(MainActivity.this, ScrollingActivity.class));
    }

    public void writeClicked(View view){

        editText2 = findViewById(R.id.editText2);

        String messageText = (editText2.getText().toString().trim());

        Map<String, Object> Message = new HashMap<>();
        Message.put("Message", messageText);

        // Add a new document with a generated ID
        db.collection("Message")
                .add(Message)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error adding document", e);
                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

                        } else {
                            //An error has occured, display error message and remove user from page.
                            Toast.makeText(MainActivity.this, "Oops! Looks like something went wrong.", Toast.LENGTH_SHORT).show();
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


        //If no user is logged in, take user to login activity.
        if (userData.userID_Global == null){
            startActivity(new Intent(MainActivity.this, LogIn.class));
        }


    }
}
