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
import com.firebase.rentme.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ViewFavoritesActivity extends AppCompatActivity
{

    private static final String TAG = "ViewListingsActivity";

    private FirebaseFirestore database;

    private RecyclerView recyclerView;
    private ArrayList<Property> propertyList = new ArrayList<>();
    private ListViewAdapter adapter;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Log.d(TAG, "onCreate: started");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        relativeLayout = findViewById(R.id.relativeLayout);

        getFavoritePropertiesList();
        enableSwipeToDeleteAndUndo();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        ArrayList<String> updatedFavorites = new ArrayList<>();

        for(Property property : propertyList)
        {
            updatedFavorites.add(property.getDocumentReferenceID());
        }

        database.collection("users").document(FirebaseAuth.getInstance().getUid()).update("ownerFavorites", updatedFavorites);
    }

    private void getFavoritePropertiesList()
    {
        database = FirebaseFirestore.getInstance();

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(userID != null)
        {
            DocumentReference userDocRef = database.collection("users").document(userID);

            userDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
            {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot)
                {
                    populateList(documentSnapshot.toObject(User.class));
                }
            });
        }
    }

    private void populateList(User user)
    {
        if(user != null)
        {
            ArrayList<String> targets = new ArrayList<>(user.getOwnerFavorites());

            while(targets.size() > 0)
            {
                final String target = targets.remove(0);

                database.collection("properties").document(target).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot)
                            {
                                Property property = documentSnapshot.toObject(Property.class);

                                if(property != null)
                                {
                                    propertyList.add(documentSnapshot.toObject(Property.class));
                                    buildAdapter();
                                }
                                else
                                {
                                    database.collection("users").document(FirebaseAuth.getInstance().getUid()).update("ownerFavorites", FieldValue.arrayRemove(target));
                                }
                            }
                        });
            }
        }
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