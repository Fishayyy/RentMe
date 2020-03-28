package com.firebase.rentme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.firebase.rentme.models.Property;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";

    private FirebaseFirestore db;
    private CollectionReference properties;
    private CardViewAdapter adapter;

    List<Property> cards;
    SwipeFlingAdapterView flingAdapterView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: started");

        initFirestore();

        cards = new ArrayList<Property>();

        adapter = new CardViewAdapter(this, R.layout.property_card, cards);

        loadProperties();

        flingAdapterView = findViewById(R.id.frame);
        flingAdapterView.setAdapter(adapter);
        flingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener()
        {
            Property property;

            @Override
            public void removeFirstObjectInAdapter()
            {
                property = cards.remove(0);
                cards.add(property);
                adapter.notifyDataSetChanged();
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

        flingAdapterView.setOnItemClickListener
                (new SwipeFlingAdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClicked(int i, Object o)
                    {
                        Intent intent = new Intent(MainActivity.this, ViewContactInfoActivity.class);
                        intent.putExtra(Property.PARCELABLE_PROPERTY, cards.get(0));
                        startActivity(intent);
                    }
                });
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        properties.addSnapshotListener(this, new EventListener<QuerySnapshot>()
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
                            Log.d(TAG, "New city: " + dc.getDocument().getData());
                            cards.add(dc.getDocument().toObject(Property.class));
                            adapter.notifyDataSetChanged();
                            break;
                        case MODIFIED:
                            Log.d(TAG, "Modified city: " + dc.getDocument().getData());
                            break;
                        case REMOVED:
                            Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                            break;
                    }
                }
            }
        });
    }

    //Initialize FireStore
    private void initFirestore()
    {
        db = FirebaseFirestore.getInstance();
        properties = db.collection("properties");
    }

    public void loadProperties()
    {
        properties.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
                {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots)
                    {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                        {
                            Property property = documentSnapshot.toObject(Property.class);
                            cards.add(property);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    //Add New Property to DB
    public void createListing(View view)
    {
        Intent intent = new Intent(MainActivity.this, CreateListingActivity.class);
        startActivity(intent);
    }
}
