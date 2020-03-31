package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WantedAdView extends AppCompatActivity {

    //---Firebase & Firestore---//
    private StorageReference mStorageRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //---Declare Widgets---//
    TextView wantedAdDateTextView, wantedAdTitleTextView, wantedAdDescriptionTextView;
    TextView posterNameTextView, posterLocationTextView, posterNumberTextView, posterEmailtextView;
    CardView messagePosterCardView;
    Button deleteWantedAdButton;

    //---Navigation---//
    public void backClicked(View view){
        startActivity(new Intent(WantedAdView.this, wanted.class));
    }

    //---Delete Ad Button---//
    public void deleteADClicked(View view) {
        //Delete Listing
        db.collection("wanted_ads").document(userData.WantedAdClicked_Global)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(WantedAdView.this, "Ad Deleted!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(WantedAdView.this, wanted.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(WantedAdView.this, "An error has occurred, please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wanted_ad_view);

        //---Get Widgets---//
        wantedAdDateTextView = findViewById(R.id.wantedAdDateTextView);
        wantedAdTitleTextView = findViewById(R.id.wantedAdTitleTextView);
        wantedAdDescriptionTextView = findViewById(R.id.wantedAdDescriptionTextView);

        posterNameTextView = findViewById(R.id.posterNameTextView);
        posterLocationTextView = findViewById(R.id.posterLocationTextView);
        posterNumberTextView = findViewById(R.id.posterNumberTextView);
        posterEmailtextView = findViewById(R.id.posterEmailTextView);
        messagePosterCardView = findViewById(R.id.messagePosterCardView);

        deleteWantedAdButton = findViewById(R.id.deleteWantedAdButton);

        //---Load Data into Activity---//

        try{
            //Try get data from firestore
            //---Get user data from firestore---//
            DocumentReference docRef = db.collection("wanted_ads").document(userData.WantedAdClicked_Global);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            //---Retrieve data and store as string---//
                            final String FSPosterID = document.getString("PosterID");
                            String FSTitle = document.getString("Title");
                            String FSDescription = document.getString("Description");
                            String FSTimeStamp = document.getString("TimeStamp");

                            //Display In Activity
                            wantedAdDateTextView.setText(FSTimeStamp);
                            wantedAdTitleTextView.setText(FSTitle);
                            wantedAdDescriptionTextView.setText(FSDescription);

                            //Save to Global Variable for messaging purposes
                            userData.wantedPosterID_Global = FSPosterID;


                            //---Get Poster Information---//
                            try{
                                DocumentReference docRef = db.collection("user").document(FSPosterID);
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                //---Retrieve data and store as string---//
                                                String FSFirstName = document.getString("First Name");
                                                String FSlastName = document.getString("Last Name");
                                                String FSCountyState = document.getString("County_State");
                                                String FSCountry = document.getString("Country");
                                                String FSNumber = document.getString("Phone_Num");
                                                String FSEmail = document.getString("Email");

                                                //Display in Activity
                                                posterNameTextView.setText(FSFirstName + " " + FSlastName);
                                                posterLocationTextView.setText("From " + FSCountyState + ", " + FSCountry);
                                                posterNumberTextView.setText("Contact Number: " + FSNumber);
                                                posterEmailtextView.setText("Email: " + FSEmail);

                                                //If this is the current users Item. Allow them to display it
                                                if (FSPosterID.equals(user.getUid())){
                                                    //Show Delete Button
                                                    deleteWantedAdButton.setVisibility(View.VISIBLE);
                                                }
                                                else{
                                                    //Dont Show Delete Button
                                                }

                                            }
                                            else {
                                                //An error has occured, display error message and remove user from page.
                                                Toast.makeText(WantedAdView.this, "Oops! Looks like something went wrong.", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            //Log.d(TAG, "get failed with ", task.getException());
                                        }
                                    }
                                });

                            }
                            catch(Exception e){
                                //Error Occured Display error message
                                Toast.makeText(WantedAdView.this, "Oops! Looks like something went wrong.", Toast.LENGTH_SHORT).show();

                            }



                        }
                        else {
                            //An error has occured, display error message and remove user from page.
                            Toast.makeText(WantedAdView.this, "Oops! Looks like something went wrong.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

        }
        catch(Exception e){
            //Error Occured Display error message
            Toast.makeText(WantedAdView.this, "Oops! Looks like something went wrong.", Toast.LENGTH_SHORT).show();

        }

        //---Messages---//
        messagePosterCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the Viewer is the poster remove the option to message
                if (userData.wantedPosterID_Global.equals(user.getUid())) {
                    Toast.makeText(WantedAdView.this, "This is your Ad.", Toast.LENGTH_SHORT).show();
                } else {
                    //Not the poster so allow messages
                    //Create the messages
                    //Save user 1 (Current user) save user 2 (Seller) and a starter message ("Send a message")
                    //Make Document ID = user1id + user2id this can be checked for duplicates with two if statements

                    //Check this messages has not been created before

                    DocumentReference docRef = db.collection("chats").document(user.getUid() + userData.wantedPosterID_Global);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    //Take User To Messages
                                    userData.chatClicked_Global = user.getUid()+userData.wantedPosterID_Global;
                                    userData.chatOtherUser_Global = posterNameTextView.getText().toString().trim();
                                    startActivity(new Intent(WantedAdView.this, messages.class));
                                } else {
                                    //Check for the reverse

                                    DocumentReference docRef = db.collection("chats").document(userData.wantedPosterID_Global + user.getUid());
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    //Take User To Messages
                                                    userData.chatClicked_Global = userData.wantedPosterID_Global + user.getUid();
                                                    userData.chatOtherUser_Global = posterNameTextView.getText().toString().trim();
                                                    startActivity(new Intent(WantedAdView.this, messages.class));
                                                } else {
                                                    //No Chat Exists Make the messages
                                                    getUser1Details();

                                                    //Open the messages

                                                }
                                            } else {
                                                //Log.d(TAG, "get failed with ", task.getException());
                                            }
                                        }
                                    });


                                }
                            } else {
                                //Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
                }
            }
        });

    }

    private void getUser1Details(){

        //Get User 1 Name
        DocumentReference docRef2 = db.collection("user").document(user.getUid());
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        //Retrieve data and store as string
                        userData.FSUser1FN = document.getString("First Name");
                        userData.FSUser1LN = document.getString("Last Name");
                        userData.FSUser1Initials = ("" + userData.FSUser1FN.charAt(0) + userData.FSUser1LN.charAt(0));
                        getUser2Details();


                    } else {
                        //Document Doesn't Exist
                        Toast.makeText(WantedAdView.this, "Oops! Looks like something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }

    private void getUser2Details(){

        //get User 2 Name
        DocumentReference docRef3 = db.collection("user").document(userData.wantedPosterID_Global);
        docRef3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        //Retrieve data and store as string
                        userData.FSUser2FN = document.getString("First Name");
                        userData.FSUser2LN = document.getString("Last Name");
                        userData.FSUser2Initials = ("" + userData.FSUser2FN.charAt(0) + userData.FSUser2LN.charAt(0));
                        makeChat();

                    } else {
                        //Document Doesn't Exist
                        Toast.makeText(WantedAdView.this, "Oops! Looks like something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    private void makeChat(){
        //Save Data
        Map<String, Object> chats = new HashMap<>();
        chats.put("User1", user.getUid());
        chats.put("User1FirstName", userData.FSUser1FN);
        chats.put("User1LastName", userData.FSUser1LN);
        chats.put("User1Initials", userData.FSUser1Initials);

        chats.put("User2", userData.wantedPosterID_Global);
        chats.put("User2FirstName", userData.FSUser2FN);
        chats.put("User2LastName", userData.FSUser2LN);
        chats.put("User2Initials", userData.FSUser2Initials);
        SimpleDateFormat formatter= new SimpleDateFormat("HH:mm:ss 'at' yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        chats.put("TimeStamp", date.toString().trim());

        chats.put("LastMessage", "Send a Message");

        db.collection("chats").document(user.getUid()+userData.wantedPosterID_Global)
                .set(chats)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully written!");
                        //Go to Chat
                        userData.chatClicked_Global = user.getUid()+userData.itemSeller_Global;
                        userData.chatOtherUser_Global = posterNameTextView.getText().toString().trim();
                        startActivity(new Intent(WantedAdView.this, messages.class));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error writing document", e);
                        Toast.makeText(WantedAdView.this, "Oops! Looks like something went wrong getting seller details.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
