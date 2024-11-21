package com.example.petapp.controllers;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.example.petapp.R;
import com.example.petapp.database.UserDatabaseHelper;
import com.example.petapp.models.User;
import com.example.petapp.utils.UserSessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class SignUpFragment extends Fragment {
    private UserDatabaseHelper userDatabaseHelper;
    private TextInputEditText nameEditText, emailEditText, contactNumberEditText, addressEditText, passwordEditText, confirmPasswordEditText;
    private TextInputLayout nameInputLayout, emailInputLayout, contactNumberInputLayout, addressInputLayout, passwordInputLayout, confirmPasswordInputLayout;
    private ImageView iconImageView;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Bitmap selectedImageBitmap;
    private UserSessionManager userSessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up_fragment, container, false);

        // Initialize Database Helper
        userDatabaseHelper = new UserDatabaseHelper(requireContext());

        // Initialize User Session Manager
        userSessionManager = new UserSessionManager(requireContext());

        // Initialize Views
        initializeViews(view);

        // Initialize Image Picker Launcher
        initializeImagePickerLauncher();

        // Button Click Listeners
        setupButtonListeners(view);

        return view;
    }

    private void initializeViews(View view) {
        nameEditText = view.findViewById(R.id.nameEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        contactNumberEditText = view.findViewById(R.id.contactNumberEditText);
        addressEditText = view.findViewById(R.id.addressEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        confirmPasswordEditText = view.findViewById(R.id.confirmPasswordEditText);

        iconImageView = view.findViewById(R.id.profileImageView);

        nameInputLayout = view.findViewById(R.id.nameTextInputLayout);
        emailInputLayout = view.findViewById(R.id.emailTextInputLayout);
        contactNumberInputLayout = view.findViewById(R.id.contactNumberTextInputLayout);
        addressInputLayout = view.findViewById(R.id.addressTextInputLayout);
        passwordInputLayout = view.findViewById(R.id.passwordTextInputLayout);
        confirmPasswordInputLayout = view.findViewById(R.id.confirmPasswordTextInputLayout);

    }


    private void initializeImagePickerLauncher() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                            iconImageView.setImageBitmap(selectedImageBitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    private void setupButtonListeners(View view) {
        MaterialButton loginButton = view.findViewById(R.id.loginButton);
        MaterialButton signUpButton = view.findViewById(R.id.signUpButton);
        MaterialButton backButton = view.findViewById(R.id.backButton);
        MaterialButton uploadImageButton = view.findViewById(R.id.uploadImageButton);

        loginButton.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_loginFragment)
        );

        signUpButton.setOnClickListener(v -> {
            if (validateInputs()) {
                registerUser();
            }
        });

        backButton.setOnClickListener(v ->
            Navigation.findNavController(view).popBackStack()
        );

        uploadImageButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    requestPermissions(new String[]{
                                Manifest.permission.READ_MEDIA_IMAGES},
                            REQUEST_CODE_STORAGE_PERMISSION);
                }
                else {
                    requestPermissions(new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_STORAGE_PERMISSION);
                }
            } else {
                openImagePicker();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInputs() {
        boolean isValid = true;

        // Name Validation
        String name = nameEditText.getText().toString().trim();
        if (name.isEmpty()) {
            nameInputLayout.setErrorEnabled(false);
            nameInputLayout.setError("Name cannot be empty");
            isValid = false;
        } else {
            nameInputLayout.setError(null);
            nameInputLayout.setErrorEnabled(false);
        }

        // Email Validation
        String email = emailEditText.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            emailInputLayout.setErrorEnabled(false);
            emailInputLayout.setError("Invalid email address");
            isValid = false;
        } else {
            emailInputLayout.setError(null);
            emailInputLayout.setErrorEnabled(false);
        }

        // Contact Number Validation
        String contactNumber = contactNumberEditText.getText().toString().trim();
        if (contactNumber.isEmpty()) {
            contactNumberInputLayout.setErrorEnabled(false);
            contactNumberInputLayout.setError("Contact number cannot be empty");
            isValid = false;
        } else {
            contactNumberInputLayout.setError(null);
            contactNumberInputLayout.setErrorEnabled(false);
        }

        // Address Validation
        String address = addressEditText.getText().toString().trim();
        if (address.isEmpty()) {
            addressInputLayout.setErrorEnabled(false);
            addressInputLayout.setError("Address cannot be empty");
            isValid = false;
        } else {
            addressInputLayout.setError(null);
            addressInputLayout.setErrorEnabled(false);
        }

        // Password Validation
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        if (password.isEmpty() || password.length() < 6) {
            passwordInputLayout.setErrorEnabled(false);
            passwordInputLayout.setError("Password must be at least 6 characters");
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordInputLayout.setError("Passwords do not match");
            confirmPasswordInputLayout.setErrorEnabled(false);
            isValid = false;
        } else {
            passwordInputLayout.setErrorEnabled(false);
            passwordInputLayout.setError(null);
            confirmPasswordInputLayout.setErrorEnabled(false);
            confirmPasswordInputLayout.setError(null);
        }

        return isValid;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    private void registerUser() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String contactNumber = contactNumberEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();

        // Check if user already exists
        if (userDatabaseHelper.getUserByEmail(email) != null) {
            Toast.makeText(requireContext(), "Email already registered", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert selected image to bytes
        byte[] iconBytes = null;
        if (selectedImageBitmap != null) {
            Bitmap resizedBitmap = resizeAndCropImage(selectedImageBitmap, 480, 480);
            iconBytes = UserDatabaseHelper.getBytesFromBitmap(resizedBitmap);
        };

        User newUser = new User(name, email, contactNumber, address, password, iconBytes);
        long result = userDatabaseHelper.insertUser(newUser);

        if (result != -1) {
            Toast.makeText(requireContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
            // Save current user in session
            userSessionManager.saveCurrentUser(newUser);

            // Navigate to HomeFragment
            Navigation.findNavController(requireView()).navigate(R.id.action_signUpFragment_to_homeFragment);
        } else {
            Toast.makeText(requireContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap resizeAndCropImage(Bitmap originalBitmap, int width, int height) {
        int originalWidth = originalBitmap.getWidth();
        int originalHeight = originalBitmap.getHeight();
        int cropSize = Math.min(originalWidth, originalHeight);

        // Crop the image to the center
        Bitmap croppedBitmap = Bitmap.createBitmap(
                originalBitmap,
                (originalWidth - cropSize) / 2,
                (originalHeight - cropSize) / 2,
                cropSize,
                cropSize
        );

        // Resize the cropped image
        return Bitmap.createScaledBitmap(croppedBitmap, width, height, true);
    }
}