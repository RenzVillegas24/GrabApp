package com.example.petapp.controllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petapp.R;
import com.example.petapp.adapters.BookingAdapter;
import com.example.petapp.database.BookingDatabaseHelper;
import com.example.petapp.models.Booking;

import java.util.List;

public class BookingFragment extends Fragment {
    private RecyclerView ordersRecyclerView;
    private BookingAdapter bookingAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.booking_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ordersRecyclerView = view.findViewById(R.id.ordersRecyclerView);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        BookingDatabaseHelper bookingDatabaseHelper = new BookingDatabaseHelper(getContext());
        List<Booking> bookings = bookingDatabaseHelper.getAllBookings();

        bookingAdapter = new BookingAdapter(bookings);
        ordersRecyclerView.setAdapter(bookingAdapter);
    }

    // update the booking list when the fragment is resumed
    @Override
    public void onResume() {
        super.onResume();
        BookingDatabaseHelper bookingDatabaseHelper = new BookingDatabaseHelper(getContext());
        List<Booking> bookings = bookingDatabaseHelper.getAllBookings();
        bookingAdapter = new BookingAdapter(bookings);
        ordersRecyclerView.setAdapter(bookingAdapter);
    }
}