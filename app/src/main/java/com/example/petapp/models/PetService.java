package com.example.petapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class PetService implements Parcelable {
    private int id;
    private String title;
    private String description;
    private String longDescription;
    private double price;
    private int iconResId;

    public PetService(
            int id,
            String title,
            String description,
            String longDescription,
            double price,
            int iconResId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.longDescription = longDescription;
        this.price = price;
        this.iconResId = iconResId;
    }

    // Parcelable constructor
    protected PetService(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        longDescription = in.readString();
        price = in.readDouble();
        iconResId = in.readInt();
    }

    // Parcelable Creator
    public static final Creator<PetService> CREATOR = new Creator<PetService>() {
        @Override
        public PetService createFromParcel(Parcel in) {
            return new PetService(in);
        }

        @Override
        public PetService[] newArray(int size) {
            return new PetService[size];
        }
    };

    // Parcelable method implementations
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(longDescription);
        dest.writeDouble(price);
        dest.writeInt(iconResId);
    }

    // Existing getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLongDescription() { return longDescription; }
    public double getPrice() { return price; }
    public int getIconResId() { return iconResId; }

}