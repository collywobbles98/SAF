package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ordercomplete extends AppCompatActivity {

    //---Declare Widgets---//
    TextView orderTextView;

    //---Buttons---//
    public void ordersClicked (View view){
        //To Orders Activity
    }
    public void homeClicked (View view){
        //To catalogue
        startActivity(new Intent(ordercomplete.this, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordercomplete);

        //---Get Widgets---//
        orderTextView = findViewById(R.id.orderTextView);

        //---Display Data---//
        orderTextView.setText("Order Reference:\n\n" + userData.orderRef_Global);

    }
}
