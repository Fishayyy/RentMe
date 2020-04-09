package com.firebase.rentme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        TextView propertyCost = (TextView) convertView.findViewById(R.id.attribute_cost);
        TextView propertyCity = (TextView) convertView.findViewById(R.id.attribute_city);
        TextView propertyState = (TextView) convertView.findViewById(R.id.attribute_state);
        propertyType.setText(property.getHousingCategory());
        propertyCost.setText(getContext().getString(R.string.propertyCost, property.getPrice()));
        propertyCity.setText(getContext().getString(R.string.propertyCity, property.getCity()));
        propertyState.setText(getContext().getString(R.string.propertyState, property.getState()));
    }
}