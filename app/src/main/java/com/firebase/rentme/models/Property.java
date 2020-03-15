package com.firebase.rentme.models;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Property
{

    public static final String FIELD_CATEGORY = "category";
    public static final String FIELD_PRICE = "price";
    public static final String FIELD_CITY = "city";
    public static final String FIELD_POPULARITY = "numRatings";
    public static final String FIELD_AVG_RATING = "avgRating";

    // Type
    // 0 - Apartment
    // 1 - Home
    // 2 - Room

    //Property Attributes
    private int housingCategory = 0;
    private String title;
    private String photoURL;
    private double price;
    private String city;
    private String state;
    private int numRatings;
    private double avgRating;

    //Contact Info
    private String ownerName;
    private String ownerCellNum;
    private String ownerEmail;

    // firestore requires there to be an empty constructor
    public Property()
    {

    }

    public Property(int housingCategory, String photoURL, double price, String city, String state, String ownerName, String ownerCellNum, String ownerEmail)
    {
        this.housingCategory = housingCategory;
        this.photoURL = photoURL;
        this.price = price;
        this.city = city;
        this.state = state;
        this.ownerName = ownerName;
        this.ownerCellNum = ownerCellNum;
        this.ownerEmail = ownerEmail;
    }

    public Property(String title, int housingCategory, String photoURL, double price, String city, String state, int numRatings, double avgRating, String ownerName, String ownerCellNum, String ownerEmail)
    {
        this.housingCategory = housingCategory;
        this.title = title;
        this.photoURL = photoURL;
        this.price = price;
        this.city = city;
        this.state = state;
        this.numRatings = numRatings;
        this.avgRating = avgRating;
        this.ownerName = ownerName;
        this.ownerCellNum = ownerCellNum;
        this.ownerEmail = ownerEmail;
    }

    //Get Values
    public int getHousingCategory()
    {
        return this.housingCategory;
    }
    public String getTitle()
    {
        return this.title;
    }

    public String getPhotoURL()
    {
        return this.photoURL;
    }

    public double getPrice()
    {
        return price;
    }

    public String getCity()
    {
        return this.city;
    }

    public String getState()
    {
        return this.state;
    }

    public int getNumRatings()
    {
        return numRatings;
    }

    public double getAvgRating()
    {
        return avgRating;
    }

    public String getOwnerName() { return ownerName; }

    public String getOwnerCellNum() { return ownerCellNum; }

    public String getOwnerEmail() { return  ownerEmail; }

    //Set Values
    public void setHousingCategory(int housingCategory)
    {
        this.housingCategory = housingCategory;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setPhotoURL(String photoURL)
    {
        this.photoURL = photoURL;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public void setNumRatings(int numRatings) {
        this.numRatings = numRatings;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public void setOwnerCellNum(String ownerCellNum) { this.ownerCellNum = ownerCellNum; }

    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }
}
