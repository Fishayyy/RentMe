package com.firebase.rentme.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.jaygoo.widget.SeekBar;

import java.util.ArrayList;

public class ResultsFilter implements Parcelable
{
    public static final String PARCELABLE_FILTER = "com.firebase.rentme.FILTER";

    public static final double[] bathroomValue = {1.0, 1.5, 2.0, 2.5, 3.0, 4.0};
    public static final int[] bedroomValue = {0, 1, 2, 3, 4, 5};
    public static final int ANY = 0;
    public static final int YES = 1;
    public static final int NO = 2;

    private ArrayList<Property> unfilteredProperties;
    private ArrayList<Property> results;

    private double minPrice;
    private double maxPrice;
    private int bedroomsMinValue;
    private int bedroomsMaxValue;
    private double bathroomsMinValue;
    private double bathroomsMaxValue;
    private String housingCategory;
    private int petsAllowed;
    private int smokingAllowed;
    private boolean hasParkingAvailable;
    private boolean hasPool;
    private boolean hasBackyard;
    private boolean hasLaundry;
    private boolean isHandicapAccessible;

    public ResultsFilter(Parcel parcel)
    {
        //TODO: Possible Debug
        parcel.readTypedList(unfilteredProperties, Property.CREATOR);
        parcel.readTypedList(results, Property.CREATOR);
        minPrice = parcel.readDouble();
        maxPrice = parcel.readDouble();
        bedroomsMinValue = parcel.readInt();
        bedroomsMaxValue = parcel.readInt();
        bathroomsMinValue = parcel.readDouble();
        bathroomsMaxValue = parcel.readDouble();
        housingCategory = parcel.readString();
        petsAllowed = parcel.readInt();
        smokingAllowed = parcel.readInt();
        hasParkingAvailable = parcel.readBoolean();
        hasPool = parcel.readBoolean();
        hasBackyard = parcel.readBoolean();
        hasLaundry = parcel.readBoolean();
        isHandicapAccessible = parcel.readBoolean();
    }

    public ResultsFilter(ArrayList<Property> propertiesByLocation)
    {
        unfilteredProperties = new ArrayList<>(propertiesByLocation);
        setDefaultFilter();
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
        dest.writeTypedList(unfilteredProperties);
        dest.writeTypedList(results);
        dest.writeDouble(minPrice);
        dest.writeDouble(maxPrice);
        dest.writeInt(bedroomsMinValue);
        dest.writeInt(bedroomsMaxValue);
        dest.writeDouble(bathroomsMinValue);
        dest.writeDouble(bathroomsMaxValue);
        dest.writeString(housingCategory);
        dest.writeInt(petsAllowed);
        dest.writeInt(smokingAllowed);
        dest.writeBoolean(hasParkingAvailable);
        dest.writeBoolean(hasPool);
        dest.writeBoolean(hasBackyard);
        dest.writeBoolean(hasLaundry);
        dest.writeBoolean(isHandicapAccessible);
    }

    //Return hashcode of object
    public int describeContents()
    {
        return hashCode();
    }
    
    public void setDefaultFilter()
    {
        results = new ArrayList<>(unfilteredProperties);
        minPrice = 0.0;
        maxPrice = Double.MAX_VALUE;
        bedroomsMinValue = bedroomValue[0];
        bedroomsMaxValue =  bedroomValue[bedroomValue.length - 1];
        bathroomsMinValue = bathroomValue[0];
        bathroomsMaxValue = bathroomValue[bedroomValue.length - 1];
        housingCategory = "Any";
        petsAllowed = ANY;
        smokingAllowed = ANY;
        hasParkingAvailable = false;
        hasPool = false;
        hasBackyard = false;
        hasLaundry = false;
        isHandicapAccessible = false;
    }
    
    public ArrayList<Property> getFilteredResults()
    {
        if (minPrice != 0.0 || maxPrice != Double.MAX_VALUE)
        {
            filterByPrice();
        }
        if (bedroomsMinValue != bedroomValue[0] || bedroomsMaxValue != bedroomValue[bedroomValue.length - 1])
        {
            filterByBedrooms();
        }
        if (bathroomsMinValue != bathroomValue[0] || bathroomsMaxValue != bathroomValue[bathroomValue.length - 1])
        {
            filterByBathrooms();
        }
        if (!housingCategory.equals("Any"))
        {
            filterByHousingCategory();
        }
        if (petsAllowed != ANY)
        {
            filterByPetsAllowed();
        }
        if (smokingAllowed != ANY)
        {
            filterBySmokingAllowed();
        }
        if (hasParkingAvailable)
        {
            filterByParkingAvailable();
        }
        if (hasPool)
        {
            filterByPool();
        }
        if (hasBackyard)
        {
            filterByBackyard();
        }
        if (hasLaundry)
        {
            filterByLaundry();
        }
        if (isHandicapAccessible)
        {
            filterByHandicapAccessible();
        }

        return results;
    }
    
    private void filterByPrice()
    {
        Property[] properties = (Property[]) results.toArray();

        for (Property property : properties)
        {
            double price = property.getPrice();

            if (price < minPrice || price > maxPrice)
            {
                results.remove(property);
            }
        }
    }

    private void filterByBedrooms()
    {
        Property[] properties = (Property[]) results.toArray();

        for (Property property : properties)
        {
            int bedrooms = property.getBedrooms();

            if (bedrooms < bedroomsMinValue || (bedrooms > bedroomsMaxValue && bedroomsMaxValue != bedroomValue[bedroomValue.length - 1]))
            {
                results.remove(property);
            }
        }
    }

