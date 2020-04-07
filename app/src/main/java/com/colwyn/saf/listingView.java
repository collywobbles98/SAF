package com.colwyn.saf;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class listingView extends AppCompatActivity {

    //---Firestore---//
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //---Declare Widgets---//
    TextView titleTextView, priceTextView, deliveryTextView, conditionTextView, brandTextView, descriptionTextView, postedTextView, stockTextView;
    ImageView itemImageView, newImageView;

    //---Back Button---//
    public void backClicked(View view){
        startActivity(new Intent(listingView.this, Selling.class));
    }

    //---Delete DialogBox---//
    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Delete Listing")
                .setMessage("Do you want to Delete this listing titled " + titleTextView.getText().toString() + "?")
                .setIcon(R.drawable.ic_delete_forever_black_24dp)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Delete Listing
                        db.collection("listings").document(userData.userItemClicked_Global)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        startActivity(new Intent(listingView.this, Selling.class));
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(listingView.this, "An error has occurred, please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                });



                        dialog.dismiss();

                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();

        return myQuittingDialogBox;
    }

    //---No Stock Dialogbox---//

    private AlertDialog AskOption2()
    {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Stock Alert")
                .setMessage("This item has been sold out. Please add more stock or delete this listing.")
                .setIcon(R.drawable.ic_delete_forever_black_24dp)

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        dialog.dismiss();

                    }

                })
                .create();

        return myQuittingDialogBox;
    }

    //Delete Button
    public void deleteClicked (View view){
        AlertDialog diaBox = AskOption();
        diaBox.show();
    }

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
        postedTextView = findViewById(R.id.postedTextView);
        stockTextView = findViewById(R.id.stockTextView);
        newImageView = findViewById(R.id.newImageView);


        //Get ID of Item Clicked to run search
        final String documentID = userData.userItemClicked_Global;

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
                            String FSCurrency = document.getString("Currency");
                            String FSSymbol = document.getString("Symbol");
                            String FSTimeStamp = document.getString("TimeStamp");
                            String FSStock = document.getString("Stock");

                            //Display in edit texts
                            titleTextView.setText(FSTitle);
                            priceTextView.setText(FSSymbol + FSPrice + " (" + FSCurrency + ")");
                            deliveryTextView.setText(FSDelivery);
                            conditionTextView.setText(FSCondition);
                            brandTextView.setText(FSBrand);
                            descriptionTextView.setText(FSDescription);

                            //---Get Days Since Posted---//
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            String inputString1 = FSTimeStamp;
                            String inputString2 = simpleDateFormat.format(new Date());

                            try {
                                Date date1 = simpleDateFormat.parse(inputString1);
                                Date date2 = simpleDateFormat.parse(inputString2);
                                long diff = date2.getTime() - date1.getTime();
                                //Display Days since posted
                                postedTextView.setSingleLine(false);
                                if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) == 0){
                                    newImageView.setVisibility(View.VISIBLE);
                                    postedTextView.setVisibility(View.INVISIBLE);
                                }
                                else if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) == 1){
                                    newImageView.setVisibility(View.GONE);
                                    postedTextView.setText("POSTED \n" + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + " DAY AGO");
                                }
                                else{
                                    newImageView.setVisibility(View.GONE);
                                    postedTextView.setText("POSTED \n" + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + " DAYS AGO");
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            //Display Stock
                            if(FSStock.equals("unlimited")){
                                stockTextView.setText("Available");
                                stockTextView.setTextColor(Color.parseColor("#53d769"));
                            }
                            else {
                                int stockNum = Integer.parseInt(FSStock);
                                if (stockNum >= 10) {
                                    stockTextView.setText(stockNum + " Available");
                                    stockTextView.setTextColor(Color.parseColor("#53d769"));
                                } else if (stockNum < 10 && stockNum > 0) {
                                    stockTextView.setText("Only " + stockNum + " Remaining");
                                    stockTextView.setTextColor(Color.parseColor("#fd9426"));
                                } else {
                                    stockTextView.setText("Sold Out");
                                    stockTextView.setTextColor(Color.parseColor("#fc3d39"));

                                    //Alert User to Delete or Edit Item.
                                    AlertDialog diaBox = AskOption2();
                                    diaBox.show();

                                }
                            }


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


    }


}
