package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Selling extends AppCompatActivity {

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

    }
}
