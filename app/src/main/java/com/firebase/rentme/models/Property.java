package com.firebase.rentme.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Property implements Parcelable
{

    public static final String PARCELABLE_PROPERTY = "com.firebase.rentme.PROPERTY";

    //Property Attributes
    private String documentID;

    private String housingCategory;
    private double price;
    private String photoURL;
    private String bio;
    private String address;

    private String city;
    private String zipCode;
    private String state;
    private int bedrooms;
    private double bathrooms;

    //Rules
    private boolean petsAllowed;
    private boolean smokingAllowed;

    //Amenities
    private boolean parkingAvailable;
    private boolean hasPool;
    private boolean hasBackyard;
    private boolean hasLaundry;
    private boolean isHandicapAccessible;

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
        documentID = parcel.readString();
        housingCategory = parcel.readString();
        price = parcel.readDouble();
        photoURL = parcel.readString();
        bio = parcel.readString();
        address = parcel.readString();
        city = parcel.readString();
        zipCode = parcel.readString();
        state = parcel.readString();
        ownerName = parcel.readString();
        ownerPhoneNum = parcel.readString();
        ownerEmail = parcel.readString();
        bedrooms = parcel.readInt();
        bathrooms = parcel.readDouble();
        petsAllowed = parcel.readBoolean();
        smokingAllowed = parcel.readBoolean();
        parkingAvailable = parcel.readBoolean();
        hasPool = parcel.readBoolean();
        hasBackyard = parcel.readBoolean();
        hasLaundry = parcel.readBoolean();
        isHandicapAccessible = parcel.readBoolean();
    }

    //Standard Constructor
    public Property(String documentReference, String housingCategory, double price, String photoURL, String bio, String address,
                    String city, String zipCode, String state, String ownerName, String ownerPhoneNum,
                    String ownerEmail, int bedrooms, double bathrooms, boolean petsAllowed, boolean smokingAllowed,
                    boolean parkingAvailable, boolean hasPool, boolean hasBackyard, boolean hasLaundry, boolean isHandicapAccessible)
    {
        this.documentID = documentReference;
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
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.petsAllowed = petsAllowed;
        this.smokingAllowed = smokingAllowed;
        this.parkingAvailable = parkingAvailable;
        this.hasPool = hasPool;
        this.hasBackyard = hasBackyard;
        this.hasLaundry = hasLaundry;
        this.isHandicapAccessible = isHandicapAccessible;
    }

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
        dest.writeString(documentID);
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
        dest.writeInt(bedrooms);
        dest.writeDouble(bathrooms);
        dest.writeBoolean(petsAllowed);
        dest.writeBoolean(smokingAllowed);
        dest.writeBoolean(parkingAvailable);
        dest.writeBoolean(hasPool);
        dest.writeBoolean(hasBackyard);
        dest.writeBoolean(hasLaundry);
        dest.writeBoolean(isHandicapAccessible);
    }

    public String generatePostalAddress()
    {
        return address + ", " + city + ", " + state;
    }

    //Return hashcode of object
    public int describeContents() { return hashCode(); }

    //Get Values
    public String getDocumentID() {return  this.documentID; }

    public String getHousingCategory() { return this.housingCategory; }

    public double getPrice() { return price; }

    public String getPhotoURL() { return this.photoURL; }

    public String getBio() { return this.bio; }

    public String getAddress() { return address; }

    public String getCity() { return this.city; }

    public String getZipCode() { return this.zipCode; }

    public String getState() { return this.state; }

    public String getOwnerName() { return ownerName; }

    public String getOwnerPhoneNum() { return ownerPhoneNum; }

    public String getOwnerEmail() { return ownerEmail; }

    public int getBedrooms() { return bedrooms; }

    public double getBathrooms() { return bathrooms; }

    public boolean isPetsAllowed() { return petsAllowed; }

    public boolean isSmokingAllowed() { return smokingAllowed; }

    public boolean isParkingAvailable() { return parkingAvailable; }

    public boolean isPoolAvailable() { return hasPool; }

    public boolean isBackyardAvailable() { return hasBackyard; }

    public boolean isLaundryAvailable() { return hasLaundry; }

    public boolean isHandicapAccessible() { return isHandicapAccessible; }

    //Set Values
    public void setDocumentID(String documentID) { this.documentID = documentID; }

    public void setHousingCategory(String housingCategory) { this.housingCategory = housingCategory; }

    public void setPrice(double price) { this.price = price; }

    public void setPhotoURL(String photoURL) { this.photoURL = photoURL; }

    public void setBio(String bio) { this.bio = bio; }

    public void setAddress(String address) { this.address = address; }

    public void setCity(String city) { this.city = city; }

    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public void setState(String state) { this.state = state; }

    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public void setOwnerPhoneNum(String ownerPhoneNum) { this.ownerPhoneNum = ownerPhoneNum; }

    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }

    public void setBedrooms(int bedrooms) { this.bedrooms = bedrooms; }

    public void setBathrooms(double bathrooms) { this.bathrooms = bathrooms; }

    public void setPetsAllowed(boolean petsAllowed) { this.petsAllowed = petsAllowed; }

    public void setSmokingAllowed(boolean smokingAllowed) { this.smokingAllowed = smokingAllowed; }

    public void setParkingAvailable(boolean parkingAvailable) { this.parkingAvailable = parkingAvailable; }

    public void setPoolAvailable(boolean hasPool) { this.hasPool = hasPool; }

    public void setBackyardAvailable(boolean hasBackyard) { this.hasBackyard = hasBackyard; }

    public void setLaundryAvailable(boolean hasLaundry) { this.hasLaundry = hasLaundry; }

    public void setHandicapAccessible(boolean handicapAccessible) { isHandicapAccessible = handicapAccessible; }
}
