package com.firebase.rentme.models;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

@IgnoreExtraProperties
public class User
{
    //USER Attributes
    private String ownerName;
    private String ownerPhoneNum;
    private String ownerEmail;
    private ArrayList<String> ownerFavorites;
    private ArrayList<String> ownerProperties;

    // firestore requires there to be an empty constructor
    public User()
    {

    }

    // Get User Instance
    public static User getUserInstance()
    {
        final User[] user = new User[1];

        final FirebaseFirestore database = FirebaseFirestore.getInstance();

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference userDocRef = database.collection("users").document(userID);

        userDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
        {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                user[0] = documentSnapshot.toObject(User.class);
            }
        });

        while(user[0] == null) {}

        return user[0];
    }

    // Get Values
    public String getOwnerName()
    {
        return ownerName;
    }

    public String getOwnerPhoneNum()
    {
        return ownerPhoneNum;
    }

    public String getOwnerEmail()
    {
        return ownerEmail;
    }

    public ArrayList<String> getOwnerFavorites()
    {
        return ownerFavorites;
    }

    public ArrayList<String> getOwnerProperties()
    {
        return ownerProperties;
    }

    // Set Values
    public void setOwnerName(String ownerName)
    {
        this.ownerName = ownerName;
    }

    public void setOwnerPhoneNum(String ownerPhoneNum)
    {
        this.ownerPhoneNum = ownerPhoneNum;
    }

    public void setOwnerEmail(String ownerEmail)
    {
        this.ownerEmail = ownerEmail;
    }

    public void setOwnerFavorites(ArrayList<String> ownerFavorites) { this.ownerFavorites = ownerFavorites; }

    public void setOwnerProperties(ArrayList<String> ownerProperties) { this.ownerProperties = ownerProperties; }
}
