package com.colwyn.saf;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class testactivity extends AppCompatActivity {

    EditText edittext;

    private StorageReference mStorageRef;

    public void uploadClicked (View view){


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testactivity);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        //---Get Values---//
        String userID = userData.userID_Global;



    }
}
