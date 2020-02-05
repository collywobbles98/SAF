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

import com.colwyn.saf.model.UserItem;
import com.colwyn.saf.ui.UserItemRecyclerAdapter;
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

public class Selling extends AppCompatActivity {

    //---Firebase & Firestore---//
    private StorageReference mStorageRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    //Firestore Collection Reference
    private CollectionReference collectionReference = db.collection("listings");

    private List<UserItem> UserItemList;
    private RecyclerView recyclerView;
    private UserItemRecyclerAdapter userItemRecyclerAdapter;
    private TextView noItemsTextView;
    private ImageView noItemsImageView;
    private TextView itemCountTextView;
    String userIDFB = user.getUid();


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
        //Close Activity to force a refresh
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling);

        //---Reset Edit Item Variable to False (New listing not an edit)---//
        userData.listingEdit_Global = false;
        userData.userItemClicked_Global = null;

        noItemsTextView = findViewById(R.id.noItemsTextView);
        noItemsImageView = findViewById(R.id.noItemsImageView);
        UserItemList = new ArrayList<>();
        recyclerView = findViewById(R.id.itemRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart(){
        super.onStart();

        //---Reset Edit Item Variable to False (New listing not an edit)---//
        userData.listingEdit_Global = false;

        //---Query to get items of current user---//
        collectionReference.whereEqualTo("UserID", userIDFB)
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


                                    UserItem userItem = document.toObject(UserItem.class).withId(docID);
                                    UserItemList.add(userItem);



                                    //Display how many items the user has for sale
                                    itemCountTextView = findViewById(R.id.itemCountTextView);
                                    itemCountTextView.setText(task.getResult().size() + "");
                                }
                                //Invoke Recycler View
                                userItemRecyclerAdapter = new UserItemRecyclerAdapter(getApplicationContext(), UserItemList);
                                recyclerView.setAdapter(userItemRecyclerAdapter);
                                userItemRecyclerAdapter.notifyDataSetChanged();

                            }
                            else {
                                //Display NoItem TextView and ImageView
                                noItemsTextView.setVisibility(View.VISIBLE);
                                noItemsImageView.setVisibility(View.VISIBLE);

                            }

                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }

                });




    }
}
