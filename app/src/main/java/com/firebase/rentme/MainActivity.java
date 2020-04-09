package com.firebase.rentme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.firebase.rentme.models.Property;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";

    private FirebaseFirestore database;
    private CollectionReference propertiesCollection;
    private CardViewAdapter cardAdapter;

    List<Property> propertyCardList;
    SwipeFlingAdapterView flingAdapterView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: started");

        initFirestore();

        initCardArray();

        initCardFlingAdapterView();

        initRealTimeListener();
    }

    private void initFirestore()
    {
        database = FirebaseFirestore.getInstance();
        propertiesCollection = database.collection("properties");
    }

    private void initCardArray()
    {
        propertyCardList = new ArrayList<>();
        cardAdapter = new CardViewAdapter(this, R.layout.property_card, propertyCardList);
    }

    private void initCardFlingAdapterView()
    {
        flingAdapterView = findViewById(R.id.frame);
        flingAdapterView.setAdapter(cardAdapter);
        flingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener()
        {
            Property property;

            @Override
            public void removeFirstObjectInAdapter()
            {
                property = propertyCardList.remove(0);
                propertyCardList.add(property);
                cardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o)
            {

            }

            @Override
            public void onRightCardExit(Object o)
            {

            }

            @Override
            public void onAdapterAboutToEmpty(int i)
            {

            }

            @Override
            public void onScroll(float v)
            {

            }
        });

        flingAdapterView.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClicked(int i, Object o)
            {
                Intent intent = new Intent(MainActivity.this, ViewContactInfoActivity.class);
                intent.putExtra(Property.PARCELABLE_PROPERTY, propertyCardList.get(0));
                startActivity(intent);
            }
        });
    }

    private void initRealTimeListener()
    {
        propertiesCollection.addSnapshotListener(new EventListener<QuerySnapshot>()
        {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e)
            {
                if (e != null)
                {
                    return;
                }

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges())
                {
                    switch (dc.getType())
                    {
                        case ADDED:
                            Log.d(TAG, "New property: " + dc.getDocument().getData());
                            propertyCardList.add(dc.getDocument().toObject(Property.class));
                            break;
                        case MODIFIED:
                            Log.d(TAG, "Modified property: " + dc.getDocument().getData());
                            propertyCardList.add(dc.getDocument().toObject(Property.class));
                            break;
                        case REMOVED:
                            Log.d(TAG, "Removed property: " + dc.getDocument().getData());
                            break;
                    }
                }

                cardAdapter.notifyDataSetChanged();
            }
        });
    }

    public void createListing(View view)
    {
        Intent intent = new Intent(MainActivity.this, CreateListingActivity.class);
        startActivity(intent);
    }
}
