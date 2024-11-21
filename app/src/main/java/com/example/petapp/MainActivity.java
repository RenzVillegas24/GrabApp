package com.example.petapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

import com.example.petapp.utils.UserSessionManager;

public class MainActivity extends AppCompatActivity {
    private NavController navController;
    private UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null)
            navController = ((NavHostFragment)navHostFragment).getNavController();
        else
            navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        userSessionManager = new UserSessionManager(this);
        if (userSessionManager.isLoggedIn()) {
            // User is already logged in, navigate to HomeFragment and clear back stack
            navController.navigate(R.id.action_welcomeFragment_to_homeFragment);
        }
    }
}