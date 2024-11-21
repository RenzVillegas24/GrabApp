package com.example.petapp.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petapp.R;
import com.example.petapp.datas.ServicesList;
import com.example.petapp.models.Booking;
import com.example.petapp.models.PetService;

import java.util.ArrayList;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SECTION_HEADER = 0;
    private static final int VIEW_TYPE_BOOKING_ITEM = 1;

    private List<Object> items;
    private List<PetService> services;

    public BookingAdapter(List<Booking> bookingList) {
        this.services = new ServicesList().getServices();
        this.items = new ArrayList<>();
        sortAndGroupBookings(bookingList);
    }

    private void sortAndGroupBookings(List<Booking> bookingList) {
        List<Booking> currentBookings = new ArrayList<>();
        List<Booking> historyBookings = new ArrayList<>();

        for (Booking booking : bookingList) {
            if ("PENDING".equals(booking.getStatus()) || "CONFIRMED".equals(booking.getStatus())) {
                currentBookings.add(booking);
            } else {
                historyBookings.add(booking);
            }
        }

        if (!currentBookings.isEmpty()) {
            items.add("Current Bookings");
            items.addAll(currentBookings);
        }

        if (!historyBookings.isEmpty()) {
            items.add("History");
            items.addAll(historyBookings);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof String) {
            return VIEW_TYPE_SECTION_HEADER;
        } else {
            return VIEW_TYPE_BOOKING_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SECTION_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.section_header_layout, parent, false);
            return new SectionHeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.booking_item_layout, parent, false);
            return new BookingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_SECTION_HEADER) {
            SectionHeaderViewHolder sectionHeaderViewHolder = (SectionHeaderViewHolder) holder;
            sectionHeaderViewHolder.sectionTitleTextView.setText((String) items.get(position));
        } else {
            BookingViewHolder bookingViewHolder = (BookingViewHolder) holder;
            Booking booking = (Booking) items.get(position);
            PetService service = services.get(booking.getServiceId());
            bookingViewHolder.serviceNameTextView.setText(service.getTitle());
            bookingViewHolder.statusTextView.setText(booking.getStatus());
            bookingViewHolder.dateTextView.setText(booking.getBookingDate());

            bookingViewHolder.itemView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable("booking", booking);
                bundle.putParcelable("petService", service);
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_bookingDetailsFragment2, bundle);
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class SectionHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView sectionTitleTextView;

        public SectionHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionTitleTextView = itemView.findViewById(R.id.sectionTitleTextView);
        }
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView serviceNameTextView;
        TextView statusTextView;
        TextView dateTextView;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceNameTextView = itemView.findViewById(R.id.serviceNameTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }
}