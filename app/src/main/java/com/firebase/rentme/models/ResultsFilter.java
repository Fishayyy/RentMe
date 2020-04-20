package com.firebase.rentme.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.jaygoo.widget.SeekBar;

import java.util.ArrayList;

public class ResultsFilter implements Parcelable
{
    public static final String PARCELABLE_FILTER = "com.firebase.rentme.FILTER";

    private static final int[] BEDROOM_VALUES = {0, 1, 2, 3, 4, 5};
    private static final double[] BATHROOM_VALUES = {1.0, 1.5, 2.0, 2.5, 3.0, 4.0};
    private static final int ANY = 0;
    private static final int YES = 1;
    private static final int NO = 2;

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

    public ResultsFilter()
    {
        setDefaultFilter();
    }

    //Methods for Parcelable
    //used when un-parceling our parcel (creating the object)
    public static final Parcelable.Creator<ResultsFilter> CREATOR = new Parcelable.Creator<ResultsFilter>()
    {
        @Override
        public ResultsFilter createFromParcel(Parcel parcel)
        {
            return new ResultsFilter(parcel);
        }

        @Override
        public ResultsFilter[] newArray(int size)
        {
            return new ResultsFilter[0];
        }
    };

    //write object values to parcel for storage
    public void writeToParcel(Parcel dest, int flags)
    {
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
        minPrice = 0.0;
        maxPrice = Double.MAX_VALUE;
        bedroomsMinValue = BEDROOM_VALUES[0];
        bedroomsMaxValue = BEDROOM_VALUES[BEDROOM_VALUES.length - 1];
        bathroomsMinValue = BATHROOM_VALUES[0];
        bathroomsMaxValue = BATHROOM_VALUES[BEDROOM_VALUES.length - 1];
        housingCategory = "Any";
        petsAllowed = ANY;
        smokingAllowed = ANY;
        hasParkingAvailable = false;
        hasPool = false;
        hasBackyard = false;
        hasLaundry = false;
        isHandicapAccessible = false;
    }

    public ArrayList<Property> getFilteredResults(ArrayList<Property> unfilteredProperties)
    {
        ArrayList<Property> results = new ArrayList<>(unfilteredProperties);

        if (minPrice != 0.0 || maxPrice != Double.MAX_VALUE && results.size() != 0)
        {
            filterByPrice(results);
        }
        if (bedroomsMinValue != BEDROOM_VALUES[0] || bedroomsMaxValue != BEDROOM_VALUES[BEDROOM_VALUES.length - 1] && results.size() != 0)
        {
            filterByBedrooms(results);
        }
        if (bathroomsMinValue != BATHROOM_VALUES[0] || bathroomsMaxValue != BATHROOM_VALUES[BATHROOM_VALUES.length - 1] && results.size() != 0)
        {
            filterByBathrooms(results);
        }
        if (!housingCategory.equals("Any") && results.size() != 0)
        {
            filterByHousingCategory(results);
        }
        if (petsAllowed != ANY && results.size() != 0)
        {
            filterByPetsAllowed(results);
        }
        if (smokingAllowed != ANY && results.size() != 0)
        {
            filterBySmokingAllowed(results);
        }
        if (hasParkingAvailable && results.size() != 0)
        {
            filterByParkingAvailable(results);
        }
        if (hasPool && results.size() != 0)
        {
            filterByPool(results);
        }
        if (hasBackyard && results.size() != 0)
        {
            filterByBackyard(results);
        }
        if (hasLaundry && results.size() != 0)
        {
            filterByLaundry(results);
        }
        if (isHandicapAccessible && results.size() != 0)
        {
            filterByHandicapAccessible(results);
        }

        return results;
    }

    private void filterByPrice(ArrayList<Property> results)
    {
        if (results.size() != 0)
        {
            ArrayList<Property> temp = new ArrayList<>(results);

            for (Property property : temp)
            {
                double price = property.getPrice();

                if (price < minPrice || price > maxPrice)
                {
                    results.remove(property);
                }
            }
        }
    }

    private void filterByBedrooms(ArrayList<Property> results)
    {
        ArrayList<Property> temp = new ArrayList<>(results);

        for (Property property : temp)
        {
            int bedrooms = property.getBedrooms();

            if (bedrooms < bedroomsMinValue || (bedrooms > bedroomsMaxValue && bedroomsMaxValue != BEDROOM_VALUES[BEDROOM_VALUES.length - 1]))
            {
                results.remove(property);
            }
        }
    }

    private void filterByBathrooms(ArrayList<Property> results)
    {
        ArrayList<Property> temp = new ArrayList<>(results);

        for (Property property : temp)
        {
            double bathrooms = property.getBathrooms();

            if (bathrooms < bathroomsMinValue || (bathrooms > bathroomsMaxValue && bathroomsMaxValue != BATHROOM_VALUES[BATHROOM_VALUES.length - 1]))
            {
                results.remove(property);
            }
        }
    }

    private void filterByHousingCategory(ArrayList<Property> results)
    {
        ArrayList<Property> temp = new ArrayList<>(results);

        for (Property property : temp)
        {
            String type = property.getHousingCategory();

            if (!type.equals(housingCategory))
            {
                results.remove(property);
            }
        }
    }

    private void filterByPetsAllowed(ArrayList<Property> results)
    {
        ArrayList<Property> temp = new ArrayList<>(results);

        for (Property property : temp)
        {
            if ((petsAllowed == YES && !property.isPetsAllowed()) || (petsAllowed == NO && property.isPetsAllowed()))
            {
                results.remove(property);
            }
        }
    }

