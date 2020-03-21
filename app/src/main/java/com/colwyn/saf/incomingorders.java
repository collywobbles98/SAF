package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.colwyn.saf.model.SalesItem;
import com.colwyn.saf.ui.SalesItemRecyclerAdapter;
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

public class incomingorders extends AppCompatActivity {

    //---Firebase & Firestore---//
    private StorageReference mStorageRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    //Firestore Collection Reference
    private CollectionReference collectionReference = db.collection("items_sold");

    //---Declare Widgets---//
    TextView noSalesTextView;
    RecyclerView salesRecyclerView;
    private List<SalesItem> SalesItemList;
    private SalesItemRecyclerAdapter salesItemrecyclerAdapter;

    //---Navigation---//
    public void backToProfile (View view) {
        startActivity(new Intent(incomingorders.this, Profile.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomingorders);

        //---Get Widgets---//
        SalesItemList = new ArrayList<>();

        salesRecyclerView = findViewById(R.id.salesRecyclerView);
        salesRecyclerView.setHasFixedSize(true);
        salesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        //---Query to get items of current user---//
        collectionReference.whereEqualTo("SellerID", user.getUid())
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

                                    //Add Items to list
                                    SalesItem salesItem = document.toObject(SalesItem.class).withId(docID);
                                    SalesItemList.add(salesItem);

                                }

                                //Invoke Recycler View
                                SalesItemRecyclerAdapter salesItemRecyclerAdapter = new SalesItemRecyclerAdapter(getApplicationContext(), SalesItemList);
                                salesRecyclerView.setAdapter(salesItemRecyclerAdapter);
                                salesItemRecyclerAdapter.notifyDataSetChanged();

                            }
                            else {
                                //Display NoItem TextView and ImageView
                                noSalesTextView = findViewById(R.id.noSalesTextView);
                                salesRecyclerView = findViewById(R.id.salesRecyclerView);
                                noSalesTextView.setVisibility(View.VISIBLE);
                                salesRecyclerView.setVisibility(View.GONE);

                            }

                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }

                });


    }
}
