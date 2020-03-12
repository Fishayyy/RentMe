package com.firebase.rentme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class arrayAdapter extends ArrayAdapter {

    Context context;

    public arrayAdapter(Context context, int resourceId, List<Property> property)
    {
        super(context, resourceId, property);
    }


    public View getView(int position, View convertView, ViewGroup parent)
    {
        Property property = (Property) getItem(position);

        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.property_card, parent, false);
        }

        //Display Card Image
        ImageView propertyImage = convertView.findViewById(R.id.cardImage);
        String imageURL = property.getPhotoURL();
        //Loading image using Picasso
        Picasso.with(getContext()).load(imageURL).placeholder(R.drawable.ic_launcher_background).into(propertyImage);

        //Display Card Text
        TextView propertyType = (TextView) convertView.findViewById(R.id.attribute_type);
        //TextView propertyTypeBackground = (TextView) convertView.findViewById(R.id.attribute_typeBackground);
        TextView propertyCost = (TextView) convertView.findViewById(R.id.attribute_cost);
        TextView propertyCity = (TextView) convertView.findViewById(R.id.attribute_city);
        TextView propertyState = (TextView) convertView.findViewById(R.id.attribute_state);
        int type = property.getHousingCategory();
        String cost;
        String costResource = getContext().getString(R.string.propertyCost);
        String cityResource = getContext().getString(R.string.propertyCity);
        String stateResource = getContext().getString(R.string.propertyState);

        //context.getString(R.string.propertyState)

        switch(type)
        {
            case 0:
                propertyType.setText(R.string.type0);
                //propertyTypeBackground.setText(R.string.type0);
                break;
            case 1:
                propertyType.setText(R.string.type1);
                //propertyTypeBackground.setText(R.string.type1);
                break;
            case 2:
                propertyType.setText(R.string.type2);
                //propertyTypeBackground.setText(R.string.type2);
                break;
        }

        cost = String.format("%.2f", property.getPrice());
        propertyCost.setText(costResource + cost);
        propertyCity.setText(cityResource + property.getCity());
        propertyState.setText(stateResource + property.getState());

        return convertView;
    }

}
