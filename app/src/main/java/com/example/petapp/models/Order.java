package com.example.petapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Order implements Parcelable {
    private int id;
    private String serviceName;
    private String status;
    private String date;

    public Order(int id, String serviceName, String status, String date) {
        this.id = id;
        this.serviceName = serviceName;
        this.status = status;
        this.date = date;
    }

    // Parcelable constructor
    protected Order(Parcel in) {
        id = in.readInt();
        serviceName = in.readString();
        status = in.readString();
        date = in.readString();
    }

    // Parcelable Creator
    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
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
        dest.writeString(serviceName);
        dest.writeString(status);
        dest.writeString(date);
    }

    // Existing getters
    public int getId() { return id; }
    public String getServiceName() { return serviceName; }
    public String getStatus() { return status; }
    public String getDate() { return date; }
}