package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.colwyn.saf.model.WantedItem;
import com.colwyn.saf.ui.WantedItemRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class wanted extends AppCompatActivity {

    //---Firebase & Firestore---//
    private StorageReference mStorageRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //---Get Widgets---//
    private List<WantedItem> WantedItemList;
    private WantedItemRecyclerAdapter wantedItemrecyclerAdapter;
    RecyclerView wantedRecyclerView;

    //---Navigation---//
    public void backClicked (View view){
        //Take user back to profile
        startActivity(new Intent(wanted.this, Profile.class));
    }

    public void addClicked (View view){
        //Take user to post a wanted ad
        startActivity(new Intent(wanted.this, addWanted.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wanted);

        //---Get Widgets---//
        WantedItemList = new ArrayList<>();
        wantedRecyclerView = findViewById(R.id.wantedRecyclerView);
        wantedRecyclerView.setHasFixedSize(true);
        wantedRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        //---Query to get all wanted ads in order of recent to oldest---//

        db.collection("wanted_ads").orderBy("TimeStamp")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                //Show Wanted Ads
                                //Get Document ID
                                String docID= document.getId();

                                //Add Items to list
                                WantedItem wantedItem = document.toObject(WantedItem.class).withId(docID);
                                WantedItemList.add(wantedItem);

                            }
                            //Invoke Recycler View
                            WantedItemRecyclerAdapter wantedItemRecyclerAdapter = new WantedItemRecyclerAdapter(getApplicationContext(), WantedItemList);
                            wantedRecyclerView.setAdapter(wantedItemRecyclerAdapter);
                            wantedItemRecyclerAdapter.notifyDataSetChanged();

                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

}
