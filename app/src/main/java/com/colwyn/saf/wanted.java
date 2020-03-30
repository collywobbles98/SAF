package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class wanted extends AppCompatActivity {

    //---Navigation---//
    public void backClicked (View view){
        //Take user back to profile
        startActivity(new Intent(wanted.this, Profile.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wanted);
    }
}
