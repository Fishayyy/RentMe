package com.firebase.rentme.models;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.firebase.rentme.R;
import com.firebase.rentme.profiles.ViewPropertyDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder>
{
    private ArrayList<Property> propertyList;
    Context context;

    public ListViewAdapter(Context context, ArrayList<Property> propertyList)
    {
        this.context = context;
        this.propertyList = propertyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.property_list_element, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        final Property property = propertyList.get(position);

        holder.attributeType.setText(propertyList.get(position).getHousingCategory());
        holder.attributeCity.setText("in " + propertyList.get(position).getCity());
        holder.attributeRate.setText(context.getString(R.string.rateFormat, property.getPrice()));
        Picasso.get().load(property.getPhotoURL())
                .placeholder(R.drawable.animated_loading)
                .error(R.drawable.error)
                .noFade()
                .into(holder.propertyThumbnail);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewPropertyDetailsActivity.class);
                intent.putExtra(Property.PARCELABLE_PROPERTY, propertyList.get(position));
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount()
    {
        return propertyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView propertyThumbnail;
        public TextView attributeType;
        public TextView attributeCity;
        public TextView attributeRate;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView)
        {
            super(itemView);
            this.propertyThumbnail = (ImageView) itemView.findViewById(R.id.property_thumbnail);
            this.attributeType = (TextView) itemView.findViewById(R.id.attribute_type);
            this.attributeCity = (TextView) itemView.findViewById(R.id.attribute_city);
            this.attributeRate = (TextView) itemView.findViewById(R.id.attribute_rate);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }

    public void removeItem(int position)
    {
        propertyList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Property item, int position)
    {
        propertyList.add(position, item);
        notifyItemInserted(position);
    }

    public ArrayList<Property> getData()
    {
        return propertyList;
    }
}