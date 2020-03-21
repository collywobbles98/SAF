package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.colwyn.saf.model.SoldItem;
import com.colwyn.saf.ui.SoldItemRecyclerAdapter;
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

public class orderbreakdown extends AppCompatActivity {

    //---Firebase & Firestore---//
    private StorageReference mStorageRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    //Firestore Collection Reference
    private CollectionReference collectionReference = db.collection("items_sold");

    //---Declare Widgets---//
    private List<SoldItem> SoldItemList;
    private SoldItemRecyclerAdapter soldItemrecyclerAdapter;
    RecyclerView itemsSoldRecyclerView;
    TextView orderReferenceTextView;

    //--Back Button---//
    public void backToOrders (View view){
        startActivity(new Intent(orderbreakdown.this, myorders.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderbreakdown);

        SoldItemList = new ArrayList<>();

        itemsSoldRecyclerView = findViewById(R.id.itemsSoldRecyclerView);
        itemsSoldRecyclerView.setHasFixedSize(true);
        itemsSoldRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderReferenceTextView = findViewById(R.id.orderReferenceTextView);
        orderReferenceTextView.setText(userData.orderClicked_Global);

    }

    @Override
    protected void onStart() {
        super.onStart();

        //---Query to get items of current user---//
        collectionReference.whereEqualTo("OrderID", userData.orderClicked_Global)
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
                                    SoldItem soldItem = document.toObject(SoldItem.class).withId(docID);
                                    SoldItemList.add(soldItem);

                                }

                                //Invoke Recycler View
                                SoldItemRecyclerAdapter soldItemRecyclerAdapter = new SoldItemRecyclerAdapter(getApplicationContext(), SoldItemList);
                                itemsSoldRecyclerView.setAdapter(soldItemRecyclerAdapter);
                                soldItemRecyclerAdapter.notifyDataSetChanged();

                            }
                            else {
                                Toast.makeText(orderbreakdown.this, "Item Information Unavailable.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(orderbreakdown.this, myorders.class));

                            }

                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }

                });


    }
}
