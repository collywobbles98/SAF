package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class addReview extends AppCompatActivity {

    //---Firestore---//
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //---Navigation---//
    public void cancelClicked (View view){
        startActivity(new Intent(addReview.this, CatListingView.class));
    }

    //---Upload Button---//
    public void uploadClicked(View view){
        uploadReview();
    }

    //---Declare Widgets---//
    ImageView star1ImageView, star2ImageView, star3ImageView, star4ImageView, star5ImageView;
    EditText reviewTitleEditText, reviewDescriptionEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        //Get Reviewer Name
        try {
            DocumentReference docRef = db.collection("user").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            //---Retrieve data and store as string---//
                            String FSUserEmail = document.getString("Email");
                            String FSFirstname = document.getString("First Name");
                            String FSLastname = document.getString("Last Name");

                            //---Save to global variable---//
                            userData.email_Global = FSUserEmail;
                            userData.firstname_Global = FSFirstname;
                            userData.lastname_Global = FSLastname;

                        }
                        else {

                        }
                    } else {

                    }
                }
            });
        }

        catch(Exception e) {
            //Error
        }

        //Default 1 star
        userData.reviewStars_Global = "1";

        //---Get Widgets---//
        star1ImageView = findViewById(R.id.star1ImageView);
        star2ImageView = findViewById(R.id.star2ImageView);
        star3ImageView = findViewById(R.id.star3ImageView);
        star4ImageView = findViewById(R.id.star4ImageView);
        star5ImageView = findViewById(R.id.star5ImageView);

        reviewTitleEditText = findViewById(R.id.reviewTitleEditText);
        reviewDescriptionEditText = findViewById(R.id.reviewDescriptionEditText);



        //---Set Onclick Listeners for each star---//
        star1ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set to 1 star
                star2ImageView.setImageResource(R.drawable.ic_star_border_black_24dp);
                star3ImageView.setImageResource(R.drawable.ic_star_border_black_24dp);
                star4ImageView.setImageResource(R.drawable.ic_star_border_black_24dp);
                star5ImageView.setImageResource(R.drawable.ic_star_border_black_24dp);
                userData.reviewStars_Global = "1";
            }
        });

        star2ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set to 2 Star
                star2ImageView.setImageResource(R.drawable.ic_star_black_24dp);
                star3ImageView.setImageResource(R.drawable.ic_star_border_black_24dp);
                star4ImageView.setImageResource(R.drawable.ic_star_border_black_24dp);
                star5ImageView.setImageResource(R.drawable.ic_star_border_black_24dp);
                userData.reviewStars_Global = "2";
            }
        });

        star3ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set to 3 star
                star2ImageView.setImageResource(R.drawable.ic_star_black_24dp);
                star3ImageView.setImageResource(R.drawable.ic_star_black_24dp);
                star4ImageView.setImageResource(R.drawable.ic_star_border_black_24dp);
                star5ImageView.setImageResource(R.drawable.ic_star_border_black_24dp);
                userData.reviewStars_Global = "3";
            }
        });

        star4ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set to 4 star
                star2ImageView.setImageResource(R.drawable.ic_star_black_24dp);
                star3ImageView.setImageResource(R.drawable.ic_star_black_24dp);
                star4ImageView.setImageResource(R.drawable.ic_star_black_24dp);
                star5ImageView.setImageResource(R.drawable.ic_star_border_black_24dp);
                userData.reviewStars_Global = "4";
            }
        });

        star5ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set to 5 star
                star2ImageView.setImageResource(R.drawable.ic_star_black_24dp);
                star3ImageView.setImageResource(R.drawable.ic_star_black_24dp);
                star4ImageView.setImageResource(R.drawable.ic_star_black_24dp);
                star5ImageView.setImageResource(R.drawable.ic_star_black_24dp);
                userData.reviewStars_Global = "5";
            }
        });


    }

    private void uploadReview(){

        //---Check Data is filled in---//

        if (TextUtils.isEmpty(reviewTitleEditText.getText().toString().trim())){
            //Display Error Message
            Toast.makeText(addReview.this, "Please Add a Title!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(reviewDescriptionEditText.getText().toString().trim())){
            Toast.makeText(addReview.this, "Please Add a Description!", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            //---Save Data---//

            Map<String, Object> reviews = new HashMap<>();
            reviews.put("UserID", user.getUid());
            reviews.put("Reviewer", userData.firstname_Global + " " + userData.lastname_Global);
            reviews.put("Item", userData.catItemClicked_Global);
            reviews.put("Title", reviewTitleEditText.getText().toString().trim());
            reviews.put("Description", reviewDescriptionEditText.getText().toString().trim());
            reviews.put("Stars", userData.reviewStars_Global);

            // Add a new document with a generated ID
            db.collection("reviews")
                    .add(reviews)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            //Go back to Main Activity
                            Toast.makeText(addReview.this, "Thanks for the review!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(addReview.this, CatListingView.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Log.w(TAG, "Error adding document", e);
                            Toast.makeText(addReview.this, "Cannot Add review at this time.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }



    }
}
