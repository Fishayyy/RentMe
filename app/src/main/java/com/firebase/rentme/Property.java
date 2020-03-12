package com.firebase.rentme;

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

    private int housingCategory = 0;
    private String title;
    private String photoURL;
    private double price;
    private String city;
    private String state;
    private int numRatings;
    private double avgRating;

    // firestore requires there to be an empty constructor
    public Property() {}

    public Property(int housingCategory, String photoURL, double price, String city, String state)
    {
        this.housingCategory = housingCategory;
        this.photoURL = photoURL;
        this.price = price;
        this.city = city;
        this.state = state;
    }

    public Property(String title, int housingCategory, String photoURL, double price, String city, String state, int numRatings, double avgRating)
    {
        this.housingCategory = housingCategory;
        this.title = title;
        this.photoURL = photoURL;
        this.price = price;
        this.city = city;
        this.state = state;
        this.numRatings = numRatings;
        this.avgRating = avgRating;
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
}
