package com.example.petapp.controllers;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petapp.MainActivity;
import com.example.petapp.R;
import com.example.petapp.adapters.PetServicesAdapter;
import com.example.petapp.database.BookingDatabaseHelper;
import com.example.petapp.datas.ServicesList;
import com.example.petapp.modals.PetAppointmentPicker;
import com.example.petapp.models.PetService;
import com.example.petapp.models.User;
import com.example.petapp.utils.UserSessionManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ServicesFragment extends Fragment {
    private RecyclerView servicesRecyclerView;
    private PetServicesAdapter servicesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.services_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        servicesRecyclerView = view.findViewById(R.id.servicesRecyclerView);
        servicesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<PetService> services = new ServicesList().getServices();

        servicesAdapter = new PetServicesAdapter(
                services,
                service -> {
            if (getContext() != null) {
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
                                service.getId(),
                                formattedDateTime,
                                service.getPrice(),
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
            }},
            service -> {
                // navigate with the service data
                Bundle bundle = new Bundle();
                bundle.putParcelable("petService", service);
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_petServiceDetailsFragment, bundle);
            }
        );
        servicesRecyclerView.setAdapter(servicesAdapter);
    }


}