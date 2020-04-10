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
    private static final String TAG = "ViewContactCard";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property_card_detials);

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
        ImageView propertyImage = findViewById(R.id.property_image);
        Picasso.get().load(property.getPhotoURL())
                .placeholder(R.drawable.animated_loading)
                .error(R.drawable.error)
                .noFade()
                .into(propertyImage);
    }

    private void displayCardText(Property property)
    {
        TextView address = (TextView) findViewById(R.id.attribute_address);
        TextView ownerName = (TextView) findViewById(R.id.attribute_name);
        TextView ownerEmail = (TextView) findViewById(R.id.attribute_email);
        TextView ownerPhone = (TextView) findViewById(R.id.attribute_phone);
        TextView bio = (TextView) findViewById(R.id.attribute_bio);

        address.setText(getApplicationContext().getString(R.string.addressFormat, property.getAddress(), property.getCity(), property.getState(), property.getZipCode()));
        ownerName.setText(property.getOwnerName());
        ownerEmail.setText(property.getOwnerEmail());
        ownerPhone.setText(property.getOwnerPhoneNum());
        bio.setText(property.getBio());
    }
}