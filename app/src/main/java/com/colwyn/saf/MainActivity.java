package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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


    private List<CatItem> CatItemList;
    private RecyclerView recyclerView;
    private CatItemRecyclerAdapter catItemRecyclerAdapter;
    private TextView noItemsTextView;
    private ImageView noItemsImageView;
    private TextView itemCountTextView;
    String userIDFB = user.getUid();



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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userData.userID_Global = user.getUid();

        //If no user is logged in, take user to login activity.
        if (userData.userID_Global == null){
            startActivity(new Intent(MainActivity.this, LogIn.class));
        }

        CatItemList = new ArrayList<>();

        //Recycler View
        recyclerView = findViewById(R.id.catItemRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        


    }

    @Override
    protected void onStart(){
        super.onStart();

        //---Query to get items of current user---//
        collectionReference
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


                                    CatItem catItem = document.toObject(CatItem.class).withId(docID);
                                    CatItemList.add(catItem);

                                }
                                //Invoke Recycler View
                                catItemRecyclerAdapter = new CatItemRecyclerAdapter(getApplicationContext(), CatItemList);
                                recyclerView.setAdapter(catItemRecyclerAdapter);
                                catItemRecyclerAdapter.notifyDataSetChanged();

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
