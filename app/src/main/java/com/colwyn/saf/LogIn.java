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
import com.google.firebase.auth.FirebaseUser;

public class LogIn extends AppCompatActivity {

    EditText txtEmail, txtPassword;
    Button btnLogIn;
    //Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;

    //Take user to Register Activity
    public void registerLink(View view){
        startActivity(new Intent(LogIn.this, Register.class));
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

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get Values
                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();

                //Check Form has been Filled in
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(LogIn.this, "Please enter an email address.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(LogIn.this, "Please enter a password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Check Password Length (>8)
                if(password.length() < 8) {
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
                                    //startActivity(new Intent(LogIn.this, MainActivity.class));
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
    }
}
