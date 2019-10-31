package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    EditText editText2;
    TextView useridTextView;
    String userId = user.getUid();

    public void getuserid(View view){
        Toast.makeText(MainActivity.this, userId + " ", Toast.LENGTH_SHORT).show();
        useridTextView = findViewById(R.id.userIdTextView);
        useridTextView.setText(userId + " ");
    }



    public void profileClicked(View view){
        startActivity(new Intent(MainActivity.this, LogIn.class));
    }

    public void scrollingLink(View view){
        startActivity(new Intent(MainActivity.this, ScrollingActivity.class));
    }

    public void writeClicked(View view){

        editText2 = findViewById(R.id.editText2);

        String messageText = (editText2.getText().toString().trim());

        // Create a new user with a first and last name
        Map<String, Object> Message = new HashMap<>();
        Message.put("Message", messageText);

        // Add a new document with a generated ID
        db.collection("Message")
                .add(Message)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error adding document", e);
                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
