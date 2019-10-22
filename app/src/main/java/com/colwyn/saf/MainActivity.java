package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public void profileClicked(View view){
        startActivity(new Intent(MainActivity.this, LogIn.class));
    }

    public void scrollingLink(View view){
        startActivity(new Intent(MainActivity.this, ScrollingActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
