package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.colwyn.saf.model.encryption;

public class paymentdetails extends AppCompatActivity {

    //--Declare Widgets--//
    TextView disCardNum1TextView, disCardNum2TextView, disCardNum3TextView, disCardNum4TextView, disCardNameTextView, cardTypeTextView, disMonthTextView, disYearTextView, slashTextView, ccvTextView;
    EditText num1EditText, num2EditText, num3EditText, num4EditText, nameEditText, monthEditText, yearEditText, ccvEditText;
    Button confirmButton, cancelButton, layoutFillerButton;
    CardView mainCardView, ccvCardView;
    ProgressBar paymentProgressBar;

    //---Cancel Button---//
    public void cancelClicked(View view){
        startActivity(new Intent(paymentdetails.this, basket.class));
    }

    //Encryption Variables
    String outputString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentdetails);

        //--Get Widgets--//
        mainCardView = findViewById(R.id.mainCardView);
        ccvCardView = findViewById(R.id.ccvCardView);

        disCardNum1TextView = findViewById(R.id.disCardNum1TextView);
        disCardNum2TextView = findViewById(R.id.disCardNum2TextView);
        disCardNum3TextView = findViewById(R.id.disCardNum3TextView);
        disCardNum4TextView = findViewById(R.id.disCardNum4TextView);
        disCardNameTextView = findViewById(R.id.disCardNameTextView);
        disMonthTextView = findViewById(R.id.disMonthTextView);
        slashTextView = findViewById(R.id.slashTextView);
        disYearTextView = findViewById(R.id.disYearTextView);
        cardTypeTextView = findViewById(R.id.cardTypeTextView);

        num1EditText = findViewById(R.id.num1EditText);
        num2EditText = findViewById(R.id.num2EditText);
        num3EditText = findViewById(R.id.num3EditText);
        num4EditText = findViewById(R.id.num4EditText);

        nameEditText = findViewById(R.id.nameEditText);
        monthEditText = findViewById(R.id.monthEditText);
        yearEditText = findViewById(R.id.yearEditText);
        ccvEditText = findViewById(R.id.ccvEditText);
        ccvTextView = findViewById(R.id.ccvTextView);

        confirmButton = findViewById(R.id.confirmButton);
        cancelButton = findViewById(R.id.cancelButton);
        layoutFillerButton = findViewById(R.id.layoutFillerButton);

        //Make layout filler button invisible
        layoutFillerButton.setVisibility(View.INVISIBLE);

        //Hide a few Items
        ccvCardView.setVisibility(View.GONE);
        disCardNameTextView.setVisibility(View.INVISIBLE);
        disMonthTextView.setVisibility(View.INVISIBLE);
        disYearTextView.setVisibility(View.INVISIBLE);
        slashTextView.setVisibility(View.INVISIBLE);

        //Get progress Bar
        paymentProgressBar = findViewById(R.id.paymentProgressBar);
        paymentProgressBar.setVisibility(View.VISIBLE);



        //---Fill Card With Entered data---//
        //First Number Set
        num1EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //Display Entered Vales
                disCardNum1TextView.setText(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

                //Save Number to variable

                //Display Card Type when data is entered
                try{
                    //Display Type of Card (Visa (4) or MasterCard (5))
                    String cardType = disCardNum1TextView.getText().toString().trim();

                    char charCardType = cardType.charAt(0);

                    if (cardType.startsWith("5")){

                        cardTypeTextView.setText("MASTERCARD");

                    }

                    else if (cardType.startsWith("4")){

                        cardTypeTextView.setText("VISA");

                    }

                    else if (cardType.startsWith("3")){

                        cardTypeTextView.setText("TRAVEL");

                    }

                    else if (cardType.startsWith("6")){

                        cardTypeTextView.setText("DISCOVER");

                    }

                    else {
                        cardTypeTextView.setText("UNKNOWN");
                    }

                }
                //Revert to default if no data is entered
                catch(Exception e){
                    cardTypeTextView.setText("VISA");

                }

                //if Left Blank revert back to default card values
                if( TextUtils.isEmpty(num1EditText.getText())){
                    disCardNum1TextView.setText("");
                    cardTypeTextView.setText("VISA");
                }

            }
        });

        //Second Number Set
        num2EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //Display Entered Vales
                disCardNum2TextView.setText(s);

                //if Left Blank revert back to default card values
                if( TextUtils.isEmpty(num2EditText.getText())){
                    disCardNum2TextView.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        //Third Number Set
        num3EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Display Entered Vales
                disCardNum3TextView.setText(s);

                //if Left Blank revert back to default card values
                if( TextUtils.isEmpty(num3EditText.getText())){
                    disCardNum3TextView.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        //Fourth Number Set
        num4EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Display Entered Vales
                disCardNum4TextView.setText(s);

                //if Left Blank revert back to default card values
                if( TextUtils.isEmpty(num4EditText.getText())){
                    disCardNum4TextView.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        //CardHolder Name
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                disCardNameTextView.setVisibility(View.VISIBLE);
                //Display Entered Vales
                disCardNameTextView.setText(s);

                //if Left Blank revert back hide TextView
                if( TextUtils.isEmpty(nameEditText.getText())){
                    disCardNameTextView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Expiry Month
        monthEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                disMonthTextView.setVisibility(View.VISIBLE);
                slashTextView.setVisibility(View.VISIBLE);
                //Display Entered Vales
                disMonthTextView.setText(s);

                //if Left Blank revert back hide TextView
                if( TextUtils.isEmpty(monthEditText.getText())){
                    disMonthTextView.setVisibility(View.INVISIBLE);
                    slashTextView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Expiry Year
        yearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                disYearTextView.setVisibility(View.VISIBLE);
                //Display Entered Vales
                disYearTextView.setText(s);

                //if Left Blank revert back hide TextView
                if( TextUtils.isEmpty(yearEditText.getText())){
                    disYearTextView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //---Reverse Side of card---//
        ccvEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //User is entyering ccv
                if (hasFocus) {
                    mainCardView.setVisibility(View.GONE);
                    ccvCardView.setVisibility(View.VISIBLE);
                }
                else{
                    ccvCardView.setVisibility(View.GONE);
                    mainCardView.setVisibility(View.VISIBLE);
                }
            }
        });

        //CCV (Reverse/Other Card View)
        ccvEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Display Entered Vales
                ccvTextView.setText(s);

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        //Confirm Button
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //---Check Form has been filled in---//
                //Number1
                if(TextUtils.isEmpty(num1EditText.getText())){
                    Toast.makeText(paymentdetails.this, "Please enter your card number", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (num1EditText.getText().length() == 4){
                    String num1 = num1EditText.getText().toString().trim();
                    //Encrpt
                    String num1encrypted; //result
                    try {
                        num1encrypted = encryption.encrypt(num1);
                        //Toast.makeText(paymentdetails.this, num1encrypted, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                    }
                }
                else{
                    Toast.makeText(paymentdetails.this, "Please check your card number", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Number 2
                if(TextUtils.isEmpty(num2EditText.getText())){
                    Toast.makeText(paymentdetails.this, "Please enter your card number", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (num2EditText.getText().length() == 4){
                    String num2 = num2EditText.getText().toString().trim();
                    //Encrpt
                    String num2encrypted; //result
                    try {
                        num2encrypted = encryption.encrypt(num2);
                        //Toast.makeText(paymentdetails.this, num2encrypted, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                    }
                }
                else{
                    Toast.makeText(paymentdetails.this, "Please check your card number", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Number 3
                if(TextUtils.isEmpty(num3EditText.getText())){
                    Toast.makeText(paymentdetails.this, "Please enter your card number", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (num3EditText.getText().length() == 4){
                    String num3 = num3EditText.getText().toString().trim();
                    //Encrpt
                    String num3encrypted; //result
                    try {
                        num3encrypted = encryption.encrypt(num3);
                        //Toast.makeText(paymentdetails.this, num3encrypted, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                    }
                }
                else{
                    Toast.makeText(paymentdetails.this, "Please check your card number", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Number 4
                if(TextUtils.isEmpty(num4EditText.getText())){
                    Toast.makeText(paymentdetails.this, "Please enter your card number", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (num4EditText.getText().length() == 4){
                    String num4 = num4EditText.getText().toString().trim();
                    //Encrpt
                    String num4encrypted; //result
                    try {
                        num4encrypted = encryption.encrypt(num4);
                        //Toast.makeText(paymentdetails.this, num4encrypted, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                    }
                }
                else{
                    Toast.makeText(paymentdetails.this, "Please check your card number", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Name
                if(TextUtils.isEmpty(nameEditText.getText())){
                    Toast.makeText(paymentdetails.this, "Please enter the cardholders name", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    String name = nameEditText.getText().toString().trim();
                }
                //Expiration Month
                if(TextUtils.isEmpty(monthEditText.getText())){
                    Toast.makeText(paymentdetails.this, "Please enter the month of expiry", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (monthEditText.getText().length() == 2){
                    String month = monthEditText.getText().toString().trim();
                    //Encrpt
                    String monthencrypted; //result
                    try {
                        monthencrypted = encryption.encrypt(month);
                        //Toast.makeText(paymentdetails.this, monthencrypted, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                    }
                }
                else{
                    Toast.makeText(paymentdetails.this, "Please check your expiry month", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Expiration Year
                if(TextUtils.isEmpty(yearEditText.getText())){
                    Toast.makeText(paymentdetails.this, "Please enter the year of expiry", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (yearEditText.getText().length() == 2){
                    String year = yearEditText.getText().toString().trim();
                    //Encrpt
                    String yearencrypted; //result
                    try {
                        yearencrypted = encryption.encrypt(year);
                        //Toast.makeText(paymentdetails.this, yearencrypted, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                    }
                }
                else{
                    Toast.makeText(paymentdetails.this, "Please check your expiry year", Toast.LENGTH_SHORT).show();
                    return;
                }
                //CCV
                if(TextUtils.isEmpty(ccvEditText.getText())){
                    Toast.makeText(paymentdetails.this, "Please enter the CCV number on the back of your card", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (ccvEditText.getText().length() == 3){
                    String ccv = ccvEditText.getText().toString().trim();
                    //Encrpt
                    String ccvencrypted; //result
                    try {
                        ccvencrypted = encryption.encrypt(ccv);
                        //Toast.makeText(paymentdetails.this, ccvencrypted, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                    }
                }
                else{
                    Toast.makeText(paymentdetails.this, "Please check the CCV number on the back of your card", Toast.LENGTH_SHORT).show();
                    return;
                }

                //---Save Data to firebase---//
                //Show Progress Bar
                paymentProgressBar.setVisibility(View.VISIBLE);




            }
        });






    }
}
