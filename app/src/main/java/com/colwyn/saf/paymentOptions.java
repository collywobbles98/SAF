package com.colwyn.saf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.colwyn.saf.Paypal.Config;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class paymentOptions extends AppCompatActivity {

    //---Paypal---//
    public static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) //SandBox Environment
            .clientId(Config.paypal_ClientID);

    //---Declare Widgets---//
    CardView paypalcardView, cardCardView;

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
        userData.paymentMethod_Global = "card";
        startActivity(new Intent(paymentOptions.this, paymentdetails.class));
    }
    //PayPal Payment Clicked
    public void paypalClicked (View view){
        //startActivity(new Intent(paymentOptions.this, paypal.class));

    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_options);

        //---Start Paypal Service---//
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);


        //---Get Widgets---//
        paypalcardView = findViewById(R.id.paypalCardView);
        cardCardView = findViewById(R.id.cardCardView);

        //Hide CardPayment Option (Switched to Paypal Soft Delete Option)
        cardCardView.setVisibility(View.GONE);


        paypalcardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userData.paymentMethod_Global = "paypal";
                processPayment();
            }
        });


    }

    private void processPayment(){

        String Currency;
        //---Convert to PayPal Currencies---//
        if (userData.currency_Global.equals("(EUR)")){
            Currency = "EUR";
        }
        else if (userData.currency_Global.equals("(USD)")){
            Currency = "USD";
        }
        else if (userData.currency_Global.equals("(CAD)")){
            Currency = "CAD";
        }
        else if (userData.currency_Global.equals("(AUD)")){
            Currency = "AUD";
        }
        else if (userData.currency_Global.equals("(JPY)")){
            Currency = "JPY";
        }
        else{
            Currency = "GBP";
        }

        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(userData.subtotal_Global)), Currency,
                "Pay to Spark Air & Fuel", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this, confirmorder.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", userData.subtotal_Global));

                    } catch (JSONException e) {
                        //Error
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
        }
    }


}
