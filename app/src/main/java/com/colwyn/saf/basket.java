package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.colwyn.saf.model.BasketItem;
import com.colwyn.saf.ui.BasketRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class basket extends AppCompatActivity {

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

    }

    @Override
    protected void onStart(){
        super.onStart();

        //---Query to get items of current user---//

        String userID = user.getUid();;

        collectionReference.whereEqualTo("UserID", userID)
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

                                    BasketItem basketItem = document.toObject(BasketItem.class).withId(docID);
                                    BasketItemList.add(basketItem);

                                    //Display how many items the user has for sale
                                    //itemCountTextView = findViewById(R.id.itemCountTextView);
                                    //itemCountTextView.setText(task.getResult().size() + "");
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