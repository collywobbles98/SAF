package com.colwyn.saf;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.colwyn.saf.model.ReviewItem;
import com.colwyn.saf.ui.ReviewItemRecyclerAdapter;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CatListingView extends AppCompatActivity {

    //---Firestore---//
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //Firestore Collection Reference
    private CollectionReference collectionReference = db.collection("reviews");

    //---Declare Widgets---//
    TextView titleTextView, priceTextView, deliveryTextView, conditionTextView, brandTextView, descriptionTextView, nameTextView, locationTextView, numberTextView, emailTextView, postedTextView, symbolTextView, currencyTextView, stockTextView, reviewNumtextView;
    ImageView catItemImageView, newImageView;
    Button addToBasketButton, increaseButton, decreaseButton;
    EditText quantityEditText;
    CardView messageSellerCardView, buyCardView;

    private List<ReviewItem> ReviewItemList;
    private RecyclerView reviewRecyclerView;
    private ReviewItemRecyclerAdapter reviewItemRecyclerAdapter;


    //---Nav---//
    public void backClicked (View view){
        //Take user back to catalogue and remove clicked Item Variable
        userData.catItemClicked_Global = null;
        userData.itemSeller_Global = null;
        userData.itemStock_Global = null;
        startActivity(new Intent(CatListingView.this, MainActivity.class));
    }

    public void addReviewClicked (View view){
        startActivity(new Intent(CatListingView.this, addReview.class));
    }

//---Add Items to basket---//
    public void addtoBasketClicked (View view){

        //Declare Widgets
        titleTextView = findViewById(R.id.titleTextView);
        priceTextView = findViewById(R.id.priceTextView);
        deliveryTextView = findViewById(R.id.deliveryTextView);
        symbolTextView = findViewById(R.id.symbolTextView);
        currencyTextView = findViewById(R.id.currencyTextView);
        quantityEditText = findViewById(R.id.quantityEditText);

        //Add item to users basket

            Map<String, Object> basket_items = new HashMap<>();
            basket_items.put("UserID", user.getUid());
            basket_items.put("Item", userData.catItemClicked_Global);
            basket_items.put("ImageURL", userData.imageURLBasket_Global);
            basket_items.put("Title", titleTextView.getText().toString().trim());
            basket_items.put("Price", priceTextView.getText().toString().trim());
            basket_items.put("Symbol", symbolTextView.getText().toString().trim());
            basket_items.put("Currency", currencyTextView.getText().toString().trim());
            basket_items.put("Quantity", quantityEditText.getText().toString().trim());
            basket_items.put("Delivery_Notes", deliveryTextView.getText().toString().trim());
            basket_items.put("SellerID", userData.itemSeller_Global);


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
        symbolTextView = findViewById(R.id.symbolTextView);
        currencyTextView = findViewById(R.id.currencyTextView);
        deliveryTextView = findViewById(R.id.deliveryTextView);
        conditionTextView = findViewById(R.id.conditionTextView);
        brandTextView = findViewById(R.id.brandTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        nameTextView = findViewById(R.id.disCardNameTextView);
        locationTextView = findViewById(R.id.locationTextView);
        numberTextView = findViewById(R.id.numberTextView);
        emailTextView = findViewById(R.id.emailTextView);
        postedTextView = findViewById(R.id.postedTextView);
        stockTextView = findViewById(R.id.stockTextView);

        newImageView = findViewById(R.id.newImageView);
        catItemImageView = findViewById(R.id.catItemImageView);

        buyCardView = findViewById(R.id.buyCardView);
        addToBasketButton = findViewById(R.id.addToBasketButton);

        quantityEditText = findViewById(R.id.quantityEditText);
        increaseButton = findViewById(R.id.increaseButton);
        decreaseButton = findViewById(R.id.decreaseButton);

        messageSellerCardView = findViewById(R.id.messageSellerCardView);
        //Reviews
        ReviewItemList = new ArrayList<>();
        reviewNumtextView = findViewById(R.id.reviewNumTextView);
        reviewRecyclerView = findViewById(R.id.reviewRecyclerView);
        reviewRecyclerView.setHasFixedSize(true);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                            String FSCurrency = document.getString("Currency");
                            String FSSymbol = document.getString("Symbol");
                            String FSTimeStamp = document.getString("TimeStamp");
                            String FSStock = document.getString("Stock");
                            userData.imageURLBasket_Global = document.getString("ImageURL");
                            userData.itemSeller_Global = document.getString("UserID");

                            //Display in edit texts
                            titleTextView.setText(FSTitle);
                            priceTextView.setText(FSPrice);
                            symbolTextView.setText(FSSymbol);
                            currencyTextView.setText(" (" + FSCurrency + ")");
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
                                userData.itemStock_Global = "unlimited";
                            }
                            else {
                                int stockNum = Integer.parseInt(FSStock);
                                if (stockNum >= 10) {
                                    stockTextView.setText(stockNum + " Available");
                                    stockTextView.setTextColor(Color.parseColor("#53d769"));
                                    userData.itemStock_Global = FSStock;
                                } else if (stockNum < 10 && stockNum > 0) {
                                    stockTextView.setText("Only " + stockNum + " Remaining");
                                    stockTextView.setTextColor(Color.parseColor("#fd9426"));
                                    userData.itemStock_Global = FSStock;
                                } else {
                                    stockTextView.setText("Sold Out");
                                    stockTextView.setTextColor(Color.parseColor("#fc3d39"));

                                    //Can't Buy Item, Remove Add to basket Button
                                    buyCardView.setVisibility(View.GONE);
                                    addToBasketButton.setVisibility(View.GONE);
                                    userData.itemStock_Global = null;

                                }
                            }

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


        //---Stock Increase Button---//
        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userData.itemStock_Global.equals("unlimited")){
                    //Increase as much as the user wants
                    String strcurrentQuantity = quantityEditText.getText().toString().trim();
                    int currentQuantity = Integer.parseInt(strcurrentQuantity);
                    int newQuantity = currentQuantity + 1;
                    quantityEditText.setText(newQuantity + "");
                }
                else{
                    //Not unlimited
                    //Get Current Value and add 1 (if stock allows for it)
                    int quantityLimit = Integer.parseInt(userData.itemStock_Global);
                    String strcurrentQuantity = quantityEditText.getText().toString().trim();
                    int currentQuantity = Integer.parseInt(strcurrentQuantity);

                    if (currentQuantity < quantityLimit){
                        int newQuantity = currentQuantity + 1;
                        quantityEditText.setText(newQuantity + "");
                    }
                }

            }
        });

        //---Stock Decrease Button---//
        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get Current Quantity and remove 1 only if quantity is > 1
                String strcurrentQuantity = quantityEditText.getText().toString().trim();
                int currentQuantity = Integer.parseInt(strcurrentQuantity);

                if (currentQuantity > 1){
                    int newQuantity = currentQuantity - 1;
                    quantityEditText.setText(newQuantity + "");
                }
                else{
                    // Cant add 0 items to basket
                }
            }
        });


        //---Message Seller Button---//

        messageSellerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the Viewer is the seller remove the option to message
                if (userData.itemSeller_Global.equals(user.getUid())) {
                    Toast.makeText(CatListingView.this, "This is your item.", Toast.LENGTH_SHORT).show();
                } else {
                    //Not the seller so dont hide
                    //Create the messages
                    //Save user 1 (Current user) save user 2 (Seller) and a starter message ("Send a message")
                    //Make Document ID = user1id + user2id this can be checked for duplicates with two if statements

                    //Check this messages has not been created before

                    DocumentReference docRef = db.collection("chats").document(user.getUid() + userData.itemSeller_Global);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    //Take User To Messages
                                    userData.chatClicked_Global = user.getUid()+userData.itemSeller_Global;
                                    userData.chatOtherUser_Global = nameTextView.getText().toString().trim();
                                    startActivity(new Intent(CatListingView.this, messages.class));
                                } else {
                                    //Check for the reverse

                                    DocumentReference docRef = db.collection("chats").document(userData.itemSeller_Global + user.getUid());
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    //Take User To Messages
                                                    userData.chatClicked_Global = userData.itemSeller_Global + user.getUid();
                                                    userData.chatOtherUser_Global = nameTextView.getText().toString().trim();
                                                    startActivity(new Intent(CatListingView.this, messages.class));
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

        //Dont let user type more than available stock
        quantityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (userData.itemStock_Global.equals("unlimited")){

                }
                else{
                    //Not unlimited
                    //Check Current Value is the same or less than the stock
                    try {
                        int quantityLimit = Integer.parseInt(userData.itemStock_Global);
                        String strcurrentQuantity = quantityEditText.getText().toString().trim();
                        int currentQuantity = Integer.parseInt(strcurrentQuantity);

                        if (currentQuantity > quantityLimit) {
                            quantityEditText.setText(userData.itemStock_Global);
                        }
                    }
                    catch (Exception e){

                    }
                }

            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();

        //---Reset Edit Item Variable to False (New listing not an edit)---//
        userData.listingEdit_Global = false;

        //---Query to get Reviews---//
        collectionReference.whereEqualTo("Item", userData.catItemClicked_Global).orderBy("ServerTimeStamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //Check if the query is not empty
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    //Get Document ID
                                    String docID= document.getId();

                                    ReviewItem reviewItem = document.toObject(ReviewItem.class).withId(docID);
                                    ReviewItemList.add(reviewItem);

                                    //Display how many items the user has for sale
                                    reviewNumtextView.setText("Reviews (" + task.getResult().size() + ")");
                                }

                                //Invoke Recycler View
                                reviewItemRecyclerAdapter = new ReviewItemRecyclerAdapter(getApplicationContext(), ReviewItemList);
                                reviewRecyclerView.setAdapter(reviewItemRecyclerAdapter);
                                reviewItemRecyclerAdapter.notifyDataSetChanged();

                            }
                            else {
                                //No Reviews

                            }

                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
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
                        Toast.makeText(CatListingView.this, "Oops! Looks like something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }

    private void getUser2Details(){

        //get User 2 Name
        DocumentReference docRef3 = db.collection("user").document(userData.itemSeller_Global);
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
                        Toast.makeText(CatListingView.this, "Oops! Looks like something went wrong.", Toast.LENGTH_SHORT).show();
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

        chats.put("User2", userData.itemSeller_Global);
        chats.put("User2FirstName", userData.FSUser2FN);
        chats.put("User2LastName", userData.FSUser2LN);
        chats.put("User2Initials", userData.FSUser2Initials);
        SimpleDateFormat formatter= new SimpleDateFormat("HH:mm:ss 'at' yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        chats.put("TimeStamp", date.toString().trim());

        chats.put("LastMessage", "Send a Message");

        db.collection("chats").document(user.getUid()+userData.itemSeller_Global)
                .set(chats)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully written!");
                        //Go to Chat
                        userData.chatClicked_Global = user.getUid()+userData.itemSeller_Global;
                        userData.chatOtherUser_Global = nameTextView.getText().toString().trim();
                        startActivity(new Intent(CatListingView.this, messages.class));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error writing document", e);
                        Toast.makeText(CatListingView.this, "Oops! Looks like something went wrong getting seller details.", Toast.LENGTH_SHORT).show();
                    }
                });
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
