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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ViewListingsActivity extends AppCompatActivity
{

    private static final String TAG = "ViewListingsActivity";

    RecyclerView recyclerView;
    ArrayList<Property> propertyList = new ArrayList<>();
    ListViewAdapter adapter;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Log.d(TAG, "onCreate: started");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        relativeLayout = findViewById(R.id.relativeLayout);

        displayActivityName();
        getOwnedPropertiesList();
        enableSwipeToDeleteAndUndo();
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
                    propertyList.add(documentSnapshot.toObject(Property.class));
                    buildAdapter();
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

                /*
                Toast toast = new Toast(getApplicationContext());
                toast.makeText(getApplicationContext(),item.getHousingCategory() + " Deleted\n           Undo",Toast.LENGTH_LONG).show();
                 */


                Snackbar snackbar = Snackbar
                        .make(relativeLayout, item.getHousingCategory() + " was removed from the list.", Snackbar.LENGTH_LONG);
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
