package com.example.petapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BookingDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "PetServiceBookings.db";
    private static final int DATABASE_VERSION = 1;

    // Table and column names
    private static final String TABLE_BOOKINGS = "bookings";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_SERVICE_ID = "service_id";
    private static final String COLUMN_BOOKING_DATE = "booking_date";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_ANIMAL_TYPE = "animal_type";

    // Update create table SQL
    private static final String CREATE_BOOKINGS_TABLE =
            "CREATE TABLE " + TABLE_BOOKINGS + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_SERVICE_ID + " INTEGER, " +
                    COLUMN_BOOKING_DATE + " TEXT, " +
                    COLUMN_PRICE + " REAL, " +
                    COLUMN_STATUS + " TEXT DEFAULT 'PENDING', " +
                    COLUMN_ANIMAL_TYPE + " TEXT)";

    public BookingDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOKINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        onCreate(db);
    }

    public long insertBooking(int serviceId, String bookingDate, double price, String animalType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_SERVICE_ID, serviceId);
        values.put(COLUMN_BOOKING_DATE, bookingDate);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_STATUS, "PENDING");
        values.put(COLUMN_ANIMAL_TYPE, animalType);

        return db.insert(TABLE_BOOKINGS, null, values);
    }
}