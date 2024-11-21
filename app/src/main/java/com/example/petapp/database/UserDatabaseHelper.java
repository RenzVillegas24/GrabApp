package com.example.petapp.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.petapp.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "PetAppUsers.db";
    private static final int DATABASE_VERSION = 1;

    // Table and column names
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_CONTACT_NUMBER = "contact_number";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ICON = "icon";

    // Create table SQL
    private static final String CREATE_USERS_TABLE =
            "CREATE TABLE " + TABLE_USERS + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_EMAIL + " TEXT UNIQUE, " +
                    COLUMN_CONTACT_NUMBER + " TEXT, " +
                    COLUMN_ADDRESS + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_ICON + " BLOB)";


    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Create (Insert) User
    public long insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_CONTACT_NUMBER, user.getContactNumber());
        values.put(COLUMN_ADDRESS, user.getAddress());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_ICON, user.getUserIcon());

        return db.insert(TABLE_USERS, null, values);
    }

    // Read User by ID
    public User getUserById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_EMAIL, COLUMN_CONTACT_NUMBER, COLUMN_PASSWORD, COLUMN_ICON},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTACT_NUMBER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                    cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_ICON))
            );
            cursor.close();
            return user;
        }

        return null;
    }

    // Helper method to convert Bitmap to byte array
    public static byte[] getBytesFromBitmap(android.graphics.Bitmap bitmap) {
        if (bitmap == null) return null;

        java.io.ByteArrayOutputStream stream = new java.io.ByteArrayOutputStream();
        bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    // Helper method to convert byte array to Bitmap
    public static android.graphics.Bitmap getBitmapFromBytes(byte[] image) {
        if (image == null) return null;

        return android.graphics.BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    // Read User by Email
    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_EMAIL, COLUMN_CONTACT_NUMBER, COLUMN_ADDRESS, COLUMN_PASSWORD, COLUMN_ICON},
                COLUMN_EMAIL + "=?",
                new String[]{email},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTACT_NUMBER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                    cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_ICON))
            );
            cursor.close();
            return user;
        }

        return null;
    }

    // Read All Users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTACT_NUMBER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                    cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_ICON))
                );
                users.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return users;
    }

    // Update User
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_CONTACT_NUMBER, user.getContactNumber());
        values.put(COLUMN_ADDRESS, user.getAddress());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_ICON, user.getUserIcon());

        return db.update(TABLE_USERS, values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
    }

    // Delete User
    public void deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(userId)});
    }

    // Check User Credentials
    public boolean validateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID},
                COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{email, password},
                null, null, null);

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }
}