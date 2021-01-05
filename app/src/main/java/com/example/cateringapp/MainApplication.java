package com.example.cateringapp;

import android.app.Application;

import com.google.firebase.auth.FirebaseUser;

public class MainApplication extends Application
{
    public static FirebaseUser currentUser;

    @Override
    public void onCreate() {
        super.onCreate();


    }
}
