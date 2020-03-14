package com.colwyn.saf;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.colwyn.saf.model.BasketItem;
import com.colwyn.saf.ui.BasketRecyclerAdapter;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class basket extends AppCompatActivity {
    //PayPal ClientID
    //ARMgrdew6mxqJusWWx1XBsD4gR12pokkrnv6FJGsv1UaqCsghSy2XXxYJBhv_NlZe158OW9ohB8NY-hq

    //---Firebase & Firestore---//
    private StorageReference mStorageRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    //Firestore Collection Reference
    private CollectionReference collectionReference = db.collection("basket_items");

    //---Declare Widgets---//
    private List<BasketItem> BasketItemList;
    private RecyclerView basketRecyclerView;
    private BasketRecyclerAdapter basketRecyclerAdapter;


    ImageView emptyImageView;
    TextView emptyTextView;
    TextView symbolTextView, subtotalTextView, currencyTextView;


    //---Navigation---//
    public void goToHome (View view) {
        startActivity(new Intent(basket.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }
    //Selling Activity
    public void sellingClicked(View view){
        startActivity(new Intent(basket.this, Selling.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }
    //profile activity
    public void profileClicked(View view){
        startActivity(new Intent(basket.this, Profile.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    //---Checkout Button---//
    public void checkoutClicked(View view){
        startActivity(new Intent(basket.this, paymentOptions.class));
    }

    //---Empty Basket Button---//
    public void emptyBasketClicked (View view){
        //Remove record if userid matches
        db.collection("basket_items")
                .whereEqualTo("UserID", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                //Delete this document
                                db.collection("basket_items").document(document.getId())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                //Reload Activity
                                                finish();
                                                startActivity(getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //Log.w(TAG, "Error deleting document", e);
                                                Toast.makeText(basket.this, "Oops! Looks like something went wrong.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

    //---Delete DialogBox---//
    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Remove Item")
                .setMessage("Do you want to remove this item from your basket?")
                .setIcon(R.drawable.ic_delete_forever_black_24dp)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Delete Listing
                        db.collection("listings").document(userData.userItemClicked_Global)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //Reload Activity
                                        finish();
                                        startActivity(getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        //---Declare Widgets---//
        emptyImageView = findViewById(R.id.emptyImageView);
        emptyTextView = findViewById(R.id.emptyTextView);
        BasketItemList = new ArrayList<>();
        basketRecyclerView = findViewById(R.id.basketRecyclerView);
        basketRecyclerView.setHasFixedSize(true);
        basketRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        symbolTextView = findViewById(R.id.symbolTextView);
        subtotalTextView = findViewById(R.id.subtotalTextView);
        currencyTextView = findViewById(R.id.currencyTextView);
        userData.currency_Global = "(GBP)"; //Default Currency

        //---Get Currency from Firestore---//
        String userID = user.getUid();
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
                            userData.currency_Global = FSCurrency;

                            //---Display Data---//
                            if (FSCurrency.equals("(GBP)")){
                                symbolTextView.setText("£");
                            }
                            else if (FSCurrency.equals("(EUR)")){
                                symbolTextView.setText("€");
                            }
                            else if (FSCurrency.equals("(USD)")){
                                symbolTextView.setText("$");
                            }
                            else if (FSCurrency.equals("(CAD)")){
                                symbolTextView.setText("$");
                            }
                            else if (FSCurrency.equals("(AUD)")){
                                symbolTextView.setText("$");
                            }
                            else if (FSCurrency.equals("(JPY)")){
                                symbolTextView.setText("¥");
                            }
                            else{
                                symbolTextView.setText("No Currency Selected.");
                            }

                            currencyTextView.setText(" " + FSCurrency);

                        } else {
                            //An error has occured, display error message and remove user from page.
                            userData.currency_Global = "(GBP)"; //Default Currency
                            Toast.makeText(basket.this, "Oops! Looks like something went wrong.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //Log.d(TAG, "get failed with ", task.getException());
                        Toast.makeText(basket.this, "Try Again.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        catch(Exception e){
            //Error Occured Display error message
            Toast.makeText(basket.this, "Oops! Looks like something went wrong.", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onStart(){
        super.onStart();

        //---Query to get items of current user---//
        String userID = user.getUid();
        collectionReference.whereEqualTo("UserID", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Double subtotal = 0.00;
                            //Check if the query is not empty
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //Get Document ID
                                    String docID= document.getId();

                                    BasketItem basketItem = document.toObject(BasketItem.class).withId(docID);
                                    BasketItemList.add(basketItem);

                                    //---Calculate Subtotal---//
                                    //Get Data
                                    String currency = document.getString("Currency").trim();
                                    Double price = new Double(document.getString("Price"));
                                    int quantity = Integer.parseInt(document.getString("Quantity"));
                                    //Total Price of each Item
                                    Double quantPrice = price * quantity;

                                    //--To GBP--//
                                    if (userData.currency_Global.equals("(GBP)")){
                                        if (currency.equals("(GBP)")){
                                            //No Currency Change Needed
                                            subtotal = subtotal + quantPrice;
                                        }
                                        else if(currency.equals("(USD)")){
                                            //USD -> GBP
                                            subtotal = (subtotal + quantPrice) * 0.78;

                                        }
                                        else if (currency.equals("(EUR)")){
                                            //EUR -> GBP
                                            subtotal = (subtotal + quantPrice) * 0.85;
                                        }
                                        else if (currency.equals("(CAD)")){
                                            //CAD -> GBP
                                            subtotal = (subtotal + quantPrice) * 0.58;
                                        }
                                        else if (currency.equals("(AUD)")){
                                            //AUD -> GBP
                                            subtotal = (subtotal + quantPrice) * 0.51;
                                        }
                                        else if (currency.equals("(JPY)")){
                                            //JPY -> GBP
                                            subtotal = (subtotal + quantPrice) * 0.0071;
                                        }
                                        else{
                                            //DataBase Error
                                            Toast.makeText(basket.this, "Oops! Somethings Wrong. Please review your basket.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    //--To USD--//
                                    if (userData.currency_Global.equals("(USD)")) {
                                        if (currency.equals("(USD)")) {
                                            //No Currency Change Needed
                                            subtotal = subtotal + quantPrice;
                                        } else if (currency.equals("(GBP)")) {
                                            //GBP -> USD
                                            subtotal = (subtotal + quantPrice) * 1.29;

                                        } else if (currency.equals("(EUR)")) {
                                            //EUR -> USD
                                            subtotal = (subtotal + quantPrice) * 1.09;
                                        } else if (currency.equals("(CAD)")) {
                                            //CAD -> USD
                                            subtotal = (subtotal + quantPrice) * 0.75;
                                        } else if (currency.equals("(AUD)")) {
                                            //AUD -> USD
                                            subtotal = (subtotal + quantPrice) * 0.66;
                                        } else if (currency.equals("(JPY)")) {
                                            //JPY -> USD
                                            subtotal = (subtotal + quantPrice) * 0.0091;
                                        } else {
                                            //DataBase Error
                                            Toast.makeText(basket.this, "Oops! Somethings Wrong. Please review your basket.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    //--To EUR--//
                                    if (userData.currency_Global.equals("(EUR)")){
                                        if (currency.equals("(EUR)")){
                                            //No Currency Change Needed
                                            subtotal = subtotal + quantPrice;
                                        }
                                        else if(currency.equals("(USD)")){
                                            //USD -> EUR
                                            subtotal = (subtotal + quantPrice) * 0.91;

                                        }
                                        else if (currency.equals("(GBP)")){
                                            //GBP -> EUR
                                            subtotal = (subtotal + quantPrice) * 1.18;
                                        }
                                        else if (currency.equals("(CAD)")){
                                            //CAD -> EUR
                                            subtotal = (subtotal + quantPrice) * 0.68;
                                        }
                                        else if (currency.equals("(AUD)")){
                                            //AUD -> EUR
                                            subtotal = (subtotal + quantPrice) * 0.60;
                                        }
                                        else if (currency.equals("(JPY)")){
                                            //JPY -> EUR
                                            subtotal = (subtotal + quantPrice) * 0.0083;
                                        }
                                        else{
                                            //DataBase Error
                                            Toast.makeText(basket.this, "Oops! Somethings Wrong. Please review your basket.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    //--To CAD--//
                                    if (userData.currency_Global.equals("(CAD)")){
                                        if (currency.equals("(CAD)")){
                                            //No Currency Change Needed
                                            subtotal = subtotal + quantPrice;
                                        }
                                        else if(currency.equals("(USD)")){
                                            //USD -> CAD
                                            subtotal = (subtotal + quantPrice) * 1.33;
                                        }
                                        else if (currency.equals("(EUR)")){
                                            //EUR -> CAD
                                            subtotal = (subtotal + quantPrice) * 1.46;
                                        }
                                        else if (currency.equals("(GBP)")){
                                            //GBP -> CAD
                                            subtotal = (subtotal + quantPrice) * 1.72;
                                        }
                                        else if (currency.equals("(AUD)")){
                                            //AUD -> CAD
                                            subtotal = (subtotal + quantPrice) * 0.88;
                                        }
                                        else if (currency.equals("(JPY)")){
                                            //JPY -> CAD
                                            subtotal = (subtotal + quantPrice) * 0.012;
                                        }
                                        else{
                                            //DataBase Error
                                            Toast.makeText(basket.this, "Oops! Somethings Wrong. Please review your basket.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    //--To AUD--//
                                    if (userData.currency_Global.equals("(AUD)")){
                                        if (currency.equals("(AUD)")){
                                            //No Currency Change Needed
                                            subtotal = subtotal + quantPrice;
                                        }
                                        else if(currency.equals("(USD)")){
                                            //USD -> AUD
                                            subtotal = (subtotal + quantPrice) * 1.52;
                                        }
                                        else if (currency.equals("(EUR)")){
                                            //EUR -> AUD
                                            subtotal = (subtotal + quantPrice) * 1.67;
                                        }
                                        else if (currency.equals("(GBP)")){
                                            //GBP -> AUD
                                            subtotal = (subtotal + quantPrice) * 1.96;
                                        }
                                        else if (currency.equals("(CAD)")){
                                            //CAD -> AUD
                                            subtotal = (subtotal + quantPrice) * 1.14;
                                        }
                                        else if (currency.equals("(JPY)")){
                                            //JPY -> AUD
                                            subtotal = (subtotal + quantPrice) * 0.014;
                                        }
                                        else{
                                            //DataBase Error
                                            Toast.makeText(basket.this, "Oops! Somethings Wrong. Please review your basket.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    //--To JPY--//
                                    if (userData.currency_Global.equals("(JPY)")){
                                        if (currency.equals("(JPY)")){
                                            //No Currency Change Needed
                                            subtotal = subtotal + quantPrice;
                                        }
                                        else if(currency.equals("(USD)")){
                                            //USD -> JPY
                                            subtotal = (subtotal + quantPrice) * 109.92;
                                        }
                                        else if (currency.equals("(EUR)")){
                                            //EUR -> JPY
                                            subtotal = (subtotal + quantPrice) * 120.48;
                                        }
                                        else if (currency.equals("(GBP)")){
                                            //GBP -> JPY
                                            subtotal = (subtotal + quantPrice) * 141.70;
                                        }
                                        else if (currency.equals("(CAD)")){
                                            //CAD -> JPY
                                            subtotal = (subtotal + quantPrice) * 82.34;
                                        }
                                        else if (currency.equals("(AUD)")){
                                            //AUD -> JPY
                                            subtotal = (subtotal + quantPrice) * 72.29;
                                        }
                                        else{
                                            //DataBase Error
                                            Toast.makeText(basket.this, "Oops! Somethings Wrong. Please review your basket.", Toast.LENGTH_SHORT).show();
                                        }
                                    }


                                    //Display Subtotal
                                    //With 2 decimal places
                                    String strSubtotal = String.format("%.2f", subtotal);
                                    subtotalTextView.setText(strSubtotal);
                                    //Save Subtotal for checkout
                                    userData.subtotal_Global = subtotal;


                                }

                                //Invoke Recycler View
                                basketRecyclerAdapter = new BasketRecyclerAdapter(getApplicationContext(), BasketItemList);
                                basketRecyclerView.setAdapter(basketRecyclerAdapter);
                                basketRecyclerAdapter.notifyDataSetChanged();


                            }
                            else {
                                //Display NoItem TextView and ImageView
                                emptyImageView.setVisibility(View.VISIBLE);
                                emptyTextView.setVisibility(View.VISIBLE);

                            }

                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }

                });






    }
}
