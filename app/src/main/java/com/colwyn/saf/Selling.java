package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class Selling extends AppCompatActivity {

    public static final String TAG = "FireLog";

    //---Firestore---//
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //---Recycler View---//
    //private userItemsAdapter userItemsAdapter;
    private List<userItems> userItemsList;

    //---Declare Widgets---//
    private RecyclerView mUserItems;

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

        mUserItems = findViewById(R.id.userItemRecyclerView);

        userItemsList = new ArrayList<>();
        //userItemsAdapter = new userItems(userItemsList);

        //Get List of Items
        db.collection("listings").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if(e != null){
                    Toast.makeText(Selling.this, "Couldnt load Items",Toast.LENGTH_SHORT).show();
                }

                for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){

                    if(doc.getType() == DocumentChange.Type.ADDED){
                        //String Title = doc.getDocument().getString("Title");
                        //Log.d(TAG, "Title: " + Title);

                        userItems userItems = doc.getDocument().toObject(userItems.class);
                        userItemsList.add(userItems);
                    }
                }

                //for(DocumentSnapshot doc : queryDocumentSnapshots) {
                //    String Title = doc.getString("Title");
                //    Log.d(TAG, "Title: " + Title);
                //}
            }
        });

    }
}
