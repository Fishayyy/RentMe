package com.firebase.rentme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.rentme.account.ManageAccountActivity;
import com.firebase.rentme.filters.CreatePropertyFilterActivity;
import com.firebase.rentme.models.CardViewAdapter;
import com.firebase.rentme.models.Property;
import com.firebase.rentme.models.ResultsFilter;
import com.firebase.rentme.profiles.ViewPropertyDetailsActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private static final int REQUEST_CODE_SIGN_IN = 2;
    private static final int REQUEST_CODE_FILTER_RESULTS = 3;

    private FirebaseFirestore database;
    private FirebaseUser user;
    private CollectionReference propertiesCollection;
    private ListenerRegistration registration;
    private AutocompleteSupportFragment autocompleteFragment;
    private String locationQuery;
    private ResultsFilter propertiesFilter;

    private ImageButton getCurrentLocationButton;
    private boolean isPressed;

    private CardViewAdapter cardAdapter;
    private ArrayList<Property> propertyCardList;
    private ArrayList<Property> unfilteredProperties;
    private SwipeFlingAdapterView flingAdapterView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        signInUser();
    }

    private void signInUser()
    {
        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null)
        {
            List<AuthUI.IdpConfig> providers = Arrays.asList( new AuthUI.IdpConfig.EmailBuilder().build() );
            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setTheme(R.style.AppTheme)
                            .build(),
                    REQUEST_CODE_SIGN_IN);
        }
        else
        {
            startAppSession();
        }
    }

    private void startAppSession()
    {
        initFirestore();

        initGooglePlaces();

        initCardArray();

        initCardFlingAdapterView();

        initFilter();

        initLocationButton();
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
        PlacesClient placesClient = Places.createClient(this);

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
                if (isPressed)
                {
                    getCurrentLocationButton.performClick();
                }
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
            //Something wrong here?
            @Override
            public void onItemClicked(int i, Object o)
            {
                Intent intent = new Intent(MainActivity.this, ViewPropertyDetailsActivity.class);
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
                refreshCards();
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
                refreshCards();
            }
        });
    }

    public void refreshCards()
    {
        propertyCardList.addAll(propertiesFilter.getFilteredResults(unfilteredProperties));
        cardAdapter.notifyDataSetChanged();
        if (cardAdapter.isEmpty())
        {
            Toast.makeText(this, "Search Returned No Results", Toast.LENGTH_SHORT).show();
        }
    }

    private void initFilter()
    {
        propertiesFilter = new ResultsFilter();
        propertyCardList.addAll(propertiesFilter.getFilteredResults(unfilteredProperties));
    }

    public void filterProperties(View view)
    {
        Intent intent = new Intent(MainActivity.this, CreatePropertyFilterActivity.class);
        intent.putExtra(ResultsFilter.PARCELABLE_FILTER, propertiesFilter);
        startActivityForResult(intent, REQUEST_CODE_FILTER_RESULTS);
    }

    public void initLocationButton()
    {
        getCurrentLocationButton = findViewById(R.id.currentLocationButton);
        isPressed = false;
        getCurrentLocationButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_LOCATION_PERMISSION);
                }
                else
                {
                    if (isPressed)
                    {
                        getCurrentLocationButton.setBackgroundResource(R.drawable.location_unpressed);
                        isPressed = false;
                    }
                    else
                    {
                        getCurrentLocationButton.setBackgroundResource(R.drawable.location_pressed);
                        autocompleteFragment.setText("");
                        findCurrentLocation();
                        isPressed = true;
                    }
                }
            }
        });
        getCurrentLocationButton.performClick();
    }

    public void findCurrentLocation()
    {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback()
                {
                    @Override
                    public void onLocationResult(LocationResult locationResult)
                    {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0)
                        {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();

                            try
                            {
                                Geocoder geocoder;
                                List<Address> addresses;
                                geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                addresses = geocoder.getFromLocation(latitude, longitude, 1);

                                if (addresses.size() > 0)
                                {
                                    Address currentLocation = addresses.get(0);
                                    locationQuery = currentLocation.getPostalCode();
                                    initRealTimeListener();
                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this, "Location not Found", Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }

                    }
                }, Looper.getMainLooper());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FILTER_RESULTS)
        {
            if (resultCode == RESULT_OK)
            {
                propertiesFilter = data.getParcelableExtra("result");
                propertyCardList.clear();
                propertyCardList.addAll(propertiesFilter.getFilteredResults(unfilteredProperties));
                cardAdapter.notifyDataSetChanged();
                if (cardAdapter.isEmpty())
                {
                    Toast.makeText(this, "Filtering Returned No Results", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if (requestCode == REQUEST_CODE_SIGN_IN)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK)
            {
                startAppSession();
            }
            else
            {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0)
        {
            getCurrentLocationButton.performClick();
        }
        else
        {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    public void viewAccountInfo(View view)
    {
        Intent intent = new Intent(this, ManageAccountActivity.class);
        startActivity(intent);
    }
}
