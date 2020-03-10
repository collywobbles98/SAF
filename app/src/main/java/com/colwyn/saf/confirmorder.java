package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

public class confirmorder extends AppCompatActivity {

    //---Firebase & Firestore---//
    private StorageReference mStorageRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //---Declare Widgets---//

    //---Buttons Clicked---//
    //Edit Basket
    public void editBasketClicked(View view){
        userData.basketEdit_Global = Boolean.TRUE;
        startActivity(new Intent(confirmorder.this, basket.class));
    }
    //Edit Delivery Address
    public void changeAddressClicked(View view){
        userData.addressEdit_Global = Boolean.TRUE;
        startActivity(new Intent(confirmorder.this, accountDetails.class));
    }
    //Edit Card
    public void changeCardClicked(View view){
        userData.paymentEdit_Global = Boolean.TRUE;
        startActivity(new Intent(confirmorder.this, paymentdetails.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmorder);



        //---Display data---//

        //Display Items

        //Display Card Details

        //Display Delivery

    }
}
