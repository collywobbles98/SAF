package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class accountDetails extends AppCompatActivity {

    //---Firestore---//
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //---Declare Widgets---//
    TextView userEmailTextView;
    EditText txtFName;
    EditText txtLName;
    EditText txtAddress;
    EditText txtPostcodeZip;
    EditText txtCountyState;
    EditText txtCountry;
    EditText txtPhoneNum;
    //For Progress Bar
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //---Get Widgets---//
        userEmailTextView = findViewById(R.id.userEmailTextView);
        txtFName = findViewById(R.id.fNameEditText);
        txtLName = findViewById(R.id.lnameEditText);
        txtAddress = findViewById(R.id.addressEditText);
        txtPostcodeZip = findViewById(R.id.postcodeZipEditText);
        txtCountyState = findViewById(R.id.countyStateEditText);
        txtCountry = findViewById(R.id.countryEditText);
        txtPhoneNum = findViewById(R.id.phoneNumEditText);
        //For Progress Bar
        linearLayout = findViewById(R.id.linearLayout);


        //---Get User ID & Email from global variable---//
        String userID = userData.userID_Global;
        String email = userData.email_Global;


        try {
            //---Get user data from firestore---//
            DocumentReference docRef = db.collection("user").document(userID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            //---Retrieve data and store as string---//
                            String FSFirstname = document.getString("First Name");
                            String FSLastname = document.getString("Last Name");
                            String FSAddress = document.getString("Address");
                            String FSPostcodeZip = document.getString("Postcode_Zip");
                            String FSCountyState = document.getString("County_State");
                            String FSCountry = document.getString("Country");
                            String FSPhoneNum = document.getString("Phone_Num");
                            String FSCurrency = document.getString("Currency");

                            //---Display in edit texts ---//
                            txtFName.setText(FSFirstname);
                            txtLName.setText(FSLastname);
                            txtAddress.setText(FSAddress);
                            txtPostcodeZip.setText(FSPostcodeZip);
                            txtCountyState.setText(FSCountyState);
                            txtCountry.setText(FSCountry);
                            txtPhoneNum.setText(FSPhoneNum);


                            //--Display User Email---//
                            userEmailTextView.setText(userData.email_Global);

                            //--Store Currency to be used in the save---//
                            userData.currency_Global = FSCurrency;


                            //Hide progress bar and show data
                            linearLayout.setVisibility(View.VISIBLE);
                            //Toast.makeText(accountDetails.this,  "User data found " + FSFirstname , Toast.LENGTH_SHORT).show();
                            //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            //An error has occured, display error message and remove user from page.
                            Toast.makeText(accountDetails.this, "Oops! Looks like something went wrong.", Toast.LENGTH_SHORT).show();
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

        //---Save Button---//
        FloatingActionButton fab = findViewById(R.id.save_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //---Get Values---//
                String userID = userData.userID_Global;
                String email = userData.email_Global;
                String fName = txtFName.getText().toString().trim();
                String lName = txtLName.getText().toString().trim();
                String address = txtAddress.getText().toString().trim();
                String postcodeZip = txtPostcodeZip.getText().toString().trim();
                String countyState = txtCountyState.getText().toString().trim();
                String country = txtCountry.getText().toString().trim();
                String phoneNum = txtPhoneNum.getText().toString().trim();

                //---Check all edit texts are completed---//
                //If statement

                //Save Data to Firestore

                Map<String, Object> user = new HashMap<>();
                user.put("userID", userID);
                user.put("Email", email);
                user.put("First Name", fName);
                user.put("Last Name", lName);
                user.put("Address", address);
                user.put("Postcode_Zip",postcodeZip);
                user.put("County_State", countyState);
                user.put("Country", country);
                user.put("Phone_Num", phoneNum);
                user.put("Currency", userData.currency_Global);


                db.collection("user").document(userID)
                        .set(user)//, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Log.w(TAG, "Error writing document", e);
                            }
                        });

                //---If its an edit from checkout---//
                if (userData.addressEdit_Global){
                    //Return to confirm order
                    startActivity(new Intent(accountDetails.this, confirmorder.class));
                    userData.addressEdit_Global = Boolean.FALSE;
                }
                else{
                    //Return & Return to Profile
                    startActivity(new Intent(accountDetails.this, Profile.class));
                }

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
