package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogIn extends AppCompatActivity {

    EditText txtEmail, txtPassword;
    Button btnLogIn, sendEmailButton, cancelResetButton;
    Button resetPasswordButton, signUpButton;
    TextView titleTextView;
    ImageView lockImageView;

    //Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    //Take user to Register Activity
    public void registerLink(View view){
        startActivity(new Intent(LogIn.this, Register.class));
    }

    //---Password Reset Button Clicked---//
    public void resetPasswordClicked(View view){

        //Get Widgets
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        titleTextView = findViewById(R.id.TextView);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogIn = findViewById(R.id.btnLogIn);
        lockImageView = findViewById(R.id.lockImageView);
        signUpButton = findViewById(R.id.signUpButton);
        sendEmailButton = findViewById(R.id.sendEmailButton);
        cancelResetButton = findViewById(R.id.cancelResetButton);

        //Change the form
        titleTextView.setText("RESET PASSWORD");
        txtPassword.setVisibility(View.GONE);
        lockImageView.setVisibility(View.GONE);
        resetPasswordButton.setVisibility(View.GONE);
        signUpButton.setVisibility(View.GONE);
        btnLogIn.setVisibility(View.GONE);
        sendEmailButton.setVisibility(View.VISIBLE);
        cancelResetButton.setVisibility(View.VISIBLE);

    }

    //---Send Reset Password---//
    public void sendEmailClicked(View view){


        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get Values
                String email = txtEmail.getText().toString().trim();

                //Check Form has been Filled in
                if (TextUtils.isEmpty(email)) {
                    //No Email Entered (Display Error Message)
                    Toast.makeText(LogIn.this, "Please enter an email address.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!TextUtils.isEmpty(email)) {
                    //Email Entered
                    auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LogIn.this, "Email Sent", Toast.LENGTH_SHORT).show();
                                        recreate();
                                        return;

                                    }

                                    else{
                                        Toast.makeText(LogIn.this, "An Error has occured. Please Check your Email.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }


            }
        });


    }

    //---Cancel Send Email---//
    public void cancelSendEmailClicked(View view){
        //Reload Activity
        recreate();
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //Get widgets
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogIn = findViewById(R.id.btnLogIn);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Try to check if there is a user session running
        try {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            userData.userID_Global = user.getUid();
        }
        catch(Exception e) {

            //No Current Firebase User, Let user sign in.
        }

        if (userData.userID_Global == null) {

            btnLogIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Get Values
                    String email = txtEmail.getText().toString().trim();
                    String password = txtPassword.getText().toString().trim();

                    //Check Form has been Filled in
                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(LogIn.this, "Please enter an email address.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(LogIn.this, "Please enter a password.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //Check Password Length (>8)
                    if (password.length() < 8) {
                        Toast.makeText(LogIn.this, "Passwords must be at least 8 characters.", Toast.LENGTH_SHORT).show();
                    }

                    //Sign in existing users
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LogIn.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        //Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        //updateUI(user);
                                        //Go to Main Activity
                                        startActivity(new Intent(LogIn.this, MainActivity.class));
                                        Toast.makeText(LogIn.this, "Welcome.",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        //Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LogIn.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        //updateUI(null);
                                    }

                                    // ...
                                }
                            });

                }
            });


        }//end if
        else {
            //Log in session already exists
            //Take user to main activity (removed animation)
            startActivity(new Intent(LogIn.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));

        }


    }
}
