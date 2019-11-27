package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
    private static final String USERID_KEY = "userID";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //---Declare Widgets---//
    TextView userIDTextView;
    EditText txtFName;
    EditText txtLName;
    EditText txtAddress;
    EditText txtPostcodeZip;
    EditText txtCountyState;
    EditText txtCountry;
    EditText txtPhoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //---Show Data---//
        //userIDTextView.setText(userData.userID_Global);

        //Try go get data

        //---Get Widgets---//
        userIDTextView = findViewById(R.id.userIDTextView);

        //---Get Values---//
        String userID = userData.userID_Global;
        //--Set Values---//
        userIDTextView.setText(userID);


        DocumentReference docRef = db.collection("user").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String Testname = document.getString("First Name");
                        //txtFName.setText(Testname + " ");
                        Toast.makeText(accountDetails.this,  "User data found " + Testname , Toast.LENGTH_SHORT).show();
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        //Log.d(TAG, "No such document");
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        //---Save Button---//
        FloatingActionButton fab = findViewById(R.id.save_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //---Get Widgets---//
                userIDTextView = findViewById(R.id.userIDTextView);
                txtFName = findViewById(R.id.fNameEditText);
                txtLName = findViewById(R.id.lnameEditText);
                txtAddress = findViewById(R.id.addressEditText);
                txtPostcodeZip = findViewById(R.id.postcodeZipEditText);
                txtCountyState = findViewById(R.id.countyStateEditText);
                txtCountry = findViewById(R.id.countryEditText);
                txtPhoneNum = findViewById(R.id.phoneNumEditText);

                //---Get Values---//
                String userID = userData.userID_Global;
                String fName = txtFName.getText().toString().trim();
                String lName = txtLName.getText().toString().trim();
                String address = txtAddress.getText().toString().trim();
                String postcodeZip = txtPostcodeZip.getText().toString().trim();
                String countyState = txtCountyState.getText().toString().trim();
                String country = txtCountry.getText().toString().trim();
                String phoneNum = txtPhoneNum.getText().toString().trim();



                //Save Data to Firestore

                Map<String, Object> user = new HashMap<>();
                user.put("userID", userID);
                user.put("First Name", fName);
                user.put("Last Name", lName);
                user.put("Address", address);
                user.put("Postcode_Zip",postcodeZip);
                user.put("County_State", countyState);
                user.put("Country", country);
                user.put("Phone Num", phoneNum);


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





                //Return & Return to Profile
                startActivity(new Intent(accountDetails.this, Profile.class));


                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
