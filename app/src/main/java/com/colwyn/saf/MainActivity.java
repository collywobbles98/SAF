package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.colwyn.saf.model.CatItem;
import com.colwyn.saf.ui.CatItemRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //---Firebase & Firestore---//
    private StorageReference mStorageRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //Firestore Collection Reference
    private CollectionReference collectionReference = db.collection("listings");

    String userId = user.getUid();

    //---Declare Widgets---//
    private List<CatItem> CatItemList;
    private RecyclerView recyclerView;
    private CatItemRecyclerAdapter catItemRecyclerAdapter;
    private TextView noItemsTextView;
    private ImageView noItemsImageView;
    private TextView itemCountTextView;
    String userIDFB = user.getUid();

    EditText searchEditText;
    ImageView searchButton, search2Button, cancelSearchButton;
    CardView searchCardView;
    RecyclerView catItemRecyclerView;





    //---Navigation---//
    public void sellingClicked(View view){

        startActivity(new Intent(MainActivity.this, Selling.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    public void basketClicked(View view){

        startActivity(new Intent(MainActivity.this, basket.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    public void goToProfile(View view){

        userData.userID_Global = user.getUid();

        startActivity(new Intent(MainActivity.this, Profile.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    public void messagesClicked(View view){

        startActivity(new Intent(MainActivity.this, chats.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }


    //---Category Buttons---//
    public void allClicked(View view){
        userData.categories_Global = "all";
        fillRecylerView();
    }
    public void accessoriesClicked(View view){
        userData.categories_Global = "Accessories";
        fillRecylerView();
    }
    public void badgesClicked(View view){
        userData.categories_Global = "Badges,Decals";
        fillRecylerView();
    }
    public void bodyClicked(View view){
        userData.categories_Global = "Body";
        fillRecylerView();
    }
    public void brakesClicked(View view){
        userData.categories_Global = "Brakes";
        fillRecylerView();
    }
    public void clutchClicked(View view){
        userData.categories_Global = "Clutch";
        fillRecylerView();
    }
    public void coolingClicked(View view){
        userData.categories_Global = "Cooling,Heating";
        fillRecylerView();
    }
    public void drivetrainClicked(View view){
        userData.categories_Global = "Drivetrain";
        fillRecylerView();
    }
    public void electricsClicked(View view){
        userData.categories_Global = "Electrics";
        fillRecylerView();
    }
    public void engineClicked(View view){
        userData.categories_Global = "Engine";
        fillRecylerView();
    }
    public void exhaustClicked(View view){
        userData.categories_Global = "Exhaust";
        fillRecylerView();
    }
    public void exteriorClicked(View view){
        userData.categories_Global = "Exterior";
        fillRecylerView();
    }
    public void filtersClicked(View view){
        userData.categories_Global = "Filters";
        fillRecylerView();
    }
    public void fuelClicked(View view){
        userData.categories_Global = "Fuel";
        fillRecylerView();
    }
    public void gagesClicked(View view){
        userData.categories_Global = "Gages";
        fillRecylerView();
    }
    public void gearboxClicked(View view){
        userData.categories_Global = "Gearbox";
        fillRecylerView();
    }
    public void interiorClicked(View view){
        userData.categories_Global = "Interior";
        fillRecylerView();
    }
    public void steeringClicked(View view){
        userData.categories_Global = "Steering";
        fillRecylerView();
    }
    public void suspensionClicked(View view){
        userData.categories_Global = "Suspension";
        fillRecylerView();
    }
    public void wheelsClicked(View view){
        userData.categories_Global = "Wheels";
        fillRecylerView();
    }
    public void otherClicked(View view){
        userData.categories_Global = "Other";
        fillRecylerView();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userData.userID_Global = user.getUid();

        //Set category to all listings
        userData.categories_Global = "all";

        //If no user is logged in, take user to login activity.
        if (userData.userID_Global == null){
            startActivity(new Intent(MainActivity.this, LogIn.class));
        }
        //Recycler View
        CatItemList = new ArrayList<>();
        recyclerView = findViewById(R.id.catItemRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        //---Get Widgets---//
        searchButton = findViewById(R.id.searchButton);
        search2Button = findViewById(R.id.search2Button);
        searchCardView = findViewById(R.id.searchCardView);
        cancelSearchButton = findViewById(R.id.cancelSearchButton);
        searchEditText = findViewById(R.id.searchEditText);

        //---Search Button Clicked---//
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCardView.setVisibility(View.VISIBLE);
                searchButton.setVisibility(View.GONE);
            }
        });

        //---Cancel Button Clicked---//
        cancelSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCardView.setVisibility(View.GONE);
                searchButton.setVisibility(View.VISIBLE);
            }
        });

        //---Search Capabilities---//
        search2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(searchEditText.getText().toString())){
                    //Dont do anything
                }
                else {
                    userData.categories_Global = "Search";
                    fillRecylerView();
                }
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)){
                    //If search edit text is empty then just show all listings

                    userData.categories_Global = "all";
                    fillRecylerView();

                }
                else{

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        //Set category to all listings
        userData.categories_Global = "all";

        fillRecylerView();

    }

    private void fillRecylerView() {

        //---Get Widgets---//
        noItemsTextView = findViewById(R.id.noItemsTextView);
        catItemRecyclerView = findViewById(R.id.catItemRecyclerView);

        //Clear Recycler View
        CatItemList.clear();

        if (userData.categories_Global.equals("all")){
            //---Query to get all items---//
            collectionReference.orderBy("ServerTimeStamp", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                //Check if the query is not empty
                                if (!task.getResult().isEmpty()) {
                                    //Hide NoItem TextView
                                    noItemsTextView.setVisibility(View.GONE);
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        //Get Document ID
                                        String docID= document.getId();


                                        CatItem catItem = document.toObject(CatItem.class).withId(docID);
                                        CatItemList.add(catItem);

                                    }
                                    //Invoke Recycler View
                                    //Hide Recycler View
                                    catItemRecyclerView.setVisibility(View.VISIBLE);
                                    catItemRecyclerAdapter = new CatItemRecyclerAdapter(getApplicationContext(), CatItemList);
                                    recyclerView.setAdapter(catItemRecyclerAdapter);
                                    catItemRecyclerAdapter.notifyDataSetChanged();

                                }
                                else {
                                    //Display NoItem TextView
                                    noItemsTextView.setVisibility(View.VISIBLE);
                                    //Hide Recycler View
                                    catItemRecyclerView.setVisibility(View.INVISIBLE);

                                }

                            } else {
                                //Log.d(TAG, "Error getting documents: ", task.getException());
                            }

                        }

                    });
        }

        else if (userData.categories_Global.equals("Search")){
            //---Query to get items in categories---//
            collectionReference.whereEqualTo("Title", searchEditText.getText().toString().trim())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                //Hide NoItem TextView
                                noItemsTextView.setVisibility(View.GONE);
                                //Check if the query is not empty
                                if (!task.getResult().isEmpty()) {
                                    Toast.makeText(MainActivity.this, "Search Successful.", Toast.LENGTH_SHORT).show();
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        //Get Document ID
                                        String docID= document.getId();


                                        CatItem catItem = document.toObject(CatItem.class).withId(docID);
                                        CatItemList.add(catItem);

                                    }
                                    //Invoke Recycler View
                                    //Hide Recycler View
                                    catItemRecyclerView.setVisibility(View.VISIBLE);
                                    catItemRecyclerAdapter = new CatItemRecyclerAdapter(getApplicationContext(), CatItemList);
                                    recyclerView.setAdapter(catItemRecyclerAdapter);
                                    catItemRecyclerAdapter.notifyDataSetChanged();

                                }
                                else {
                                    Toast.makeText(MainActivity.this, "Search unuccessful.", Toast.LENGTH_SHORT).show();
                                    //Display NoItem TextView
                                    noItemsTextView.setVisibility(View.VISIBLE);
                                    //Hide Recycler View
                                    catItemRecyclerView.setVisibility(View.INVISIBLE);

                                }

                            } else {
                                //Log.d(TAG, "Error getting documents: ", task.getException());
                                Toast.makeText(MainActivity.this, "Search Error.", Toast.LENGTH_SHORT).show();
                            }

                        }

                    });

        }
        else{

            //---Query to get items in categories---//
            collectionReference.whereEqualTo("Category", userData.categories_Global)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                //Hide NoItem TextView
                                noItemsTextView.setVisibility(View.GONE);
                                //Check if the query is not empty
                                if (!task.getResult().isEmpty()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        //Get Document ID
                                        String docID= document.getId();


                                        CatItem catItem = document.toObject(CatItem.class).withId(docID);
                                        CatItemList.add(catItem);

                                    }
                                    //Invoke Recycler View
                                    //Hide Recycler View
                                    catItemRecyclerView.setVisibility(View.VISIBLE);
                                    catItemRecyclerAdapter = new CatItemRecyclerAdapter(getApplicationContext(), CatItemList);
                                    recyclerView.setAdapter(catItemRecyclerAdapter);
                                    catItemRecyclerAdapter.notifyDataSetChanged();

                                }
                                else {
                                    //Display NoItem TextView
                                    noItemsTextView.setVisibility(View.VISIBLE);
                                    //Hide Recycler View
                                    catItemRecyclerView.setVisibility(View.INVISIBLE);

                                }

                            } else {
                                //Log.d(TAG, "Error getting documents: ", task.getException());
                            }

                        }

                    });

        }


    }
}
