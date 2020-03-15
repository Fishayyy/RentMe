package com.firebase.rentme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.rentme.models.Property;
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

    private int swipeCount = 0;
    private boolean newDeck = true;

    private FirebaseFirestore db;
    private Query dbQuery;

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

        popluationLogic(newDeck);

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
                swipeCount++;
//                popluationLogic(!newDeck);
            }

            @Override
            public void onRightCardExit(Object o)
            {
                Toast.makeText(getApplicationContext(), "Next", Toast.LENGTH_SHORT).show();
                swipeCount++;
//                popluationLogic(!newDeck);

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

    private void initFirestore() {
        db = FirebaseFirestore.getInstance();

        dbQuery = db.collection("properties");
    }

    private void popluationLogic(boolean newDeck)
    {
        if (swipeCount % 5 == 0)
        {
            int addQuantity = 5;
            if (newDeck)
            {
                //Request Array
//                rowItems.addAll(Arrays.asList(tempArray0).subList(0, addQuantity));
            }

            //Request Array
//            rowItems.addAll(Arrays.asList(tempArray1).subList(0, addQuantity));
        }
    }

    //Tap-only Methods for External Activities
    public void gotoPostNew(View view)
    {
        Intent intent = new Intent(MainActivity.this, CreatePropertyActivity.class);
        startActivity(intent);
    }
}
