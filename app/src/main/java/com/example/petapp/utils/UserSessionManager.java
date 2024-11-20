
package com.example.petapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.petapp.models.User;
import com.google.gson.Gson;

public class UserSessionManager {
    private static final String PREF_NAME = "UserSessionPref";
    private static final String KEY_CURRENT_USER = "currentUser";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private Gson gson;

    public UserSessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    // Save current user
    public void saveCurrentUser(User user) {
        String userJson = gson.toJson(user);
        editor.putString(KEY_CURRENT_USER, userJson);
        editor.apply();
    }

    // Get current user
    public User getCurrentUser() {
        String userJson = sharedPreferences.getString(KEY_CURRENT_USER, null);
        if (userJson != null) {
            return gson.fromJson(userJson, User.class);
        }
        return null;
    }

    // Check if user is logged in
    public boolean isLoggedIn() {
        return getCurrentUser() != null;
    }

    // Clear user session (logout)
    public void logout() {
        editor.remove(KEY_CURRENT_USER);
        editor.apply();
    }
}