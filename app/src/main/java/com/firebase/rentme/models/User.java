package com.firebase.rentme.models;

import com.google.firebase.firestore.IgnoreExtraProperties;

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

    //Return hashcode of object
    public int describeContents()
    {
        return hashCode();
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
