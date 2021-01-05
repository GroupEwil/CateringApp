package com.example.cateringapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String email ="";
    String password = "";
    String TAG = "Catering App";
    EditText usernameEditText;
    EditText passwordEditText;
    ProgressBar loadingProgressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
            updateUI(currentUser);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);

        final Button loginButton = findViewById(R.id.btnLogin);
        final Button signupButton = findViewById(R.id.btnSignup);
        loadingProgressBar = findViewById(R.id.loading);








        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                SignIn();

            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                SignUp();

            }
        });
    }

    void SignIn()
    {
        email = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            loadingProgressBar.setVisibility(View.INVISIBLE);
                            //updateUI(null);
                            // ...
                        }

                        // ...
                    }
                });
    }
    void SignUp()
    {
        email = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(LoginActivity.this, "Sign Up Successful",
                                    Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }
                        else
                        {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException(),
                                    Toast.LENGTH_LONG).show();
                            loadingProgressBar.setVisibility(View.INVISIBLE);
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
    void updateUI(FirebaseUser user)
    {
        MainApplication.currentUser = user;
        Toast.makeText(getApplicationContext(), "Welcome " + user.getDisplayName(),Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }
}