package com.firebase.rentme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.rentme.models.Property;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";
    private static final String PROPERTY = "com.firebase.rentme.PROPERTY";

    private FirebaseFirestore db;
    private Query dbQuery;
    private CollectionReference properties;

    private FirestoreAdapter adapter;
    List<Property> rowItems;

    SwipeFlingAdapterView flingAdapterView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: started");

        rowItems = new ArrayList<Property>();
        adapter = new FirestoreAdapter(this, R.layout.property_card, rowItems);

        initFirestore();

        flingAdapterView = findViewById(R.id.frame);
        flingAdapterView.setAdapter(adapter);
        flingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener()
        {
            @Override
            public void removeFirstObjectInAdapter()
            {
                rowItems.remove(0);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o)
            {
                Toast.makeText(getApplicationContext(), "Prev", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object o)
            {
                Toast.makeText(getApplicationContext(), "Next", Toast.LENGTH_SHORT).show();
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
                        Intent intent = new Intent(MainActivity.this, ViewContactCard.class);
                        intent.putExtra(PROPERTY, rowItems.get(0));
                        startActivity(intent);
                    }
                });

    }

    //Initialize FireStore
    private void initFirestore() {
        db = FirebaseFirestore.getInstance();

        dbQuery = db.collection("properties");
    }

    //Add New Property to DB
    public void gotoPostNew(View view)
    {
        Intent intent = new Intent(MainActivity.this, CreatePropertyActivity.class);
        startActivity(intent);
    }
}
