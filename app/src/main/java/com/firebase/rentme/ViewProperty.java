package com.firebase.rentme;

//This is just a temporary class

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class ViewProperty extends AppCompatActivity
{


    private static final String TAG = "ViewProperty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_property);

        Log.d(TAG, "onCreate: started");
    }
}
