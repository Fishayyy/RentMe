package com.firebase.rentme.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.rentme.R;
import com.firebase.rentme.models.ListViewAdapter;
import com.firebase.rentme.models.Property;
import com.firebase.rentme.models.SwipeToDeleteCallback;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ViewFavoritesActivity extends AppCompatActivity
{

    private static final String TAG = "ViewListingsActivity";

    RecyclerView recyclerView;
    ArrayList<Property> propertyList = new ArrayList<>();
    ListViewAdapter adapter;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Log.d(TAG, "onCreate: started");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        relativeLayout = findViewById(R.id.relativeLayout);

        displayActivityName();
        populateList();
        buildAdapter();
        enableSwipeToDeleteAndUndo();
    }

    private void displayActivityName()
    {
        TextView activityTitle = (TextView) findViewById(R.id.activity_title);
        activityTitle.setText(R.string.favorites);
    }

    private void populateList()
    {

        //Test Objects
        Property objZ = new Property();
        objZ.setHousingCategory("Apartment");
        objZ.setPrice(23400);
        objZ.setPhotoURL("https://www.marriottonthefalls.com/wp-content/uploads/2012/10/upside-down-house-ext.jpg");
        objZ.setBio("A house");
        objZ.setAddress("231 Venado Ave.");
        objZ.setCity("Newbury Park");
        objZ.setZipCode("91320");
        objZ.setState("CA");
        objZ.setOwnerName("Charles Vosguanian");
        objZ.setOwnerPhoneNum("8054906140");
        objZ.setOwnerEmail("randombot360@gmail.com");
        objZ.setBedrooms(3);
        objZ.setBathrooms(2);
        objZ.setPetsAllowed(true);
        objZ.setSmokingAllowed(false);
        objZ.setParkingAvailable(true);
        objZ.setPoolAvailable(true);
        objZ.setBackyardAvailable(true);
        objZ.setLaundryAvailable(true);
        objZ.setHandicapAccessible(true);


        //Test Objects
        Property obj0 = new Property();
        objZ.setHousingCategory("Apartment");
        objZ.setPrice(23400);
        objZ.setPhotoURL("https://www.marriottonthefalls.com/wp-content/uploads/2012/10/upside-down-house-ext.jpg");
        objZ.setBio("A house");
        objZ.setAddress("231 Venado Ave.");
        objZ.setCity("Newbury Park");
        objZ.setZipCode("91320");
        objZ.setState("CA");
        objZ.setOwnerName("Charles Vosguanian");
        objZ.setOwnerPhoneNum("8054906140");
        objZ.setOwnerEmail("randombot360@gmail.com");
        objZ.setBedrooms(3);
        objZ.setBathrooms(2);
        objZ.setPetsAllowed(true);
        objZ.setSmokingAllowed(false);
        objZ.setParkingAvailable(true);
        objZ.setPoolAvailable(true);
        objZ.setBackyardAvailable(true);
        objZ.setLaundryAvailable(true);
        objZ.setHandicapAccessible(true);

        Property obj1 = new Property();
        objZ.setHousingCategory("Apartment");
        objZ.setPrice(23400);
        objZ.setPhotoURL("https://www.marriottonthefalls.com/wp-content/uploads/2012/10/upside-down-house-ext.jpg");
        objZ.setBio("A house");
        objZ.setAddress("231 Venado Ave.");
        objZ.setCity("Newbury Park");
        objZ.setZipCode("91320");
        objZ.setState("CA");
        objZ.setOwnerName("Charles Vosguanian");
        objZ.setOwnerPhoneNum("8054906140");
        objZ.setOwnerEmail("randombot360@gmail.com");
        objZ.setBedrooms(3);
        objZ.setBathrooms(2);
        objZ.setPetsAllowed(true);
        objZ.setSmokingAllowed(false);
        objZ.setParkingAvailable(true);
        objZ.setPoolAvailable(true);
        objZ.setBackyardAvailable(true);
        objZ.setLaundryAvailable(true);
        objZ.setHandicapAccessible(true);

        Property obj2 = new Property();
        objZ.setHousingCategory("Apartment");
        objZ.setPrice(23400);
        objZ.setPhotoURL("https://www.marriottonthefalls.com/wp-content/uploads/2012/10/upside-down-house-ext.jpg");
        objZ.setBio("A house");
        objZ.setAddress("231 Venado Ave.");
        objZ.setCity("Newbury Park");
        objZ.setZipCode("91320");
        objZ.setState("CA");
        objZ.setOwnerName("Charles Vosguanian");
        objZ.setOwnerPhoneNum("8054906140");
        objZ.setOwnerEmail("randombot360@gmail.com");
        objZ.setBedrooms(3);
        objZ.setBathrooms(2);
        objZ.setPetsAllowed(true);
        objZ.setSmokingAllowed(false);
        objZ.setParkingAvailable(true);
        objZ.setPoolAvailable(true);
        objZ.setBackyardAvailable(true);
        objZ.setLaundryAvailable(true);
        objZ.setHandicapAccessible(true);

        Property obj3 = new Property();
        objZ.setHousingCategory("Apartment");
        objZ.setPrice(23400);
        objZ.setPhotoURL("https://www.marriottonthefalls.com/wp-content/uploads/2012/10/upside-down-house-ext.jpg");
        objZ.setBio("A house");
        objZ.setAddress("231 Venado Ave.");
        objZ.setCity("Newbury Park");
        objZ.setZipCode("91320");
        objZ.setState("CA");
        objZ.setOwnerName("Charles Vosguanian");
        objZ.setOwnerPhoneNum("8054906140");
        objZ.setOwnerEmail("randombot360@gmail.com");
        objZ.setBedrooms(3);
        objZ.setBathrooms(2);
        objZ.setPetsAllowed(true);
        objZ.setSmokingAllowed(false);
        objZ.setParkingAvailable(true);
        objZ.setPoolAvailable(true);
        objZ.setBackyardAvailable(true);
        objZ.setLaundryAvailable(true);
        objZ.setHandicapAccessible(true);

        propertyList.add(obj0);
        propertyList.add(obj1);
        propertyList.add(obj2);
        propertyList.add(obj3);
    }

    private void buildAdapter()
    {
        adapter = new ListViewAdapter(this, propertyList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void enableSwipeToDeleteAndUndo()
    {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this)
        {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i)
            {

                final int position = viewHolder.getAdapterPosition();
                final Property item = adapter.getData().get(position);

                adapter.removeItem(position);

                Snackbar snackbar = Snackbar
                        .make(relativeLayout, item.getHousingCategory() + " was removed from the list", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {

                        adapter.restoreItem(item, position);
                        recyclerView.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.WHITE);
                snackbar.show();



            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    public void exitActivity(View view)
    {
        finish();
    }

}