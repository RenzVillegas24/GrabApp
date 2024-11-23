package com.example.petapp.controllers;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.petapp.R;
import com.example.petapp.database.BookingDatabaseHelper;
import com.example.petapp.modals.PetAppointmentPicker;
import com.example.petapp.models.PetService;
import com.example.petapp.models.User;
import com.example.petapp.utils.UserSessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PetServiceDetailsFragment extends Fragment {
    private PetService petService;

    public PetServiceDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve PetService from navigation arguments
        if (getArguments() != null) {
            petService = getArguments().getParcelable("petService");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.pet_service_details_fragment, container, false);

        // Find views
        TextView serviceTitleTextView = view.findViewById(R.id.serviceTitleTextView);
        TextView servicePriceTextView = view.findViewById(R.id.servicePriceTextView);
        TextView serviceLongDescriptionTextView = view.findViewById(R.id.serviceLongDescriptionTextView);
        ImageView iconView = view.findViewById(R.id.iconView);
        MaterialButton addToCartButton = view.findViewById(R.id.addToCartButton);
        MaterialButton backButton = view.findViewById(R.id.backButton);

        // Set data from PetService
        if (petService != null) {
            serviceTitleTextView.setText(petService.getTitle());
            servicePriceTextView.setText(String.format(Locale.getDefault(),"₱%.2f", petService.getPrice()));
            serviceLongDescriptionTextView.setText(petService.getLongDescription());
            iconView.setImageResource(petService.getIconResId());
        }

        // Back button click listener
        backButton.setOnClickListener(v -> {
            Navigation.findNavController(view).navigateUp();
        });

        // Add to cart button click listener
        addToCartButton.setOnClickListener(v -> {
            // Initialize BookingDatabaseHelper
            BookingDatabaseHelper bookingDatabaseHelper = new BookingDatabaseHelper(requireContext());
            UserSessionManager userSessionManager = new UserSessionManager(requireContext());

            // Get the current user
            User user = userSessionManager.getCurrentUser();

            // Show the PetAppointmentPicker dialog
            PetAppointmentPicker.PetAppointmentPickerDialog dialog = new PetAppointmentPicker.PetAppointmentPickerDialog();
            dialog.setCallback(new PetAppointmentPicker.AppointmentCallback() {
                @Override
                public void onAppointmentSelected(Long timestamp, String petType, String additionalNotes, String address) {
                    // Format the selected date and time
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());
                    String formattedDateTime = sdf.format(new Date(timestamp));

                    // Insert the booking into the database
                    long result = bookingDatabaseHelper.insertBooking(
                            petService.getId(),
                            formattedDateTime,
                            petService.getPrice(),
                            petType,
                            additionalNotes,
                            user.getAddress(),
                            null,
                            null
                    );

                    if (result != -1) {
                        // Booking added successfully
                        new MaterialAlertDialogBuilder(requireContext())
                                .setTitle("Booking Confirmed")
                                .setMessage("Your booking has been added successfully.")
                                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                .show();
                        
                    } else {
                        // Booking failed
                        new MaterialAlertDialogBuilder(requireContext())
                                .setTitle("Booking Failed")
                                .setMessage("There was an error adding your booking. Please try again.")
                                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                .show();
                    }
                }

                @Override
                public void onDismiss() {
                    // Optional: handle dismissal
                }
            });
            dialog.show(getParentFragmentManager(), "PET_APPOINTMENT_PICKER");
        });

        return view;
    }

    // Static method to create fragment with arguments
    public static PetServiceDetailsFragment newInstance(PetService petService) {
        PetServiceDetailsFragment fragment = new PetServiceDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable("petService", petService);
        fragment.setArguments(args);
        return fragment;
    }
}