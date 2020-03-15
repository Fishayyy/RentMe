package com.firebase.rentme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.rentme.models.Property;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    //Temporary Values
    //------------------------------------------------------------------------------------------------------------------------------
    String imageURL0 = "https://specials-images.forbesimg.com/imageserve/1026205392/960x0.jpg?fit=scale";
    String imageURL1 = "https://www.marriottonthefalls.com/wp-content/uploads/2012/10/upside-down-house-ext.jpg";
    String imageURL2 = "https://www.serebii.net/pokearth/sprites/yellow/001.png";
    String imageURL3 = "https://sg.portal-pokemon.com/play/resources/pokedex/img/pm/1f2551fec0bed487d3e7e55c5300aed0175e02f3.png";
    Property testProperty0 = new Property(0, imageURL0, 800, "Newbury Park", "California", "Austin Fisher", "805-428-9476", "Mr.ATFisher@gmail.com");
    Property testProperty1 = new Property(2, imageURL0, 1100, "Calabasas", "California","Austin Fisher", "805-428-9476", "Mr.ATFisher@gmail.com");
    Property testProperty2 = new Property(0, imageURL0, 1, "LA", "California","Austin Fisher", "805-428-9476", "Mr.ATFisher@gmail.com");
    Property testProperty3 = new Property(1, imageURL0, 2400, "Camarillo", "California","Austin Fisher", "805-428-9476", "Mr.ATFisher@gmail.com");
    Property testProperty4 = new Property(0, imageURL0, 10000, "Ventura", "California","Austin Fisher", "805-428-9476", "Mr.ATFisher@gmail.com");
    Property testProperty5 = new Property(2, imageURL0, 2894, "Las Vegas", "Nevada","Austin Fisher", "805-428-9476", "Mr.ATFisher@gmail.com");

    Property testProperty6 = new Property(0, imageURL2, 560.34, "Santa Barbra", "California","Austin Fisher", "805-428-9476", "Mr.ATFisher@gmail.com");
    Property testProperty7 = new Property(1, imageURL3, 64300, "New York", "New York","Austin Fisher", "805-428-9476", "Mr.ATFisher@gmail.com");
    Property testProperty8 = new Property(2, imageURL2, 2894, "Las Vegas", "Nevada","Austin Fisher", "805-428-9476", "Mr.ATFisher@gmail.com");
    Property testProperty9 = new Property(2, imageURL3, 2894, "Seattle", "Washington","Austin Fisher", "805-428-9476", "Mr.ATFisher@gmail.com");
    Property testProperty10 = new Property(2, imageURL2, 2894, "Silicon Vally", "California","Austin Fisher", "805-428-9476", "Mr.ATFisher@gmail.com");

    Property []tempArray0 = {testProperty1, testProperty2, testProperty3, testProperty4, testProperty5};
    Property []tempArray1 = {testProperty6, testProperty7, testProperty8, testProperty9, testProperty10};
    //------------------------------------------------------------------------------------------------------------------------------
    //Some Test Images
    //"https://www.serebii.net/pokearth/sprites/yellow/001.png";
    //"https://specials-images.forbesimg.com/imageserve/1026205392/960x0.jpg?fit=scale";
    //"https://www.marriottonthefalls.com/wp-content/uploads/2012/10/upside-down-house-ext.jpg";
    //"https://sg.portal-pokemon.com/play/resources/pokedex/img/pm/1f2551fec0bed487d3e7e55c5300aed0175e02f3.png"
    //------------------------------------------------------------------------------------------------------------------------------

    private static final String TAG = "MainActivity";
    private static final String OWNER = "com.firebase.rentme.OWNER";
    private static final String CELL = "com.firebase.rentme.CELL";
    private static final String EMAIL = "com.firebase.rentme.EMAIL";


    private int swipeCount = 0;
    private boolean newDeck = true;

    private arrayAdapter arrayAdapter;

    List<Property> rowItems;

    SwipeFlingAdapterView flingAdapterView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: started");

        rowItems = new ArrayList<Property>();
        arrayAdapter = new arrayAdapter(this, R.layout.property_card, rowItems);

        popluationLogic(newDeck);

        flingAdapterView = findViewById(R.id.frame);
        flingAdapterView.setAdapter(arrayAdapter);
        flingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener()
        {
            @Override
            public void removeFirstObjectInAdapter()
            {
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o)
            {
                Toast.makeText(getApplicationContext(),"Prev",Toast.LENGTH_SHORT).show();
                swipeCount++;
                popluationLogic(!newDeck);
            }

            @Override
            public void onRightCardExit(Object o)
            {
                Toast.makeText(getApplicationContext(),"Next",Toast.LENGTH_SHORT).show();
                swipeCount++;
                popluationLogic(!newDeck);

            }

            @Override
            public void onAdapterAboutToEmpty(int i) {}

            @Override
            public void onScroll(float v) {}

        });

        flingAdapterView.setOnItemClickListener
                (new SwipeFlingAdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClicked(int i, Object o)
                    {
                        Intent intent = new Intent(MainActivity.this, ViewContactCard.class);
                        startActivity(intent);
                    }
                });

    }

    private void popluationLogic(boolean newDeck)
    {
        if(swipeCount % 5 == 0)
        {
            int addQuantity = 5;
            if(newDeck)
            {
                //Request Array

                rowItems.addAll(Arrays.asList(tempArray0).subList(0, addQuantity));
            }

            //Request Array

            rowItems.addAll(Arrays.asList(tempArray1).subList(0, addQuantity));
        }
    }

    //Tap-only Methods for External Activities
    public void gotoPostNew(View view)
    {
        Intent intent = new Intent(MainActivity.this, ViewContactCard.class);
        startActivity(intent);
    }
}
