package com.firebase.rentme.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.rentme.R;

public class ManageAccountActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_account);
    }

    public void createListing(View view)
    {
        Intent intent = new Intent(this, CreatePropertyActivity.class);
        startActivity(intent);
    }
}
