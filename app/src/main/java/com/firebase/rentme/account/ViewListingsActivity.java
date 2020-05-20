package com.firebase.rentme.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ViewListingsActivity extends AppCompatActivity
{

    private static final String TAG = "ViewListingsActivity";

    private RecyclerView recyclerView;
    private ArrayList<Property> propertyList = new ArrayList<>();
    private ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Log.d(TAG, "onCreate: started");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        displayActivityName();
        getOwnedPropertiesList();
        enableSwipeToDeleteAndUndo(this);
    }

    private void displayActivityName()
    {
        TextView activityTitle = (TextView) findViewById(R.id.activity_title);
        activityTitle.setText(R.string.listings);
    }

    private void getOwnedPropertiesList()
    {
        final FirebaseFirestore database = FirebaseFirestore.getInstance();

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
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

    private void populateList(User user)
    {
        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        ArrayList<String> targets = user.getOwnerProperties();

        while(targets.size() > 0)
        {
            database.collection("properties").document(targets.remove(0)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot)
                {
                    Property property = documentSnapshot.toObject(Property.class);
                    if(property != null)
                    {
                        propertyList.add(property);
                        buildAdapter();
                    }
                }
            });
        }
    }

    private void buildAdapter()
    {
        adapter = new ListViewAdapter(this, propertyList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void enableSwipeToDeleteAndUndo(final Context context)
    {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this)
        {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i)
            {

                final int position = viewHolder.getAdapterPosition();
                final Property item = adapter.getData().get(position);

                AlertDialog.Builder confirmDeleteDialog = new AlertDialog.Builder(context);
                confirmDeleteDialog.setTitle("Remove Listing");
                confirmDeleteDialog.setMessage("Are you sure you would like to take your listing off the market?");
                confirmDeleteDialog.setPositiveButton("Remove", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        FirebaseFirestore database = FirebaseFirestore.getInstance();
                        database.collection("users").document(FirebaseAuth.getInstance().getUid())
                                .update("ownerProperties", FieldValue.arrayRemove(item.getDocumentReferenceID()));
                        database.collection("properties").document(item.getDocumentReferenceID()).delete();
                        adapter.removeItem(position);
                    }
                });
                confirmDeleteDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        recyclerView.setAdapter(adapter);
                    }
                });

                confirmDeleteDialog.create().show();
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
