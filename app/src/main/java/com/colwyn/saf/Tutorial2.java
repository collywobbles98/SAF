package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Tutorial2 extends AppCompatActivity {

    //---Declare Widgets---//
    ImageView tutorialImageView;
    TextView tutorialTextView;
    Button nextButton, finishedButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial2);

        //---Get Widgets---//
        tutorialImageView = findViewById(R.id.tutorialImageView);
        tutorialTextView = findViewById(R.id.tutorialTextView);
        nextButton = findViewById(R.id.nextButton);
        finishedButton = findViewById(R.id.finishedButton);

        //---Next Button---//
        //Set counter to 1
        userData.tutorialCounter_Global = 0;

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userData.tutorialCounter_Global == 0){

                    //Change Image

                    // Show message

                    //Set Counter to 1
                    userData.tutorialCounter_Global = 1;

                }
                else if (userData.tutorialCounter_Global == 1){

                    //Change Image
                    tutorialImageView.setImageResource(R.drawable.basket_24dp);

                    // Show message
                    tutorialTextView.setText("Pay for your basket securely using your PayPal account in your preferred currency.");

                    //Set Counter to 2
                    userData.tutorialCounter_Global = 2;

                }
                else if (userData.tutorialCounter_Global == 2){

                    //Change Image
                    tutorialImageView.setImageResource(R.drawable.message_icon_png_4);

                    // Show message
                    tutorialTextView.setText("Chat with other users about listings and wanted ads.");

                    //Set Counter to 3
                    userData.tutorialCounter_Global = 3;

                }
                else if (userData.tutorialCounter_Global == 3){

                    //Change Image
                    tutorialImageView.setImageResource(R.drawable.ic_gavel_white_54dp);

                    // Show message
                    tutorialTextView.setText("Advertise and sell your very own items directly from within the app.");

                    //Set Counter to 4
                    userData.tutorialCounter_Global = 4;

                }
                else if (userData.tutorialCounter_Global == 4){

                    //Change Image
                    tutorialImageView.setImageResource(R.drawable.account_box_white);

                    // Show message
                    tutorialTextView.setText("From your profile you can: \n\nView your purchases\n\n View your sales\n\n Set your currency\nView Wanted adds\n\nEdit your details\n\n and more.");

                    //Set Counter to 5
                    userData.tutorialCounter_Global = 5;

                }

                else if (userData.tutorialCounter_Global == 5){
                    //Change Image
                    tutorialImageView.setImageResource(R.drawable.ic_create_black_24dp);

                    // Show message
                    tutorialTextView.setText("Please fill in your personal details to get the most out of the app.");

                    //Change Buttons
                    nextButton.setVisibility(View.GONE);
                    finishedButton.setVisibility(View.VISIBLE);

                }
                else{
                    //Do Nothing
                }
            }
        });

        //---Finished Button---//
        finishedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Tutorial2.this, accountDetails.class));
            }
        });


    }
}
