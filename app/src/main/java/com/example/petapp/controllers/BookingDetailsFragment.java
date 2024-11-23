package com.example.petapp.controllers;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.petapp.R;
import com.example.petapp.database.BookingDatabaseHelper;
import com.example.petapp.models.Booking;
import com.example.petapp.models.PetService;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class BookingDetailsFragment extends Fragment {
    private Booking booking;
    private PetService petService;
    private BookingDatabaseHelper bookingDatabaseHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            booking = getArguments().getParcelable("booking");
            petService = getArguments().getParcelable("petService");
        }

        bookingDatabaseHelper = new BookingDatabaseHelper(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.booking_details_fragment, container, false);

        TextView serviceTitleTextView = view.findViewById(R.id.serviceTitleTextView);
        TextView servicePriceTextView = view.findViewById(R.id.servicePriceTextView);
        TextView bookingDateTextView = view.findViewById(R.id.bookingDateTextView);
        TextView additionalNotesTextView = view.findViewById(R.id.additionalNotesTextView);
        TextView addressTextView = view.findViewById(R.id.addressTextView);
        TextView additionalNotesTitleTextView = view.findViewById(R.id.additionalNotesTitleTextView);

        ImageView iconView = view.findViewById(R.id.iconView);
        View backButton = view.findViewById(R.id.backButton);
        MaterialButton payButton = view.findViewById(R.id.payButton);
        MaterialButton markAsDoneButton = view.findViewById(R.id.markAsDoneButton);
        MaterialButton cancelButton = view.findViewById(R.id.cancelButton);
        AutoCompleteTextView paymentMethodDropdown = view.findViewById(R.id.paymentMethodDropdown);

        if (!Objects.equals(booking.getStatus(), "PENDING")) {
            payButton.setVisibility(View.GONE);
        }
        if (Objects.equals(booking.getStatus(), "DONE")) {
            cancelButton.setVisibility(View.GONE);
            markAsDoneButton.setVisibility(View.GONE);
            paymentMethodDropdown.setEnabled(false);
        }

        if (petService != null) {
            serviceTitleTextView.setText(petService.getTitle());
            servicePriceTextView.setText(String.format(Locale.getDefault(), "₱%.2f", petService.getPrice()));
            iconView.setImageResource(petService.getIconResId());
        }

        if (booking != null) {
            bookingDateTextView.setText(booking.getBookingDate());
            additionalNotesTextView.setText(booking.getAdditionalNotes());
            addressTextView.setText(booking.getAddress());

            if ("CANCELLED".equals(booking.getStatus())) {
                payButton.setVisibility(View.GONE);
                markAsDoneButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);
                paymentMethodDropdown.setEnabled(false);
            }

            if (booking.getAdditionalNotes() == null || booking.getAdditionalNotes().isEmpty()) {
                additionalNotesTitleTextView.setVisibility(View.GONE);
                additionalNotesTextView.setVisibility(View.GONE);
            }
        }

        backButton.setOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        ArrayAdapter<String> paymentMethodAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                new String[]{"GCash", "ATM", "CASH"}
        );
        paymentMethodDropdown.setAdapter(paymentMethodAdapter);
        paymentMethodDropdown.setText(booking.getPaymentMethod(), false);

        paymentMethodDropdown.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                bookingDatabaseHelper.updatePaymentMethod(booking.getId(), s.toString());
            }
        });

        payButton.setOnClickListener(v -> showConfirmationDialog("Pay Booking", "Are you sure you want to pay for this booking?", () -> {
            bookingDatabaseHelper.updateBookingStatus(booking.getId(), "CONFIRMED");
            bookingDatabaseHelper.updatePaymentStatus(booking.getId(), "PAID");
            Toast.makeText(requireContext(), "Booking Paid", Toast.LENGTH_SHORT).show();

            // Hide the pay button
            payButton.setVisibility(View.GONE);
        }));

        markAsDoneButton.setOnClickListener(v -> showConfirmationDialog("Mark as Done", "Are you sure you want to mark this booking as done?", () -> {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());
            try {
                Date bookingDate = sdf.parse(booking.getBookingDate());
                if (bookingDate != null && bookingDate.before(new Date())) {
                    bookingDatabaseHelper.updateBookingStatus(booking.getId(), "DONE");
                    Toast.makeText(requireContext(), "Booking Marked as Done", Toast.LENGTH_SHORT).show();

                    // Hide the mark as done button
                    markAsDoneButton.setVisibility(View.GONE);
                    paymentMethodDropdown.setEnabled(false);
                    cancelButton.setVisibility(View.GONE);
                    payButton.setVisibility(View.GONE);
                } else {
                    Toast.makeText(requireContext(), "Cannot mark as done before the booking time", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        cancelButton.setOnClickListener(v -> showConfirmationDialog("Cancel Booking", "Are you sure you want to cancel this booking?", () -> {
            if (!"DONE".equals(booking.getStatus())) {
                bookingDatabaseHelper.updateBookingStatus(booking.getId(), "CANCELLED");
                Toast.makeText(requireContext(), "Booking Cancelled", Toast.LENGTH_SHORT).show();

                // Hide the cancel button
                cancelButton.setVisibility(View.GONE);
                markAsDoneButton.setVisibility(View.GONE);
                payButton.setVisibility(View.GONE);
                paymentMethodDropdown.setEnabled(false);
            } else {
                Toast.makeText(requireContext(), "Cannot cancel a completed booking", Toast.LENGTH_SHORT).show();
            }
        }));

        return view;
    }

    private void showConfirmationDialog(String title, String message, Runnable onConfirm) {
        new AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Yes", (dialog, which) -> onConfirm.run())
                .setNegativeButton("No", null)
                .show();
    }
}