package com.example.petapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.petapp.models.Booking;

import java.util.ArrayList;
import java.util.List;

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
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_ADDITIONAL_NOTES = "additional_notes";
    private static final String COLUMN_PAYMENT_METHOD = "payment_method";
    private static final String COLUMN_PAYMENT_STATUS = "payment_status";

    // Update create table SQL
    private static final String CREATE_BOOKINGS_TABLE =
            "CREATE TABLE " + TABLE_BOOKINGS + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_SERVICE_ID + " INTEGER, " +
                    COLUMN_BOOKING_DATE + " TEXT, " +
                    COLUMN_PRICE + " REAL, " +
                    COLUMN_STATUS + " TEXT DEFAULT 'PENDING', " +
                    COLUMN_ANIMAL_TYPE + " TEXT, " +
                    COLUMN_ADDRESS + " TEXT, " +
                    COLUMN_ADDITIONAL_NOTES + " TEXT, " +
                    COLUMN_PAYMENT_METHOD + " TEXT, " +
                    COLUMN_PAYMENT_STATUS + " TEXT)";

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

    public long insertBooking(int serviceId, String bookingDate, double price, String animalType, String additionalNotes, String address, String paymentMethod, String paymentStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_SERVICE_ID, serviceId);
        values.put(COLUMN_BOOKING_DATE, bookingDate);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_STATUS, "PENDING");
        values.put(COLUMN_ANIMAL_TYPE, animalType);
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_ADDITIONAL_NOTES, additionalNotes);
        values.put(COLUMN_PAYMENT_METHOD, paymentMethod);
        values.put(COLUMN_PAYMENT_STATUS, paymentStatus);

        return db.insert(TABLE_BOOKINGS, null, values);
    }

    public void deleteBooking(int bookingId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKINGS, COLUMN_ID + " = ?", new String[]{String.valueOf(bookingId)});
    }

    public void updateBookingStatus(int bookingId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, status);
        db.update(TABLE_BOOKINGS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(bookingId)});
    }

    public void updatePaymentStatus(int bookingId, String paymentStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PAYMENT_STATUS, paymentStatus);
        db.update(TABLE_BOOKINGS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(bookingId)});
    }

    public void updatePaymentMethod(int bookingId, String paymentMethod) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PAYMENT_METHOD, paymentMethod);
        db.update(TABLE_BOOKINGS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(bookingId)});
    }

    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKINGS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Booking booking = new Booking(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SERVICE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOKING_DATE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANIMAL_TYPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDITIONAL_NOTES)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_METHOD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAYMENT_STATUS))
                );
                bookings.add(booking);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return bookings;
    }
}