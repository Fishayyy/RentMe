package com.firebase.rentme.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.rentme.R;
import com.firebase.rentme.models.Property;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CardViewAdapter extends ArrayAdapter
{

    public CardViewAdapter(Context context, int resourceId, List<Property> property)
    {
        super(context, resourceId, property);
    }


    public View getView(int position, View convertView, ViewGroup parent)
    {
        Property property = (Property) getItem(position);

        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.property_card, parent, false);
        }

        if (!property.getPhotoURL().isEmpty())
        {
            displayCardImage(property, convertView);
        }

        displayText(property, convertView);

        return convertView;
    }

    private void displayCardImage(Property property, View convertView)
    {
        ImageView propertyImage = convertView.findViewById(R.id.cardImage);
        Picasso.get().load(property.getPhotoURL())
                .placeholder(R.drawable.animated_loading)
                .error(R.drawable.error)
                .noFade()
                .into(propertyImage);
    }

    private void displayText(Property property, View convertView)
    {
        TextView propertyType = (TextView) convertView.findViewById(R.id.attribute_type);
        TextView propertyLocation = (TextView) convertView.findViewById(R.id.attribute_location);
        TextView propertyRate = (TextView) convertView.findViewById(R.id.attribute_rate);
        TextView propertyBedrooms = (TextView) convertView.findViewById(R.id.attribute_bedrooms);
        TextView propertyBathrooms = (TextView) convertView.findViewById(R.id.attribute_bathrooms);

        propertyType.setText(property.getHousingCategory());
        propertyLocation.setText(getContext().getString(R.string.location) + getContext().getString(R.string.locationFormat, property.getCity(), property.getState()));
        propertyRate.setText(getContext().getString(R.string.rateMonthFormat, property.getPrice()));
        propertyBedrooms.setText(getContext().getString(R.string.anyInt, property.getBedrooms()));
        if(((property.getBathrooms() / 0.5) % 2) == 0)
            propertyBathrooms.setText(getContext().getString(R.string.anyInt,(int) property.getBathrooms()));
        else
            propertyBathrooms.setText(getContext().getString(R.string.anyDouble, property.getBathrooms()));
    }
}