    private void filterBySmokingAllowed(ArrayList<Property> results)
    {
        ArrayList<Property> temp = new ArrayList<>(results);

        for (Property property : temp)
        {
            if ((smokingAllowed == YES && !property.isSmokingAllowed()) || (smokingAllowed == NO && property.isSmokingAllowed()))
            {
                results.remove(property);
            }
        }
    }

    private void filterByParkingAvailable(ArrayList<Property> results)
    {
        ArrayList<Property> temp = new ArrayList<>(results);

        for (Property property : temp)
        {
            if (!property.isParkingAvailable())
            {
                results.remove(property);
            }
        }
    }

    private void filterByPool(ArrayList<Property> results)
    {
        ArrayList<Property> temp = new ArrayList<>(results);

        for (Property property : temp)
        {
            if (!property.isPoolAvailable())
            {
                results.remove(property);
            }
        }
    }

    private void filterByBackyard(ArrayList<Property> results)
    {
        ArrayList<Property> temp = new ArrayList<>(results);

        for (Property property : temp)
        {
            if (!property.isBackyardAvailable())
            {
                results.remove(property);
            }
        }
    }

    private void filterByLaundry(ArrayList<Property> results)
    {
        ArrayList<Property> temp = new ArrayList<>(results);

        for (Property property : temp)
        {
            if (!property.isLaundryAvailable())
            {
                results.remove(property);
            }
        }
    }

    private void filterByHandicapAccessible(ArrayList<Property> results)
    {
        ArrayList<Property> temp = new ArrayList<>(results);

        for (Property property : temp)
        {
            if (!property.isHandicapAccessible())
            {
                results.remove(property);
            }
        }
    }

    //Get Values
    public double getMinPrice()
    {
        return minPrice;
    }

    public double getMaxPrice() { return maxPrice; }

    public float getBedroomsMinValueProgress() { return convertIndexToProgress(findBedroomIndex(bedroomsMinValue), BEDROOM_VALUES.length);}

    public float getBedroomsMaxValueProgress() { return convertIndexToProgress(findBedroomIndex(bedroomsMaxValue), BEDROOM_VALUES.length);}

    public float getBathroomsMinValueProgress() { return convertIndexToProgress(findBathroomIndex(bathroomsMinValue), BATHROOM_VALUES.length);}

    public float getBathroomsMaxValueProgress() { return convertIndexToProgress(findBathroomIndex(bathroomsMaxValue), BATHROOM_VALUES.length);}

    private float convertIndexToProgress(int index, int arraySize) { return index * (100f / (arraySize - 1)); }

    private int findBedroomIndex(int value)
    {
        int index = 0;
        while (index < BEDROOM_VALUES.length && value != BEDROOM_VALUES[index])
            index++;

        return index;
    }

    private int findBathroomIndex(double value)
    {
        int index = 0;
        while (index < BATHROOM_VALUES.length && value != BATHROOM_VALUES[index])
            index++;

        return index;
    }

    public String getHousingCategory()
    {
        return housingCategory;
    }

    public int getPetsAllowed()
    {
        return petsAllowed;
    }

    public int getSmokingAllowed()
    {
        return smokingAllowed;
    }

    public boolean hasParkingAvailable()
    {
        return hasParkingAvailable;
    }

    public boolean hasPool()
    {
        return hasPool;
    }

    public boolean hasBackyard()
    {
        return hasBackyard;
    }

    public boolean hasLaundry()
    {
        return hasLaundry;
    }

    public boolean isHandicapAccessible()
    {
        return isHandicapAccessible;
    }


    //Set Values
    public void setMinPrice(double minPrice) { this.minPrice = minPrice; }

    public void setMaxPrice(double maxPrice) { this.maxPrice = maxPrice; }

    public void setBedroomsMinValue(SeekBar leftBar) { bedroomsMinValue = BEDROOM_VALUES[convertProgressToIndex(leftBar.getProgress(), BEDROOM_VALUES.length)]; }

    public void setBedroomsMaxValue(SeekBar rightBar) { bedroomsMaxValue = BEDROOM_VALUES[convertProgressToIndex(rightBar.getProgress(), BEDROOM_VALUES.length)]; }

    public void setBathroomsMinValue(SeekBar leftBar) { bathroomsMinValue = BATHROOM_VALUES[convertProgressToIndex(leftBar.getProgress(), BATHROOM_VALUES.length)]; }

    public void setBathroomsMaxValue(SeekBar rightBar) { bathroomsMaxValue = BATHROOM_VALUES[convertProgressToIndex(rightBar.getProgress(), BATHROOM_VALUES.length)]; }

    private int convertProgressToIndex(float progress, int arraySize) { return (int) (progress / (100f / (arraySize - 1))); }

    public void setHousingCategoryFilter(String housingCategory) { this.housingCategory = housingCategory; }

    public void setPetsAllowedFilter(int petsAllowed) { this.petsAllowed = petsAllowed; }

    public void setSmokingAllowedFilter(int smokingAllowed) { this.smokingAllowed = smokingAllowed; }

    public void setParkingAvailableFilter(boolean hasParkingAvailable) { this.hasParkingAvailable = hasParkingAvailable; }

    public void setPoolFilter(boolean hasPool) { this.hasPool = hasPool; }

    public void setBackyardFilter(boolean hasBackyard) { this.hasBackyard = hasBackyard; }

    public void setLaundryFilter(boolean hasLaundry) { this.hasLaundry = hasLaundry; }

    public void setHandicapAccessible(boolean handicapAccessible) { isHandicapAccessible = handicapAccessible; }

}
