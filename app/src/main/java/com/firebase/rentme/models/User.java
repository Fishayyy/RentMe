package com.firebase.rentme.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User implements Parcelable
{

    public static final String PARCELABLE_USER = "com.firebase.rentme.USER";

    //USER Attributes
    private String ownerName;
    private String ownerPhoneNum;
    private String ownerEmail;
    private String[] ownerFavorites;
    private String[] ownedHouses;


    // firestore requires there to be an empty constructor
    public User()
    {

    }

    //Parcel is for sending non-Primitive objects by intent
    public User(Parcel parcel)
    {
        this.ownerName = parcel.readString();
        this.ownerPhoneNum = parcel.readString();
        this.ownerEmail = parcel.readString();
        this.ownerFavorites = parcel.createStringArray();
        this.ownedHouses = parcel.createStringArray();
    }

    //Standard Constructor
    public User(String ownerName, String ownerPhoneNum, String ownerEmail, String[] ownerFavorites, String[] ownedHouses) {
        this.ownerName = ownerName;
        this.ownerPhoneNum = ownerPhoneNum;
        this.ownerEmail = ownerEmail;
        this.ownerFavorites = ownerFavorites;
        this.ownedHouses = ownedHouses;
    }

    //Methods for Parcelable
    //used when un-parceling our parcel (creating the object)
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>()
    {
        @Override
        public User createFromParcel(Parcel parcel)
        {
            return new User(parcel);
        }

        @Override
        public User[] newArray(int size)
        {
            return new User[0];
        }
    };

    //write object values to parcel for storage
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(ownerName);
        dest.writeString(ownerPhoneNum);
        dest.writeString(ownerEmail);
        dest.writeStringArray(ownerFavorites);
        dest.writeStringArray(ownedHouses);
    }

    //Return hashcode of object
    public int describeContents()
    {
        return hashCode();
    }

    // Get Values
    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerPhoneNum() {
        return ownerPhoneNum;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public String[] getOwnerFavorites() {
        return ownerFavorites;
    }

    public String[] getOwnedHouses() {
        return ownedHouses;
    }

    // Set Values
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setOwnerPhoneNum(String ownerPhoneNum) {
        this.ownerPhoneNum = ownerPhoneNum;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public void setOwnerFavorites(String[] ownerFavorites) {
        this.ownerFavorites = ownerFavorites;
    }

    public void setOwnedHouses(String[] ownedHouses) {
        this.ownedHouses = ownedHouses;
    }
}
