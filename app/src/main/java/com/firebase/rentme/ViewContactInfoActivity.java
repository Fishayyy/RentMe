package com.firebase.rentme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.rentme.models.Property;
import com.squareup.picasso.Picasso;

public class ViewContactInfoActivity extends AppCompatActivity
{
    private static final String TAG = "ViewContactCard";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_contact_card);

        Log.d(TAG, "onCreate: started");

        Intent i = getIntent();
        Property property = i.getParcelableExtra(Property.PARCELABLE_PROPERTY);

        displayCard(property);
    }

    private void displayCard(Property property)
    {
        displayCardImage(property);
        displayCardText(property);
    }

    private void displayCardImage(Property property)
    {
        ImageView contactImage = findViewById(R.id.contact_image);
        String imageURL = property.getPhotoURL();
        Picasso.with(this).load(imageURL).placeholder(R.drawable.ic_launcher_background).into(contactImage);
    }

    private void displayCardText(Property property)
    {
        TextView address = (TextView) findViewById(R.id.attribute_address);
        TextView owner = (TextView) findViewById(R.id.attribute_owner);
        TextView ownerEmail = (TextView) findViewById(R.id.attribute_owner_email);
        TextView ownerPhone = (TextView) findViewById(R.id.attribute_owner_phone);
        TextView bio = (TextView) findViewById(R.id.attribute_bio);

        address.setText(getApplicationContext().getString(R.string.addressFormat, property.getAddress(), property.getCity(), property.getState(), property.getZipCode()));
        owner.setText(property.getOwnerName());
        ownerEmail.setText(property.getOwnerEmail());
        ownerPhone.setText(property.getOwnerPhoneNum());
        bio.setText(property.getBio());
    }
}