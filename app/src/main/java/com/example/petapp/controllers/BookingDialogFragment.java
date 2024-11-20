package com.example.petapp.controllers;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.petapp.R;
import com.example.petapp.database.BookingDatabaseHelper;
import com.example.petapp.models.PetService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BookingDialogFragment extends DialogFragment {
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Spinner animalTypeSpinner;
    private PetService service;

    public static BookingDialogFragment newInstance(PetService service) {
        BookingDialogFragment fragment = new BookingDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("service", service);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        View view = requireActivity().getLayoutInflater().inflate(R.layout.booking_dialog, null);

        if (getArguments() != null) {
            service = getArguments().getParcelable("service");
        }

        datePicker = view.findViewById(R.id.bookingDatePicker);
        timePicker = view.findViewById(R.id.bookingTimePicker);
        animalTypeSpinner = view.findViewById(R.id.animalTypeSpinner);
        TextView serviceTitle = view.findViewById(R.id.bookingServiceTitle);
        Button confirmButton = view.findViewById(R.id.confirmBookingButton);

        // Set up animal type spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.animal_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        animalTypeSpinner.setAdapter(adapter);

        // Set minimum date to today
        datePicker.setMinDate(System.currentTimeMillis() - 1000);

        if (service != null) {
            serviceTitle.setText("Book " + service.getTitle());
        }

        confirmButton.setOnClickListener(v -> {
            Calendar selectedDateTime = Calendar.getInstance();
            selectedDateTime.set(
                    datePicker.getYear(),
                    datePicker.getMonth(),
                    datePicker.getDayOfMonth(),
                    timePicker.getHour(),
                    timePicker.getMinute()
            );

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            String formattedDateTime = dateFormat.format(selectedDateTime.getTime());

            String selectedAnimalType = animalTypeSpinner.getSelectedItem().toString();

            // Save booking to database
            BookingDatabaseHelper dbHelper = new BookingDatabaseHelper(requireContext());
            long bookingId = dbHelper.insertBooking(
                    service.getId(),
                    formattedDateTime,
                    service.getPrice(),
                    selectedAnimalType
            );

            if (bookingId != -1) {
                Toast.makeText(requireContext(),
                        "Booking confirmed for " + formattedDateTime +
                                " - " + selectedAnimalType,
                        Toast.LENGTH_SHORT).show();
                dismiss();
            } else {
                Toast.makeText(requireContext(),
                        "Booking failed",
                        Toast.LENGTH_SHORT).show();
            }
        });

        builder.setView(view);
        return builder.create();
    }
}