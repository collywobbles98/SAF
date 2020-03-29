package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class paymentOptions extends AppCompatActivity {

    //---Navigation---//
    //Back to basket
    public void backClicked (View view){
        startActivity(new Intent(paymentOptions.this, basket.class));
    }
    //Back to catalogue
    public void shoppingClicked (View view){
        startActivity(new Intent(paymentOptions.this, MainActivity.class));
    }
    //Card Payment Clicked
    public void cardPaymentClicked (View view){
        startActivity(new Intent(paymentOptions.this, paymentdetails.class));
    }
    //PayPal Payment Clicked
    public void paypalClicked (View view){
        //startActivity(new Intent(paymentOptions.this, paypal.class));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_options);


    }
}
