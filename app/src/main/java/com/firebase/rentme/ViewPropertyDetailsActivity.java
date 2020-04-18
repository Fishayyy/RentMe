package com.firebase.rentme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.rentme.models.Property;
import com.squareup.picasso.Picasso;

public class ViewPropertyDetailsActivity extends AppCompatActivity
{
    private static final String TAG = "ViewPropertyDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property_card_details);

        Log.d(TAG, "onCreate: started");

        Intent intent = getIntent();
        Property property = intent.getParcelableExtra(Property.PARCELABLE_PROPERTY);

        displayCard(property);
    }

    private void displayCard(Property property)
    {
        displayPropertyImage(property);
        //displayLocation(property);
        displayPropertyStrings(property);
        displayPropertyBooleans(property);
    }

    private void displayPropertyImage(Property property)
    {
        ImageView propertyImage = findViewById(R.id.property_image);
        Picasso.get().load(property.getPhotoURL())
                .placeholder(R.drawable.animated_loading)
                .error(R.drawable.error)
                .noFade()
                .into(propertyImage);
    }

    private void displayLocation(Property property)
    {
        //private GoogleMap attributeLocation = google
    }

    private void displayPropertyStrings(Property property)
    {

        TextView rate = (TextView) findViewById(R.id.attribute_rate);
        TextView address = (TextView) findViewById(R.id.attribute_address);
        TextView ownerName = (TextView) findViewById(R.id.attribute_name);
        TextView ownerPhone = (TextView) findViewById(R.id.attribute_phone);
        TextView ownerEmail = (TextView) findViewById(R.id.attribute_email);
        TextView bedrooms = (TextView) findViewById(R.id.attribute_bedrooms);
        TextView bathrooms = (TextView) findViewById(R.id.attribute_bathrooms);
        //TextView parkingSpaces = (TextView) findViewById(R.id.attribute_parking);
        TextView bio = (TextView) findViewById(R.id.attribute_bio);

        rate.setText(getApplicationContext().getString(R.string.rateMonthFormat, property.getPrice()));
        address.setText(getApplicationContext().getString(R.string.addressFormat, property.getAddress(), property.getCity(), property.getState(), property.getZipCode()));
        ownerName.setText(property.getOwnerName());
        ownerPhone.setText(property.getOwnerPhoneNum());
        ownerEmail.setText(property.getOwnerEmail());
        bedrooms.setText(getApplicationContext().getString(R.string.anyInt, property.getBedrooms()));
        if(((property.getBathrooms() / 0.5) % 2) == 0)
            bathrooms.setText(getApplicationContext().getString(R.string.anyInt,(int) property.getBathrooms()));
        else
            bathrooms.setText(getApplicationContext().getString(R.string.anyDouble, property.getBathrooms()));
        //parkingSpaces.setText(property.getParking());
        bio.setText(property.getBio());
    }

    private void displayPropertyBooleans(Property property)
    {
        ImageView yardBool = findViewById(R.id.attribute_yard);
        ImageView poolBool = findViewById(R.id.attribute_pool);
        ImageView laundryBool = findViewById(R.id.attribute_laundry);
        ImageView assistedBool = findViewById(R.id.attribute_assistedLiving);
        ImageView nonSmokingBool = findViewById(R.id.attribute_nonSmoking);
        ImageView petsAllowedBool = findViewById(R.id.attribute_petsAllowed);

        if(property.hasBackyard())
            yardBool.setImageResource(R.drawable.ic_check_circle_green_24dp);
        if(property.hasPool())
            poolBool.setImageResource(R.drawable.ic_check_circle_green_24dp);
        if(property.hasLaundry())
            laundryBool.setImageResource(R.drawable.ic_check_circle_green_24dp);
        if(property.isHandicapAccessible())
            assistedBool.setImageResource(R.drawable.ic_check_circle_green_24dp);
        if(!property.isSmokingAllowed())
            nonSmokingBool.setImageResource(R.drawable.ic_check_circle_green_24dp);
        if(property.isPetsAllowed())
            petsAllowedBool.setImageResource(R.drawable.ic_check_circle_green_24dp);
    }
}