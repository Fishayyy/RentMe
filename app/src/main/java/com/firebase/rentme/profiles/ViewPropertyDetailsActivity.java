package com.firebase.rentme.profiles;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.firebase.rentme.R;
import com.firebase.rentme.models.Property;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ViewPropertyDetailsActivity extends FragmentActivity implements OnMapReadyCallback
{
    private static final String TAG = "ViewPropertyDetails";
    private Property property;
    private GoogleMap mMap;
    ArrayList<String> photos;
    private int currentPhotoIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property_card_details);

        Log.d(TAG, "onCreate: started");

        Intent intent = getIntent();
        property = intent.getParcelableExtra(Property.PARCELABLE_PROPERTY);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        displayCard(property);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        LatLng rentalCoordinates = getLocationFromAddress();
        String markerTitle = property.generatePostalAddress();
        if(rentalCoordinates == null)
        {
            rentalCoordinates = new LatLng(0,0);
            markerTitle = "Error";
        }
        mMap.addMarker(new MarkerOptions().position(rentalCoordinates).title(markerTitle));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(rentalCoordinates));
    }

    private void displayCard(Property property)
    {
        photos = property.getPhotoURLList();
        currentPhotoIndex = photos.size() - 1;
        displayPropertyImage(photos.get(currentPhotoIndex));
        displayPropertyStrings(property);
        displayPropertyBooleans(property);
    }

    private void displayPropertyImage(String photoURL)
    {
        ImageView propertyImage = findViewById(R.id.property_image);
        Picasso.get().load(photoURL)
                .placeholder(R.drawable.animated_loading)
                .error(R.drawable.error)
                .noFade()
                .into(propertyImage);
    }

    private void displayPropertyStrings(Property property)
    {
        TextView category = (TextView) findViewById(R.id.attribute_category);
        TextView rate = (TextView) findViewById(R.id.attribute_rate);
        TextView ownerName = (TextView) findViewById(R.id.attribute_name);
        TextView ownerPhone = (TextView) findViewById(R.id.attribute_phone);
        TextView ownerEmail = (TextView) findViewById(R.id.attribute_email);
        TextView bedrooms = (TextView) findViewById(R.id.attribute_bedrooms);
        TextView bathrooms = (TextView) findViewById(R.id.attribute_bathrooms);
        TextView bio = (TextView) findViewById(R.id.attribute_bio);

        category.setText(property.getHousingCategory());
        rate.setText(getApplicationContext().getString(R.string.rateMonthFormat, property.getPrice()));
        ownerName.setText(property.getOwnerName());
        ownerPhone.setText(property.getOwnerPhoneNum());
        ownerEmail.setText(property.getOwnerEmail());

        if(property.getBedrooms() == 0)
        {
            bedrooms.setText("Studio");
        }
        else
        {
            bedrooms.setText(getApplicationContext().getString(R.string.anyInt,(int) property.getBedrooms()));
        }
        if(((property.getBathrooms() / 0.5) % 2) == 0)
            bathrooms.setText(getApplicationContext().getString(R.string.anyInt,(int) property.getBathrooms()));
        else
            bathrooms.setText(getApplicationContext().getString(R.string.anyDouble, property.getBathrooms()));
        bio.setText(property.getBio());
    }

    private void displayPropertyBooleans(Property property)
    {
        ImageView parkingBool = findViewById(R.id.attribute_parking);
        ImageView yardBool = findViewById(R.id.attribute_yard);
        ImageView poolBool = findViewById(R.id.attribute_pool);
        ImageView laundryBool = findViewById(R.id.attribute_laundry);
        ImageView assistedBool = findViewById(R.id.attribute_assistedLiving);
        ImageView nonSmokingBool = findViewById(R.id.attribute_nonSmoking);
        ImageView petsAllowedBool = findViewById(R.id.attribute_petsAllowed);

        if(property.isParkingAvailable())
            parkingBool.setImageResource(R.drawable.ic_check_grey_24dp);
        if(property.isBackyardAvailable())
            yardBool.setImageResource(R.drawable.ic_check_grey_24dp);
        if(property.isPoolAvailable())
            poolBool.setImageResource(R.drawable.ic_check_grey_24dp);
        if(property.isLaundryAvailable())
            laundryBool.setImageResource(R.drawable.ic_check_grey_24dp);
        if(property.isHandicapAccessible())
            assistedBool.setImageResource(R.drawable.ic_check_grey_24dp);
        if(!property.isSmokingAllowed())
            nonSmokingBool.setImageResource(R.drawable.ic_check_grey_24dp);
        if(property.isPetsAllowed())
            petsAllowedBool.setImageResource(R.drawable.ic_check_grey_24dp);
    }

    public LatLng getLocationFromAddress() {
        String location = property.generatePostalAddress();
        Geocoder geocoder = new Geocoder(this);
        LatLng rentalCoordinates = null;
        List<Address> addresses;
        try
        {
            addresses = geocoder.getFromLocationName(location, 1);
            if(addresses.size() > 0)
            {
                rentalCoordinates = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return rentalCoordinates;
    }

    public void exitActivity(View view)
    {
        finish();
    }

    public void prevPhoto(View view)
    {
        if(currentPhotoIndex == 0)
        {
            currentPhotoIndex = photos.size() - 1;
        }
        else
        {
            currentPhotoIndex--;
        }
        displayPropertyImage(photos.get(currentPhotoIndex));
    }

    public void nextPhoto(View view)
    {
        if(currentPhotoIndex == photos.size() - 1)
        {
            currentPhotoIndex = 0;
        }
        else
        {
            currentPhotoIndex++;
        }
        displayPropertyImage(photos.get(currentPhotoIndex));
    }
}