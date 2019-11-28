package com.colwyn.saf;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    EditText txtEmail, txtPassword, txtConfirmPassword;
    Button btnRegister;

    //Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;

    //Take user to login activity
    public void logInLink(View view){
        startActivity(new Intent(Register.this, LogIn.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Get Widgets
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        //progressBar = findViewById(R.id.progressBar);

        //Show progress bar
        //progressBar.setVisibility(View.GONE);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Register Button Clicked (OnClickListener)
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Get Values
                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                String confirmPassword = txtConfirmPassword.getText().toString().trim();

                //Check Form has been Filled in
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this, "Please enter an email address.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this, "Please enter a password.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(confirmPassword)){
                    Toast.makeText(Register.this, "Please confirm your password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Check Password Length (>8)
                if(password.length() < 8) {
                    Toast.makeText(Register.this, "Passwords must be at least 8 characters.", Toast.LENGTH_SHORT).show();
                }


                //Check passwords match
                if(password.equals(confirmPassword)){
                    //Sign up new users
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    //Hide Progress Bar
                                    //progressBar.setVisibility(View.VISIBLE);
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        //Log.d(TAG, "createUserWithEmail:success");
                                        //FirebaseUser user = mAuth.getCurrentUser();
                                        //updateUI(user);
                                        Toast.makeText(Register.this, "Registration Complete..",
                                                Toast.LENGTH_SHORT).show();
                                        //Go to Main Activity
                                        startActivity(new Intent(Register.this, MainActivity.class));


                                        //Put user into user table


                                        //******


                                    } else {
                                        // If sign in fails, display a message to the user.
                                        //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(Register.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        //updateUI(null);
                                    }

                                    // ...
                                }
                            });

                }
                else{
                    Toast.makeText(Register.this, "Passwords do not match.",Toast.LENGTH_SHORT).show();
                }




            }
        });

    }

}
