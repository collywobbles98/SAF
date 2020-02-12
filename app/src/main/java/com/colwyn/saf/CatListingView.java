package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class CatListingView extends AppCompatActivity {

    //---Firestore---//
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //---Declare Widgets---//
    TextView titleTextView, priceTextView, deliveryTextView, conditionTextView, brandTextView, descriptionTextView, nameTextView, locationTextView, numberTextView, emailTextView;
    ImageView catItemImageView;

    //---Nav---//
    public void backClicked (View view){
        //Take user back to catalogue and remove clicked Item Variable
        userData.catItemClicked_Global = null;
        userData.itemSeller_Global = null;
        startActivity(new Intent(CatListingView.this, MainActivity.class));
    }

//---Add Items to basket---//
    public void addtoBasketClicked (View view){

        //Declare Widgets
        titleTextView = findViewById(R.id.titleTextView);
        priceTextView = findViewById(R.id.priceTextView);
        deliveryTextView = findViewById(R.id.deliveryTextView);

        //Add item to users basket

            Map<String, Object> basket_items = new HashMap<>();
            basket_items.put("UserID", user.getUid());
            basket_items.put("Item", userData.catItemClicked_Global);
            basket_items.put("ImageURL", userData.imageURLBasket_Global);
            basket_items.put("Title", titleTextView.getText().toString().trim());
            basket_items.put("Price", priceTextView.getText().toString().trim());
            basket_items.put("Delivery_Notes", deliveryTextView.getText().toString().trim());



            // Add a new document with a generated ID
            db.collection("basket_items")
                    .add(basket_items)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            //Go back to Main Activity
                            Toast.makeText(CatListingView.this, "Added To Basket!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CatListingView.this, MainActivity.class));



                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Log.w(TAG, "Error adding document", e);
                            Toast.makeText(CatListingView.this, "Cannot add Item at this time.", Toast.LENGTH_SHORT).show();
                        }
                    });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_listing_view);

        //---Load Data Into Activity---//
        //Get Widgets
        titleTextView = findViewById(R.id.titleTextView);
        priceTextView = findViewById(R.id.priceTextView);
        deliveryTextView = findViewById(R.id.deliveryTextView);
        conditionTextView = findViewById(R.id.conditionTextView);
        brandTextView = findViewById(R.id.brandTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        nameTextView = findViewById(R.id.nameTextView);
        locationTextView = findViewById(R.id.locationTextView);
        numberTextView = findViewById(R.id.numberTextView);
        emailTextView = findViewById(R.id.emailTextView);

        catItemImageView = findViewById(R.id.catItemImageView);

        //Get ID of Item Clicked to run search
        String documentID = userData.catItemClicked_Global;


        //Get Data From Firestore
        try{

            //Item Data

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
                            userData.imageURLBasket_Global = document.getString("ImageURL");
                            userData.itemSeller_Global = document.getString("UserID");

                            //Display in edit texts
                            titleTextView.setText(FSTitle);
                            priceTextView.setText(FSPrice);
                            deliveryTextView.setText(FSDelivery);
                            conditionTextView.setText(FSCondition);
                            brandTextView.setText(FSBrand);
                            descriptionTextView.setText(FSDescription);

                            //Display Photo
                            Picasso.get().load(userData.imageURLBasket_Global).into(catItemImageView);
                            //Toast.makeText(CatListingView.this, "Seller: " + userData.itemSeller_Global, Toast.LENGTH_SHORT).show();
                            getSellerData();

                        } else {
                            //Document Doesnt Exist
                            Toast.makeText(CatListingView.this, "Oops! Looks like something went wrong. Getting UserID", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        //Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });





        }
        catch (Exception e){
            Toast.makeText(CatListingView.this, "Oops! Looks like something went wrong getting item details.", Toast.LENGTH_SHORT).show();

        }
    }

    private void getSellerData() {
        try {
            DocumentReference docRef2 = db.collection("user").document(userData.itemSeller_Global);
            docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            //Retrieve data and store as string
                            String FSFirstName = document.getString("First Name");
                            String FSlastName = document.getString("Last Name");
                            String FSCountyState = document.getString("County_State");
                            String FSCountry = document.getString("Country");
                            String FSNumber = document.getString("Phone_Num");
                            String FSEmail = document.getString("Email");

                            //Display in edit texts
                            nameTextView.setText(FSFirstName + " " + FSlastName);
                            locationTextView.setText("From " + FSCountyState + ", " + FSCountry);
                            numberTextView.setText("Contact Number: " + FSNumber);
                            emailTextView.setText("Email: " + FSEmail);

                        } else {
                            //Document Doesn't Exist
                            Toast.makeText(CatListingView.this, "Oops! Looks like something went wrong.", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        //Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
        catch (Exception e){
            Toast.makeText(CatListingView.this, "Oops! Looks like something went wrong getting seller details.", Toast.LENGTH_SHORT).show();

        }

    }
}
