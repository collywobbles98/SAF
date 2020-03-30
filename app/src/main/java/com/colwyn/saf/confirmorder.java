package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.colwyn.saf.model.encryption;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class confirmorder extends AppCompatActivity {

    //---Firebase & Firestore---//
    private StorageReference mStorageRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //---Declare Widgets---//
    TextView orderTotalTextView, deliveryNameTextView, deliveryAddressTextView, cardTextView, listingToUpdateTextView, paymentMethodTextView;
    Button buyNowButton;

    //---Buttons Clicked---//
    //Edit Basket
    public void editBasketClicked(View view){
        userData.basketEdit_Global = Boolean.TRUE;
        startActivity(new Intent(confirmorder.this, basket.class));
    }
    //Edit Delivery Address
    public void changeAddressClicked(View view){
        userData.addressEdit_Global = Boolean.TRUE;
        startActivity(new Intent(confirmorder.this, accountDetails.class));
    }
    //Edit Card
    public void changeCardClicked(View view){
        userData.paymentEdit_Global = Boolean.TRUE;
        startActivity(new Intent(confirmorder.this, paymentdetails.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmorder);

        //---Get Widgets---//
        orderTotalTextView = findViewById(R.id.orderTotalTextView);
        deliveryNameTextView = findViewById(R.id.deliveryNameTextView);
        deliveryAddressTextView = findViewById(R.id.deliveryAddressTextview);
        paymentMethodTextView = findViewById(R.id.paymentMethodTextView);
        cardTextView = findViewById(R.id.cardTextView);
        buyNowButton = findViewById(R.id.buyNowButton);
        listingToUpdateTextView = findViewById(R.id.listingToUpdateTextView);

        //---Get Subtotal---//
        //Display Subtotal
        //With 2 decimal places
        String strSubtotal = String.format("%.2f", userData.subtotal_Global);

        //---Display data---//
        if (userData.currency_Global.equals("(GBP)")){
            orderTotalTextView.setText("£" + strSubtotal + " " + userData.currency_Global);
        }
        else if (userData.currency_Global.equals("(EUR)")){
            orderTotalTextView.setText("€" + strSubtotal + " " + userData.currency_Global);
        }
        else if (userData.currency_Global.equals("(JPY)")){
            orderTotalTextView.setText("¥" + strSubtotal + " " + userData.currency_Global);
        }
        else{
            orderTotalTextView.setText("$" + strSubtotal + " " + userData.currency_Global);
        }
        //PayPal Reference
        paymentMethodTextView.setText(userData.paymentMethod_Global);
        cardTextView.setText(userData.paypal_reference_Global);

        //---Get User Information from firestore---//
        String userID = user.getUid();
        try {
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

                            //---Display in edit texts ---//
                            deliveryNameTextView.setText(FSFirstname + " " + FSLastname);
                            deliveryAddressTextView.setText(FSAddress + " " + FSPostcodeZip + " " + FSCountyState + " " + FSCountry);


                        } else {
                            //An error has occured
                            Toast.makeText(confirmorder.this, "Oops! Looks like something went wrong.", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                    }
                }
            });
        }

        catch(Exception e) {
            //Error
            Toast.makeText(confirmorder.this, "Please Try Again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(confirmorder.this, basket.class));
        }

        //---Decrypt only last 4 card numbers and display them---//
        String encrypted = userData.encryptednum4_Global;
        String decrypted = "";
            try {
                    decrypted = encryption.decrypt(encrypted);
                    cardTextView.setText(userData.cardtype_Global + " **** **** **** " + decrypted);
                    //Log.d("TEST", "decrypted:" + decrypted);
                    //Toast.makeText(confirmorder.this, "" + decrypted, Toast.LENGTH_SHORT).show();
            }

            catch (Exception e) {

            }

        buyNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //---Place Order---//
                //Get Item details from users basket
                String userID = user.getUid();
                db.collection("basket_items")
                        .whereEqualTo("UserID", userID)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            String goods = "";
                            Integer itemnum = 0;

                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        userData.listingToUpdate_Global = null;
                                        String FStitle = document.getString("Title");
                                        String FSprice = document.getString("Price");
                                        String FScurrency = document.getString("Currency");
                                        final String FSquantity = document.getString("Quantity");
                                        String FSdelivery = document.getString("Delivery_Notes");
                                        final String FSitemID = document.getString("Item");
                                        String FSid = document.getId();

                                        //Get Current Stock of item
                                        DocumentReference docRef = db.collection("listings").document(FSitemID);
                                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        //---Retrieve data and store as string---//
                                                        String FSCurrentStock = document.getString("Stock");
                                                        //Toast.makeText(confirmorder.this, FSCurrentStock, Toast.LENGTH_SHORT).show();
                                                        if (FSCurrentStock.equals("unlimited")){
                                                            //No need for stock reduction
                                                        }
                                                        else{
                                                            //Reduce Stock
                                                            Integer currentStockInt = Integer.parseInt(FSCurrentStock);
                                                            Integer quantitySoldInt = Integer.parseInt(FSquantity);
                                                            Integer newStockInt = currentStockInt - quantitySoldInt;
                                                            String newStock = newStockInt.toString().trim();

                                                            Toast.makeText(confirmorder.this, document.getId() + " Stock: " + FSCurrentStock + " Minus: " + FSquantity + " Equals: " + newStock, Toast.LENGTH_SHORT).show();

                                                            //Update Stock
                                                            DocumentReference stockRef = db.collection("listings").document(FSitemID);
                                                            stockRef
                                                                    .update("Stock", newStock)
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
                                                        }

                                                    } else {
                                                        //An error has occured, display error message and remove user from page.
                                                        Toast.makeText(confirmorder.this, "Oops! Looks like something went wrong.", Toast.LENGTH_SHORT).show();
                                                        //startActivity(new Intent(accountDetails.this, Profile.class));
                                                        //Log.d(TAG, "No such document");
                                                    }
                                                } else {
                                                    //Log.d(TAG, "get failed with ", task.getException());
                                                }
                                            }
                                        });
                                        //Increase Iteration for item number
                                        itemnum ++;
                                        //Save this to string
                                        goods += "(" + itemnum + ") " + "Item ID: " + FSitemID + " Title: " + FStitle + " Price: " + FSprice + " Currency: " + FScurrency + " Quantity: " + FSquantity + " Delivery Notes: " + FSdelivery + "\n\n";
                                        userData.goods_Global = goods;

                                    }

                                    placeOrder();

                                } else {
                                    //Error getting documents
                                    return;
                                }
                            }
                        });



            }
        });


        //---PayPal---//
        //Get Intent
        Intent intent = getIntent();

        try{
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            showPayPalDetails(jsonObject.getJSONObject("response"),intent.getStringExtra("PaymentAmount"));

        }
        catch(Exception e){

        }


    }

    private void showPayPalDetails(JSONObject response, String paymentAmount){
        try{
            //Save Payment Details
            userData.paypal_reference_Global = response.getString("id");
            cardTextView.setText(userData.paypal_reference_Global);
            //cardTextView.setText("PayPal REF: " + response.getString("id") + "\nStatus: " + response.getString("state"));

        }
        catch (Exception e){

        }

    }

    private void placeOrder(){
        //Timestamp
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String format = simpleDateFormat.format(new Date());

        //Save Order Data to firestore
        Map<String, Object> orders = new HashMap<>();
        orders.put("UserID", user.getUid());
        orders.put("PaymentMethod", userData.paymentMethod_Global);
        orders.put("OrderTotal", userData.subtotal_Global.toString().trim());
        orders.put("Goods", userData.goods_Global);
        orders.put("CurrencyUsed", userData.currency_Global);
        //orders.put("CardNum1", userData.encryptednum1_Global);
        //orders.put("CardNum2", userData.encryptednum2_Global);
        //orders.put("CardNum3", userData.encryptednum3_Global);
        //orders.put("CardNum4", userData.encryptednum4_Global);
        //orders.put("CardholderName", userData.cardholdername_Global);
        //orders.put("ExpiryMonth", userData.encryptedmonth_Global);
        //orders.put("ExpiryYear", userData.encryptedyear_Global);
        //orders.put("CCV", userData.encryptedccv_Global);
        orders.put("PayPalREF", userData.paypal_reference_Global);
        orders.put("DeliveryName", deliveryNameTextView.getText().toString().trim());
        orders.put("DeliveryAddress", deliveryAddressTextView.getText().toString().trim());
        orders.put("TimeStamp", format);

        // Add a new document with a generated ID
        db.collection("orders")
                .add(orders)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        //Go back to Main Activity
                        //Toast.makeText(CatListingView.this, "Added To Basket!", Toast.LENGTH_SHORT).show();

                        //Order Reference
                        final String orderID = documentReference.getId();
                        userData.orderRef_Global = documentReference.getId();



                        //Notify Each Individual Seller about the purchase
                        db.collection("basket_items")
                                .whereEqualTo("UserID", user.getUid())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                                String FSquantity = document.getString("Quantity");
                                                String FSitemID = document.getString("Item");
                                                String FStitle = document.getString("Title");
                                                String FSSellerID = document.getString("SellerID");

                                                //Add data to firestore database
                                                Map<String, Object> items_sold = new HashMap<>();
                                                items_sold.put("BuyerID", user.getUid());
                                                items_sold.put("SellerID", FSSellerID);
                                                items_sold.put("ItemID", FSitemID);
                                                items_sold.put("ItemTitle", FStitle);
                                                items_sold.put("Quantity", FSquantity);
                                                items_sold.put("BuyerName", deliveryNameTextView.getText().toString().trim());
                                                items_sold.put("BuyerAddress", deliveryAddressTextView.getText().toString().trim());
                                                items_sold.put("Status", "Processing");
                                                items_sold.put("OrderID", orderID);

                                                db.collection("items_sold")
                                                        .add(items_sold)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());

                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                //Log.w(TAG, "Error adding document", e);
                                                            }
                                                        });

                                                //Clear Basket
                                                db.collection("basket_items").document(document.getId())
                                                        .delete()
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                //Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                //Log.w(TAG, "Error deleting document", e);
                                                            }
                                                        });



                                            }
                                        } else {
                                            //Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });

                        //Confirm order activity
                        startActivity(new Intent(confirmorder.this, ordercomplete.class));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error adding document", e);
                        Toast.makeText(confirmorder.this, "Your order could not be placed at this time.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(confirmorder.this, basket.class));
                    }
                });

    }
}
