package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.colwyn.saf.model.UserItem;
import com.colwyn.saf.ui.UserItemRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Selling extends AppCompatActivity {

    //---Firebase & Firestore---//
    private StorageReference mStorageRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //Firestore Collection Reference
    private CollectionReference collectionReference = db.collection("listings");

    private List<UserItem> UserItemList;
    private RecyclerView recyclerView;
    private UserItemRecyclerAdapter userItemRecyclerAdapter;
    private TextView noItemsTextView;


    //---Navigation---//
    //profile activity
    public void profileClicked(View view){
        startActivity(new Intent(Selling.this, Profile.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }
    //Home Activity
    public void homeClicked (View view) {
        startActivity(new Intent(Selling.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }
    //Sell Item Activity
    public void sellClicked (View view) {
        startActivity(new Intent(Selling.this, addItem.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling);

        noItemsTextView = findViewById(R.id.noItemsTextView);
        UserItemList = new ArrayList<>();
        recyclerView = findViewById(R.id.itemRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart(){
        super.onStart();

        //---Query to get items of current user---//
        collectionReference.whereEqualTo("UserID", userData.userID_Global)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //Check if the query is not empty
                            if (!task.getResult().isEmpty()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    UserItem userItem = document.toObject(UserItem.class);
                                    UserItemList.add(userItem);
                                }

                                //Invoke Recycler View
                                userItemRecyclerAdapter = new UserItemRecyclerAdapter(Selling.this, UserItemList);
                                recyclerView.setAdapter(userItemRecyclerAdapter);
                                userItemRecyclerAdapter.notifyDataSetChanged();

                            }
                            else {
                                //Display NoItemTextView
                                noItemsTextView.setVisibility(View.VISIBLE);

                            }

                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
