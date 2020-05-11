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

public class ViewListingsActivity extends AppCompatActivity
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
        activityTitle.setText(R.string.listings);
    }

    private void populateList()
    {
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
        obj0.setHousingCategory("Apartment");
        obj0.setPrice(23400);
        obj0.setPhotoURL("https://www.marriottonthefalls.com/wp-content/uploads/2012/10/upside-down-house-ext.jpg");
        obj0.setBio("A house");
        obj0.setAddress("231 Venado Ave.");
        obj0.setCity("Newbury Park");
        obj0.setZipCode("91320");
        obj0.setState("CA");
        obj0.setOwnerName("Charles Vosguanian");
        obj0.setOwnerPhoneNum("8054906140");
        obj0.setOwnerEmail("randombot360@gmail.com");
        obj0.setBedrooms(3);
        obj0.setBathrooms(2);
        obj0.setPetsAllowed(true);
        obj0.setSmokingAllowed(false);
        obj0.setParkingAvailable(true);
        obj0.setPoolAvailable(true);
        obj0.setBackyardAvailable(true);
        obj0.setLaundryAvailable(true);
        obj0.setHandicapAccessible(true);

        Property obj1 = new Property();
        obj1.setHousingCategory("Apartment");
        obj1.setPrice(23400);
        obj1.setPhotoURL("https://www.marriottonthefalls.com/wp-content/uploads/2012/10/upside-down-house-ext.jpg");
        obj1.setBio("A house");
        obj1.setAddress("231 Venado Ave.");
        obj1.setCity("Newbury Park");
        obj1.setZipCode("91320");
        obj1.setState("CA");
        obj1.setOwnerName("Charles Vosguanian");
        obj1.setOwnerPhoneNum("8054906140");
        obj1.setOwnerEmail("randombot360@gmail.com");
        obj1.setBedrooms(3);
        obj1.setBathrooms(2);
        obj1.setPetsAllowed(true);
        obj1.setSmokingAllowed(false);
        obj1.setParkingAvailable(true);
        obj1.setPoolAvailable(true);
        obj1.setBackyardAvailable(true);
        obj1.setLaundryAvailable(true);
        obj1.setHandicapAccessible(true);

        Property obj2 = new Property();
        obj2.setHousingCategory("Apartment");
        obj2.setPrice(23400);
        obj2.setPhotoURL("https://www.marriottonthefalls.com/wp-content/uploads/2012/10/upside-down-house-ext.jpg");
        obj2.setBio("A house");
        obj2.setAddress("231 Venado Ave.");
        obj2.setCity("Newbury Park");
        obj2.setZipCode("91320");
        obj2.setState("CA");
        obj2.setOwnerName("Charles Vosguanian");
        obj2.setOwnerPhoneNum("8054906140");
        obj2.setOwnerEmail("randombot360@gmail.com");
        obj2.setBedrooms(3);
        obj2.setBathrooms(2);
        obj2.setPetsAllowed(true);
        obj2.setSmokingAllowed(false);
        obj2.setParkingAvailable(true);
        obj2.setPoolAvailable(true);
        obj2.setBackyardAvailable(true);
        obj2.setLaundryAvailable(true);
        obj2.setHandicapAccessible(true);

        Property obj3 = new Property();
        obj3.setHousingCategory("Apartment");
        obj3.setPrice(23400);
        obj3.setPhotoURL("https://www.marriottonthefalls.com/wp-content/uploads/2012/10/upside-down-house-ext.jpg");
        obj3.setBio("A house");
        obj3.setAddress("231 Venado Ave.");
        obj3.setCity("Newbury Park");
        obj3.setZipCode("91320");
        obj3.setState("CA");
        obj3.setOwnerName("Charles Vosguanian");
        obj3.setOwnerPhoneNum("8054906140");
        obj3.setOwnerEmail("randombot360@gmail.com");
        obj3.setBedrooms(3);
        obj3.setBathrooms(2);
        obj3.setPetsAllowed(true);
        obj3.setSmokingAllowed(false);
        obj3.setParkingAvailable(true);
        obj3.setPoolAvailable(true);
        obj3.setBackyardAvailable(true);
        obj3.setLaundryAvailable(true);
        obj3.setHandicapAccessible(true);

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

                /*
                Toast toast = new Toast(getApplicationContext());
                toast.makeText(getApplicationContext(),item.getHousingCategory() + " Deleted\n           Undo",Toast.LENGTH_LONG).show();
                 */


                Snackbar snackbar = Snackbar
                        .make(relativeLayout, item.getHousingCategory() + " was removed from the list.", Snackbar.LENGTH_LONG);
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
