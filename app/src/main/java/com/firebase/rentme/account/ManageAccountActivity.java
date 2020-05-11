package com.firebase.rentme.account;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.rentme.R;
import com.google.firebase.auth.FirebaseAuth;

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

    public void viewListings(View view)
    {
        Intent intent = new Intent(this, ViewListingsActivity.class);
        startActivity(intent);
    }

    public void exitActivity(View view)
    {
        finish();
    }

    public void signOut(View view)
    {
        AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
        passwordResetDialog.setTitle("Sign Out");
        passwordResetDialog.setMessage("Are you sure you would like to sign out?");
        passwordResetDialog.setPositiveButton("Sign Out", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
        passwordResetDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });

        passwordResetDialog.create().show();
    }
}
