package com.example.petapp.controllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.petapp.R;
import com.example.petapp.database.UserDatabaseHelper;
import com.example.petapp.models.User;
import com.example.petapp.utils.UserSessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment {
    private UserDatabaseHelper userDatabaseHelper;
    private UserSessionManager userSessionManager;
    private TextInputEditText emailEditText, passwordEditText;
    private TextInputLayout emailInputLayout, passwordInputLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.log_in, container, false);

        // Initialize Database Helper
        userDatabaseHelper = new UserDatabaseHelper(requireContext());

        // Initialize User Session Manager
        userSessionManager = new UserSessionManager(requireContext());

        // Initialize Views
        initializeViews(view);

        // Button Click Listeners
        setupButtonListeners(view);

        return view;
    }

    private void initializeViews(View view) {
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);

        emailInputLayout = view.findViewById(R.id.emailTextInputLayout);
        passwordInputLayout = view.findViewById(R.id.passwordTextInputLayout);
    }

    private void setupButtonListeners(View view) {
        MaterialButton loginButton = view.findViewById(R.id.loginButton);
        MaterialButton signUpButton = view.findViewById(R.id.signUpButton);
        MaterialButton backButton = view.findViewById(R.id.backButton);

        loginButton.setOnClickListener(v -> {
            if (validateInputs()) {
                performLogin(view);
            }
        });

        signUpButton.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signUpFragment)
        );

        backButton.setOnClickListener(v ->
                Navigation.findNavController(view).popBackStack()
        );
    }

    private boolean validateInputs() {
        boolean isValid = true;

        // Email Validation
        String email = emailEditText.getText().toString().trim();
        if (email.isEmpty()) {
            emailInputLayout.setError("Email cannot be empty");
            isValid = false;
        } else {
            emailInputLayout.setError(null);
        }

        // Password Validation
        String password = passwordEditText.getText().toString();
        if (password.isEmpty()) {
            passwordInputLayout.setError("Password cannot be empty");
            isValid = false;
        } else {
            passwordInputLayout.setError(null);
        }

        return isValid;
    }

    private void performLogin(View view) {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();

        // Validate user credentials
        if (userDatabaseHelper.validateUser(email, password)) {
            // Retrieve user details
            User user = userDatabaseHelper.getUserByEmail(email);

            // Save current user in session
            userSessionManager.saveCurrentUser(user);

            // Navigate to home fragment
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment);

            Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Invalid Email or Password", Toast.LENGTH_SHORT).show();
        }
    }
}