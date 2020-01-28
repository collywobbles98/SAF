package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class listingView extends AppCompatActivity {

    //---Firestore---//
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //---Declare Widgets---//
    TextView titleTextView, priceTextView, deliveryTextView, conditionTextView, brandTextView, descriptionTextView;
    ImageView itemImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //---Load Data into Activity---//
        //Get Widgets
        titleTextView = findViewById(R.id.titleTextView);
        priceTextView = findViewById(R.id.priceTextView);
        deliveryTextView = findViewById(R.id.deliveryTextView);
        conditionTextView = findViewById(R.id.conditionTextView);
        brandTextView = findViewById(R.id.brandTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        itemImageView = findViewById(R.id.itemImageView);

        //Get ID of Item Clicked to run search
        String documentID = userData.userItemClicked_Global;

        //Get Data from Firestore
        try{

            DocumentReference docRef = db.collection("listings").document(documentID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            //Retrieve data and store as string
                            String FSTitle = document.getString("Title");
                            String FSPrice = document.getString("Price");
                            String FSDelivery = document.getString("Delivery_Notes");
                            String FSCondition = document.getString("Condition");
                            String FSBrand = document.getString("Brand");
                            String FSDescription = document.getString("Description");
                            String FSImageURL = document.getString("ImageURL");

                            //Display in edit texts
                            titleTextView.setText(FSTitle);
                            priceTextView.setText(FSPrice);
                            deliveryTextView.setText(FSDelivery);
                            conditionTextView.setText(FSCondition);
                            brandTextView.setText(FSBrand);
                            descriptionTextView.setText(FSDescription);

                            //Display Photo
                            Picasso.get().load(FSImageURL).into(itemImageView);

                        } else {
                            //Document Doesnt Exist
                            Toast.makeText(listingView.this, "Oops! Looks like something went wrong.", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        //Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

        }
        catch (Exception e){
            Toast.makeText(listingView.this, "Oops! Looks like something went wrong.", Toast.LENGTH_SHORT).show();

        }




        //---Edit Button---//
        FloatingActionButton editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                //Set variable to let addItem know it is an edit not a new listing
                userData.listingEdit_Global = Boolean.TRUE;

                //Start addItem Activity
                startActivity(new Intent(listingView.this, addItem.class));

            }
        });

        //---Cancel Button---//
        FloatingActionButton cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //.Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                startActivity(new Intent(listingView.this, Selling.class));
            }
        });

    }

}
