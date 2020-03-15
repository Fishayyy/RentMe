package com.firebase.rentme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.rentme.models.Property;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.lang.reflect.Array;

public class CreatePropertyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    private int housingCategory;
    private EditText editTextPrice;
    private EditText editTextPhotoURL;
    private EditText editTextBio;
    private EditText editTextAddress;
    private EditText editTextCity;
    private EditText editTextZipCode;
    private EditText editTextState;
    private EditText editTextOwnerName;
    private EditText editTextOwnerPhoneNum;
    private EditText editTextOwnerEmail;

    private FirebaseFirestore db;
    private Query mQuery;

    private static final int LIMIT = 3;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_property);

        Spinner spinner = findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        FirebaseFirestore.setLoggingEnabled(true);

        initFirestore();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        housingCategory = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    private void initFirestore()
    {
        db = FirebaseFirestore.getInstance();
    }

    public void addProperty(View v)
    {
        Property newProperty = new Property(
                housingCategory,
                Double.parseDouble(editTextPrice.getText().toString()),
                editTextPhotoURL.getText().toString(),
                editTextBio.getText().toString(),
                editTextAddress.getText().toString(),
                editTextCity.getText().toString(),
                editTextZipCode.getText().toString(),
                editTextState.getText().toString(),
                editTextOwnerName.getText().toString(),
                editTextOwnerPhoneNum.getText().toString(),
                editTextOwnerEmail.getText().toString()
        );


        // Get a reference to the properties collection
        CollectionReference properties = db.collection("properties");

        // Add a new document containing the Property object to the properties collec        db.collection("users")tion
        properties.add(newProperty).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
        {
            @Override
            public void onSuccess(DocumentReference documentReference)
            {
                Toast toast = Toast.makeText(CreatePropertyActivity.this, "Created Listing", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();
                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast toast = Toast.makeText(CreatePropertyActivity.this, "Failed to Create Listing", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                        toast.show();
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}
