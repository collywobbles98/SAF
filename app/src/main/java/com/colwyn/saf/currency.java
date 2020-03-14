package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class currency extends AppCompatActivity {
    //---Firestore---//
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //---Declare Widgets---//
    RadioButton GBPRadioButton, EURRadioButton, USDRadioButton, CADRadioButton, AUDRadioButton, JPYRadioButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        //---Get user id--//
        String userID = user.getUid();

        //---Get Widgets---//
        GBPRadioButton = findViewById(R.id.GBPRadioButton);
        EURRadioButton = findViewById(R.id.EURRadioButton);
        USDRadioButton = findViewById(R.id.USDRadioButton);
        CADRadioButton = findViewById(R.id.CADRadioButton);
        AUDRadioButton = findViewById(R.id.AUDRadioButton);
        JPYRadioButton = findViewById(R.id.JPYRadioButton);

        try{
            //Try get data from firestore
            //---Get user data from firestore---//
            DocumentReference docRef = db.collection("user").document(userID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            //---Retrieve data and store as string---//
                            String FSCurrency = document.getString("Currency");

                            //---Check appropriate radio button---//
                            if (FSCurrency.equals("(GBP)")){
                                GBPRadioButton.setChecked(true);
                            }
                            else if (FSCurrency.equals("(EUR)")){
                                EURRadioButton.setChecked(true);
                            }
                            else if (FSCurrency.equals("(USD)")){
                                USDRadioButton.setChecked(true);
                            }
                            else if (FSCurrency.equals("(CAD)")){
                                CADRadioButton.setChecked(true);
                            }
                            else if (FSCurrency.equals("(AUD)")){
                                AUDRadioButton.setChecked(true);
                            }
                            else if (FSCurrency.equals("(JPY)")){
                                JPYRadioButton.setChecked(true);
                            }
                            else{
                                Toast.makeText(currency.this, "Please Select your currency.", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            //An error has occured, display error message and remove user from page.
                            Toast.makeText(currency.this, "Oops! Looks like something went wrong.", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        //Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

        }
        catch(Exception e){
            //Error Occured Display error message
            Toast.makeText(currency.this, "Oops! Looks like something went wrong.", Toast.LENGTH_SHORT).show();

        }

        //---Save Button---//
        FloatingActionButton fab = findViewById(R.id.saveCurrencyButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //---Get user id--//
                String userID = user.getUid();

                //---Get Values---//
                String currency;

                if (GBPRadioButton.isChecked()){
                    currency = "(GBP)";
                }
                else if (EURRadioButton.isChecked()){
                    currency = "(EUR)";
                }
                else if (USDRadioButton.isChecked()){
                    currency = "(USD)";
                }
                else if (CADRadioButton.isChecked()){
                    currency = "(CAD)";
                }
                else if (AUDRadioButton.isChecked()){
                    currency = "(AUD)";
                }
                else if (JPYRadioButton.isChecked()){
                    currency = "(JPY)";
                }
                else{
                    currency = null;
                }

                DocumentReference currencyRef = db.collection("user").document(userID);

                currencyRef
                        .update("Currency", currency)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Log.w(TAG, "Error updating document", e);
                            }
                        });

                //Return & Return to Profile
                startActivity(new Intent(currency.this, Profile.class));

            }
        });

    }
}
