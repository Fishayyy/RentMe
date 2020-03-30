package com.firebase.rentme.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Property implements Parcelable
{

    public static final String PARCELABLE_PROPERTY = "com.firebase.rentme.PROPERTY";

    //Property Attributes
    private String housingCategory;
    private double price;
    private String photoURL;
    private String bio;
    private String address;
    private String city;
    private String zipCode;
    private String state;

    //Contact Info
    private String ownerName;
    private String ownerPhoneNum;
    private String ownerEmail;

    // firestore requires there to be an empty constructor
    public Property()
    {

    }

    //Parcel is for sending non-Primitive objects by intent
    public Property(Parcel parcel)
    {
        this.housingCategory = parcel.readString();
        this.price = parcel.readDouble();
        this.photoURL = parcel.readString();
        this.bio = parcel.readString();
        this.address = parcel.readString();
        this.city = parcel.readString();
        this.zipCode = parcel.readString();
        this.state = parcel.readString();
        this.ownerName = parcel.readString();
        this.ownerPhoneNum = parcel.readString();
        this.ownerEmail = parcel.readString();
    }

    //Standard Constructor
    public Property(String housingCategory, double price, String photoURL, String bio, String address, String city, String zipCode, String state, String ownerName, String ownerPhoneNum, String ownerEmail)
    {
        this.housingCategory = housingCategory;
        this.price = price;
        this.photoURL = photoURL;
        this.bio = bio;
        this.address = address;
        this.city = city;
        this.zipCode = zipCode;
        this.state = state;
        this.ownerName = ownerName;
        this.ownerPhoneNum = ownerPhoneNum;
        this.ownerEmail = ownerEmail;
    }

    //Methods for Parcelable
    //used when un-parceling our parcel (creating the object)
    public static final Parcelable.Creator<Property> CREATOR = new Parcelable.Creator<Property>()
    {
        @Override
        public Property createFromParcel(Parcel parcel)
        {
            return new Property(parcel);
        }

        @Override
        public Property[] newArray(int size)
        {
            return new Property[0];
        }
    };

    //write object values to parcel for storage
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(housingCategory);
        dest.writeDouble(price);
        dest.writeString(photoURL);
        dest.writeString(bio);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(zipCode);
        dest.writeString(state);
        dest.writeString(ownerName);
        dest.writeString(ownerPhoneNum);
        dest.writeString(ownerEmail);
    }

    //Return hashcode of object
    public int describeContents()
    {
        return hashCode();
    }

    //Get Values
    public String getHousingCategory()
    {
        return this.housingCategory;
    }

    public double getPrice()
    {
        return price;
    }

    public String getPhotoURL()
    {
        return this.photoURL;
    }

    public String getBio()
    {
        return this.bio;
    }

    public String getAddress()
    {
        return address;
    }

    public String getCity()
    {
        return this.city;
    }

    public String getZipCode()
    {
        return this.zipCode;
    }

    public String getState()
    {
        return this.state;
    }

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

    //Set Values
    public void setHousingCategory(String housingCategory)
    {
        this.housingCategory = housingCategory;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public void setPhotoURL(String photoURL)
    {
        this.photoURL = photoURL;
    }

    public void setBio(String bio)
    {
        this.bio = bio;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }

    public void setState(String state)
    {
        this.state = state;
    }

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
}
