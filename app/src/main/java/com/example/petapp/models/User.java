package com.example.petapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private int id;
    private String name;
    private String email;
    private String password;
    private byte[] userIcon;

    public User(String name, String email, String password, byte[] userIcon) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userIcon = userIcon;
    }

    public User(int id, String name, String email, String password, byte[] userIcon) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userIcon = userIcon;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public byte[] getUserIcon() { return userIcon; }
    public void setUserIcon(byte[] userIcon) { this.userIcon = userIcon; }

    // Parcelable implementation
    protected User(Parcel in) {
        id = in.readInt();
        name = in.readString();
        email = in.readString();
        password = in.readString();

        // Read byte array
        int iconLength = in.readInt();
        if (iconLength > 0) {
            userIcon = new byte[iconLength];
            in.readByteArray(userIcon);
        }
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(password);

        // Write byte array
        if (userIcon != null) {
            dest.writeInt(userIcon.length);
            dest.writeByteArray(userIcon);
        } else {
            dest.writeInt(0);
        }
    }
}