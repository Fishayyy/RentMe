package com.firebase.rentme;

//This is just a temporary class

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.rentme.models.Property;
import com.squareup.picasso.Picasso;

public class ViewContactCard extends AppCompatActivity
{
    private static final String TAG = "ViewContactCard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_contact_card);

        Log.d(TAG, "onCreate: started");

        //Test Values
        String imageURL0 = "https://specials-images.forbesimg.com/imageserve/1026205392/960x0.jpg?fit=scale";
        Property testProperty = new Property(Property.PropertyType.HOUSE.getValue(), 100.00, imageURL0, "This place rocks!", "123 Living St.", "Newbury Park", "California", "12345","Austin Fisher", "805-428-9476", "Mr.ATFisher@gmail.com");

        //Get property object from intent and pass it here
        displayCard(testProperty);
    }

    //Methods For Current Activity
    private void displayCard(Property property)
    {
        displayCardImage(property);
        displayCardText(property);
    }

    private void displayCardImage(Property property)
    {
        ImageView contactImage = findViewById(R.id.contact_image);
        String imageURL = property.getPhotoURL();

        //Loading image using Picasso
        Picasso.with(this).load(imageURL).placeholder(R.drawable.ic_launcher_background).into(contactImage);
    }

    private void displayCardText(Property property)
    {
        TextView owner = (TextView) findViewById(R.id.attribute_owner);
        TextView ownerEmail = (TextView) findViewById(R.id.attribute_owner_email);
        TextView ownerPhone = (TextView) findViewById(R.id.attribute_owner_phone);

        String userResource = getString(R.string.owner);
        String emailResource = getString(R.string.ownerEmail);
        String phoneResource = getString(R.string.ownerPhoneNum);

        owner.setText(userResource + property.getOwnerName());          //owner
        ownerEmail.setText(emailResource + property.getOwnerEmail());   //email
        ownerPhone.setText(phoneResource + property.getOwnerPhoneNum()); //phone

    }
}