package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.colwyn.saf.model.OrderItem;
import com.colwyn.saf.ui.OrderItemRecyclerAdapter;
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

public class myorders extends AppCompatActivity {

    //---Firebase & Firestore---//
    private StorageReference mStorageRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    //Firestore Collection Reference
    private CollectionReference collectionReference = db.collection("orders");

    //---Declare Widgets---//
    private List<OrderItem> OrderItemList;
    private OrderItemRecyclerAdapter orderItemrecyclerAdapter;
    TextView noOrdersTextView;
    RecyclerView ordersRecyclerView;

    //--Back Button---//
    public void backToProfile (View view){
        startActivity(new Intent(myorders.this, Profile.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorders);

        noOrdersTextView = findViewById(R.id.noItemsTextView);
        OrderItemList = new ArrayList<>();

        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        ordersRecyclerView.setHasFixedSize(true);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        //---Query to get items of current user---//
        collectionReference.whereEqualTo("UserID", user.getUid())
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
                                    OrderItem orderItem = document.toObject(OrderItem.class).withId(docID);
                                    OrderItemList.add(orderItem);

                                }

                                //Invoke Recycler View
                                OrderItemRecyclerAdapter orderItemRecyclerAdapter = new OrderItemRecyclerAdapter(getApplicationContext(), OrderItemList);
                                ordersRecyclerView.setAdapter(orderItemRecyclerAdapter);
                                orderItemRecyclerAdapter.notifyDataSetChanged();

                            }
                            else {
                                //Display NoItem TextView and ImageView
                                noOrdersTextView = findViewById(R.id.noOrdersTextView);
                                ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
                                noOrdersTextView.setVisibility(View.VISIBLE);
                                ordersRecyclerView.setVisibility(View.GONE);

                            }

                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }

                });


    }

}
