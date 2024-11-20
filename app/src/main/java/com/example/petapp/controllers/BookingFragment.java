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
import com.example.petapp.models.Order;

import java.util.ArrayList;
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

        List<Order> orders = createOrdersList();

        bookingAdapter = new BookingAdapter(orders);
        ordersRecyclerView.setAdapter(bookingAdapter);
    }

    private List<Order> createOrdersList() {
        List<Order> orders = new ArrayList<>();
        // Example orders - you'll likely fetch these from a database or backend
        orders.add(new Order(1, "Basic Grooming", "Completed", "2024-02-15"));
        orders.add(new Order(2, "Vaccination", "Pending", "2024-03-01"));
        orders.add(new Order(3, "Nail Trimming", "In Progress", "2024-03-10"));
        return orders;
    }
}