    private void filterByBathrooms()
    {
        Property[] properties = (Property[]) results.toArray();

        for (Property property : properties)
        {
            double bathrooms = property.getBathrooms();

            if (bathrooms < bathroomsMinValue || (bathrooms > bathroomsMaxValue && bathroomsMaxValue != bathroomValue[bathroomValue.length - 1]))
            {
                results.remove(property);
            }
        }
    }

    private void filterByHousingCategory()
    {
        Property[] properties = (Property[]) results.toArray();

        for (Property property : properties)
        {
            String type = property.getHousingCategory();

            if (!type.equals(housingCategory))
            {
                results.remove(property);
            }
        }
    }

    private void filterByPetsAllowed()
    {
        Property[] properties = (Property[]) results.toArray();

        for (Property property : properties)
        {
            if ((petsAllowed == YES && !property.isPetsAllowed()) || (petsAllowed == NO && property.isPetsAllowed()))
            {
                results.remove(property);
            }
        }
    }

    private void filterBySmokingAllowed()
    {
        Property[] properties = (Property[]) results.toArray();

        for (Property property : properties)
        {
            if ((smokingAllowed == YES && !property.isSmokingAllowed()) || (smokingAllowed == NO && property.isSmokingAllowed()))
            {
                results.remove(property);
            }
        }
    }

    private void filterByParkingAvailable()
    {
        Property[] properties = (Property[]) results.toArray();

        for (Property property : properties)
        {
            if (!property.hasParking())
            {
                results.remove(property);
            }
        }
    }

    private void filterByPool()
    {
        Property[] properties = (Property[]) results.toArray();

        for (Property property : properties)
        {
            if (!property.hasPool())
            {
                results.remove(property);
            }
        }
    }

    private void filterByBackyard()
    {
        Property[] properties = (Property[]) results.toArray();

        for (Property property : properties)
        {
            if (!property.hasBackyard())
            {
                results.remove(property);
            }
        }
    }

    private void filterByLaundry()
    {
        Property[] properties = (Property[]) results.toArray();

        for (Property property : properties)
        {
            if (!property.hasLaundry())
            {
                results.remove(property);
            }
        }
    }

    private void filterByHandicapAccessible()
    {
        Property[] properties = (Property[]) results.toArray();

        for (Property property : properties)
        {
            if (!property.isHandicapAccessible())
            {
                results.remove(property);
            }
        }
    }

    public double getMinPrice()
    {
        return minPrice;
    }

    public void setMinPrice(double minPrice)
    {
        this.minPrice = minPrice;
    }

    public double getMaxPrice()
    {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice)
    {
        this.maxPrice = maxPrice;
    }

    public float getBedroomsMinValueProgress()
    {
        int index = 0;
        while(index < bedroomValue.length && bedroomsMinValue != bedroomValue[index])
            index++;

        return index * 20;
    }

    public void setBedroomsMinValue(SeekBar leftBar)
    {
       float progress = leftBar.getProgress();
       int progressIndex = (int) (progress/20);
       bedroomsMinValue = bedroomValue[progressIndex];
    }

    public float getBedroomsMaxValueProgress()
    {
        int index = 0;
        while(index < bedroomValue.length && bedroomsMaxValue != bedroomValue[index])
            index++;

        return index * 20;
    }

    public void setBedroomsMaxValue(SeekBar rightBar)
    {
        float progress = rightBar.getProgress();
        int progressIndex = (int) (progress/20);
        bedroomsMaxValue = bedroomValue[progressIndex];
    }

    public float getBathroomsMinValueProgress()
    {
        int index = 0;
        while(index < bedroomValue.length && bathroomsMinValue != bathroomValue[index])
            index++;

        return index * 20;
    }

    public void setBathroomsMinValue(SeekBar leftBar)
    {
        float progress = leftBar.getProgress();
        int progressIndex = (int) (progress/20);
        bathroomsMinValue = bathroomValue[progressIndex];
    }

    public float getBathroomsMaxValueProgress()
    {
        int index = 0;
        while(index < bedroomValue.length && bathroomsMaxValue != bathroomValue[index])
            index++;

        return index * 20;
    }

    public void setBathroomsMaxValue(SeekBar rightBar)
    {
        float progress = rightBar.getProgress();
        int progressIndex = (int) (progress/20);
        bathroomsMinValue = bathroomValue[progressIndex];
    }

    public String getHousingCategory()
    {
        return housingCategory;
    }

    public void setHousingCategoryFilter(String housingCategory)
    {
        this.housingCategory = housingCategory;
    }

    public int getPetsAllowed()
    {
        return petsAllowed;
    }

    public void setPetsAllowedFilter(int petsAllowed)
    {
        this.petsAllowed = petsAllowed;
    }

    public int getSmokingAllowed()
    {
        return smokingAllowed;
    }

    public void setSmokingAllowedFilter(int smokingAllowed)
    {
        this.smokingAllowed = smokingAllowed;
    }

    public boolean hasParkingAvailable()
    {
        return hasParkingAvailable;
    }

    public void setParkingAvailableFilter(boolean hasParkingAvailable)
    {
        this.hasParkingAvailable = hasParkingAvailable;
    }

    public boolean hasPool()
    {
        return hasPool;
    }

    public void setPoolFilter(boolean hasPool)
    {
        this.hasPool = hasPool;
    }

    public boolean hasBackyard()
    {
        return hasBackyard;
    }

    public void setBackyardFilter(boolean hasBackyard)
    {
        this.hasBackyard = hasBackyard;
    }

    public boolean hasLaundry()
    {
        return hasLaundry;
    }

    public void setLaundryFilter(boolean hasLaundry)
    {
        this.hasLaundry = hasLaundry;
    }

    public boolean isHandicapAccessible()
    {
        return isHandicapAccessible;
    }

    public void setHandicapAccessible(boolean handicapAccessible)
    {
        isHandicapAccessible = handicapAccessible;
    }
}
