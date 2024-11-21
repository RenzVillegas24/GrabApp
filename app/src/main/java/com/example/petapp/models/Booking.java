package com.example.petapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Booking implements Parcelable {
    private int id;
    private int serviceId;
    private String bookingDate;
    private double price;
    private String status;
    private String animalType;
    private String address;
    private String additionalNotes;
    private String paymentMethod;
    private String paymentStatus;

    public Booking(int id, int serviceId, String bookingDate, double price, String status, String animalType, String address, String additionalNotes, String paymentMethod, String paymentStatus) {
        this.id = id;
        this.serviceId = serviceId;
        this.bookingDate = bookingDate;
        this.price = price;
        this.status = status;
        this.animalType = animalType;
        this.address = address;
        this.additionalNotes = additionalNotes;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
    }

    // Parcelable implementation
    protected Booking(Parcel in) {
        id = in.readInt();
        serviceId = in.readInt();
        bookingDate = in.readString();
        price = in.readDouble();
        status = in.readString();
        animalType = in.readString();
        address = in.readString();
        additionalNotes = in.readString();
        paymentMethod = in.readString();
        paymentStatus = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(serviceId);
        dest.writeString(bookingDate);
        dest.writeDouble(price);
        dest.writeString(status);
        dest.writeString(animalType);
        dest.writeString(address);
        dest.writeString(additionalNotes);
        dest.writeString(paymentMethod);
        dest.writeString(paymentStatus);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Booking> CREATOR = new Creator<Booking>() {
        @Override
        public Booking createFromParcel(Parcel in) {
            return new Booking(in);
        }

        @Override
        public Booking[] newArray(int size) {
            return new Booking[size];
        }
    };

    // Getters
    public int getId() { return id; }
    public int getServiceId() { return serviceId; }
    public String getBookingDate() { return bookingDate; }
    public double getPrice() { return price; }
    public String getStatus() { return status; }
    public String getAnimalType() { return animalType; }
    public String getAddress() { return address; }
    public String getAdditionalNotes() { return additionalNotes; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getPaymentStatus() { return paymentStatus; }
}