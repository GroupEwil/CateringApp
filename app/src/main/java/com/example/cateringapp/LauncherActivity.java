package com.example.cateringapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LauncherActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
            updateUI(currentUser);
        else
            SignIn();

    }
    void showLoading(boolean isShowing)
    {
        if(isShowing)
            progressBar.setVisibility(View.INVISIBLE);
        else
            progressBar.setVisibility(View.VISIBLE);
    }

    void updateUI(FirebaseUser user)
    {
        MainApplication.currentUser = user;
        DatabaseHelper.getInstance(getApplicationContext(), new DatabaseOperationCallback() {
            @Override
            public void OnOperationComplete() {

            }

            @Override
            public void OnImageOperationComplete(Bitmap image) {

            }
        });
        showLoading(false);
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();

    }

    void SignIn()
    {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}