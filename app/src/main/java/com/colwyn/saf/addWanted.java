package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class addWanted extends AppCompatActivity {

    //---Firestore---//
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //---Declare Widgets---//
    EditText wantedTitleEditText, wantedDescriptionEditText;

    //---Navigation---//
    public void backClicked (View view){
        //Take user back to profile
        startActivity(new Intent(addWanted.this, wanted.class));
    }

    //---Upload Button---//
    public void saveClicked ( View view){
        //Save add to firestore
        saveWantedAd();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wanted);

        //---Get Widgets---//
        wantedTitleEditText = findViewById(R.id.wantedTitleEditText);
        wantedDescriptionEditText = findViewById(R.id.wantedDescriptionEditText);

    }

    private void saveWantedAd(){

        //---Make Sure Fields are filled in---//
        if (TextUtils.isEmpty(wantedTitleEditText.getText().toString().trim())){
            //Title Missing prompt user to enter a title
            Toast.makeText(addWanted.this, "Please Add a Title!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(wantedDescriptionEditText.getText().toString().trim())){
            //Title Missing prompt user to enter a title
            Toast.makeText(addWanted.this, "Please Add a Description!", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            //Form Filled out. save data

            //Get Date
            //Timestamp
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String format = simpleDateFormat.format(new Date());

            Map<String, Object> wanted = new HashMap<>();
            wanted.put("PosterID", user.getUid());
            wanted.put("Title", wantedTitleEditText.getText().toString().trim());
            wanted.put("Description", wantedDescriptionEditText.getText().toString().trim());
            wanted.put("TimeStamp", format);
            wanted.put("ServerTimeStamp", FieldValue.serverTimestamp());

            db.collection("wanted_ads")
                    .add(wanted)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                            //Let user know ad has been posted and return to main page.
                            Toast.makeText(addWanted.this, "Ad Posted!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(addWanted.this, wanted.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Log.w(TAG, "Error adding document", e);
                            Toast.makeText(addWanted.this, "Your Ad could not be posted at this time.", Toast.LENGTH_SHORT).show();
                        }
                    });


        }
    }
}
