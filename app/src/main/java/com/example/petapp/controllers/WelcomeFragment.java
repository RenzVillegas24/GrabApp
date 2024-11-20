package com.example.petapp.controllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.petapp.R;
import com.example.petapp.utils.UserSessionManager;
import com.google.android.material.button.MaterialButton;

public class WelcomeFragment extends Fragment {

    public WelcomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.welcome, container, false);

        MaterialButton loginButton = view.findViewById(R.id.loginButton);
        MaterialButton signUpButton = view.findViewById(R.id.signUpButton);

        loginButton.setOnClickListener(v -> {
            // Navigate to login fragment
            Navigation.findNavController(getView()).navigate(R.id.action_welcomeFragment_to_loginFragment);
        });

        signUpButton.setOnClickListener(v -> {
            // Navigate to sign up fragment
            Navigation.findNavController(getView()).navigate(R.id.action_welcomeFragment_to_signUpFragment);
        });

        return view;
    }
}