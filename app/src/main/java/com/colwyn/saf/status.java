package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

public class status extends AppCompatActivity {

    //---Firebase & Firestore---//
    private StorageReference mStorageRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    //Firestore Collection Reference
    private CollectionReference collectionReference = db.collection("items_sold");

    //---Declare Widgets---//
    TextView orderIDTextView, titleTextView, quantityTextView, buyerNameTextView, buyerAddressTextView, statusTextView;
    RadioButton processingRadioButton, packingRadioButton, readyForShipmentButton, sentRadioButton;
    Button updateStatusButton, saveButton;
    CardView updateStatusCardView;

    //---Back Button---//
    public void backClicked(View View){
        startActivity(new Intent(status.this, incomingorders.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        //---Get Widgets---//
        orderIDTextView = findViewById(R.id.orderIDTextView);
        titleTextView = findViewById(R.id.titleTextView);
        quantityTextView = findViewById(R.id.quantityTextView);
        buyerNameTextView = findViewById(R.id.buyerNameTextView);
        buyerAddressTextView = findViewById(R.id.buyerAddressTextView);
        statusTextView = findViewById(R.id.statusTextView);

        processingRadioButton = findViewById(R.id.processingRadioButton);
        packingRadioButton = findViewById(R.id.packingRadioButton);
        readyForShipmentButton = findViewById(R.id.readyForShipmentButton);
        sentRadioButton = findViewById(R.id.sentRadioButton);

        updateStatusButton = findViewById(R.id.updateStatusButton);
        saveButton = findViewById(R.id.saveButton);

        updateStatusCardView = findViewById(R.id.updateStatusCardView);

        //---Load Data into Activity---//
        try {
            DocumentReference docRef = db.collection("items_sold").document(userData.SellingOrderClicked_Global);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            //---Retrieve data and store as string---//
                            String FSOrderID = document.getString("OrderID");
                            String FSTitle = document.getString("ItemTitle");
                            String FSQuantity = document.getString("Quantity");
                            String FSBuyerName = document.getString("BuyerName");
                            String FSBuyerAddress = document.getString("BuyerAddress");
                            String FSStatus = document.getString("Status");

                            //---Display data---//
                            orderIDTextView.setText(FSOrderID);
                            titleTextView.setText("Title: " + FSTitle);
                            quantityTextView.setText("Quantity: " + FSQuantity);
                            buyerNameTextView.setText(FSBuyerName);
                            buyerAddressTextView.setText(FSBuyerAddress);
                            statusTextView.setText(FSStatus);
                            //Check Radio Buttons
                            if (FSStatus.equals("Processing")){
                                processingRadioButton.setChecked(true);
                            }
                            else if (FSStatus.equals("Packing")){
                            packingRadioButton.setChecked(true);
                            }
                            else if (FSStatus.equals("Ready For Shipment")){
                                readyForShipmentButton.setChecked(true);
                            }
                            else if (FSStatus.equals("Sent")){
                                sentRadioButton.setChecked(true);
                            }
                            else{
                                //Error
                            }


                        } else {
                            //An error has occured, display error message and remove user from page.
                            Toast.makeText(status.this, "Oops! Looks like something went wrong.", Toast.LENGTH_SHORT).show();
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

        //---Update Status Button---//
        updateStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Show CardView to update Status
                updateStatusCardView.setVisibility(View.VISIBLE);

            }
        });

        //---Save Status---//
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get Data from radioButtons
                String newStatus;

                if (processingRadioButton.isChecked()){
                    newStatus = "Processing";
                }
                else if (packingRadioButton.isChecked()){
                    newStatus = "Packing";
                }
                else if (readyForShipmentButton.isChecked()){
                    newStatus = "Ready For Shipment";
                }
                else if (sentRadioButton.isChecked()){
                    newStatus = "Sent";
                }
                else{
                    return;
                }

                if (newStatus.isEmpty()){
                    Toast.makeText(status.this, "Please add a status.", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Update Status in firestore
                    DocumentReference currencyRef = db.collection("items_sold").document(userData.SellingOrderClicked_Global);

                    currencyRef
                            .update("Status", newStatus)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //Log.d(TAG, "DocumentSnapshot successfully updated!");
                                    Toast.makeText(status.this, "Status Updated.", Toast.LENGTH_SHORT).show();
                                    //Hide update Card View
                                    updateStatusCardView.setVisibility(View.GONE);
                                    //Reload Activity
                                    finish();
                                    startActivity(getIntent());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //Log.w(TAG, "Error updating document", e);
                                    Toast.makeText(status.this, "Oops! Looks like something went wrong.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }
        });

    }
}
