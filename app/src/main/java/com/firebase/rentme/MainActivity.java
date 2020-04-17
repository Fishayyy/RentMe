package com.firebase.rentme;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.firebase.rentme.models.Property;
import com.firebase.rentme.models.ResultsFilter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";

    private FirebaseFirestore database;
    private CollectionReference propertiesCollection;
    private ListenerRegistration registration;
    private PlacesClient placesClient;
    private AutocompleteSupportFragment autocompleteFragment;
    private String locationQuery;
    private ResultsFilter propertiesFilter;

    private CardViewAdapter cardAdapter;
    private ArrayList<Property> propertyCardList;
    private ArrayList<Property> unfilteredProperties;
    private SwipeFlingAdapterView flingAdapterView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: started");

        initFirestore();

        initGooglePlaces();

        initCardArray();

        initCardFlingAdapterView();

        initFilter();
    }

    private void initFirestore()
    {
        database = FirebaseFirestore.getInstance();
        propertiesCollection = database.collection("properties");
    }

    private void initGooglePlaces()
    {
        // Initialize the SDK
        Places.initialize(getApplicationContext(), BuildConfig.GOOGLE_API_KEY);

        // Create a new Places client instance
        placesClient = Places.createClient(this);

        initAutoComplete();
    }

    private void initAutoComplete()
    {
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setTypeFilter(TypeFilter.REGIONS);

        autocompleteFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(-33.880490, 151.184363),
                new LatLng(-33.858754, 151.229596)));
        autocompleteFragment.setCountries("US");

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener()
        {
            @Override
            public void onPlaceSelected(Place place)
            {
                locationQuery = place.getName();
                initRealTimeListener();
            }

            @Override
            public void onError(Status status)
            {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    private void initCardArray()
    {
        propertyCardList = new ArrayList<>();
        unfilteredProperties = new ArrayList<>();
        cardAdapter = new CardViewAdapter(this, R.layout.property_card, propertyCardList);
    }

    private void initCardFlingAdapterView()
    {
        flingAdapterView = findViewById(R.id.cardFrame);
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
        if (registration != null)
        {
            registration.remove();
            unfilteredProperties.clear();
            cardAdapter.clear();
        }

        if (isNumeric(locationQuery))
        {
            queryByZip();
        }
        else
        {
            queryByCity();
        }
    }

    public static boolean isNumeric(String string)
    {
        if (string == null)
        {
            return false;
        }
        try
        {
            double d = Double.parseDouble(string);
        }
        catch (NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    public void queryByZip()
    {
        registration = propertiesCollection.whereEqualTo("zipCode", locationQuery).addSnapshotListener(new EventListener<QuerySnapshot>()
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
                            unfilteredProperties.add(dc.getDocument().toObject(Property.class));
                            break;
                        case MODIFIED:
                            Log.d(TAG, "Modified property: " + dc.getDocument().getData());
                            unfilteredProperties.add(dc.getDocument().toObject(Property.class));
                            break;
                        case REMOVED:
                            Log.d(TAG, "Removed property: " + dc.getDocument().getData());
                            break;
                    }
                }
                propertyCardList.addAll(propertiesFilter.getFilteredResults(unfilteredProperties));
                cardAdapter.notifyDataSetChanged();
            }
        });
    }

    public void queryByCity()
    {
        registration = propertiesCollection.whereEqualTo("city", locationQuery).addSnapshotListener(new EventListener<QuerySnapshot>()
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
                            unfilteredProperties.add(dc.getDocument().toObject(Property.class));
                            break;
                        case MODIFIED:
                            Log.d(TAG, "Modified property: " + dc.getDocument().getData());
                            unfilteredProperties.add(dc.getDocument().toObject(Property.class));
                            break;
                        case REMOVED:
                            Log.d(TAG, "Removed property: " + dc.getDocument().getData());
                            break;
                    }
                }
                propertyCardList.addAll(propertiesFilter.getFilteredResults(unfilteredProperties));
                cardAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initFilter()
    {
        propertiesFilter = new ResultsFilter();
        propertyCardList.addAll(propertiesFilter.getFilteredResults(unfilteredProperties));
    }

    public void createListing(View view)
    {
        Intent intent = new Intent(MainActivity.this, CreateListingActivity.class);
        startActivity(intent);
    }

    public void filterProperties(View view)
    {
        Intent intent = new Intent(MainActivity.this, CreatePropertyFilterActivity.class);
        intent.putExtra(ResultsFilter.PARCELABLE_FILTER, propertiesFilter);
        startActivityForResult(intent, 1);
    }

    public void setCurrentLocation(View view)
    {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                propertiesFilter = data.getParcelableExtra("result");
                propertyCardList.clear();
                propertyCardList.addAll(propertiesFilter.getFilteredResults(unfilteredProperties));
                cardAdapter.notifyDataSetChanged();
            }
        }
    }
}
