package com.example.cateringapp;

import android.graphics.Bitmap;

public interface DatabaseOperationCallback {
    void OnOperationComplete();
    void OnImageOperationComplete(Bitmap image);
}
