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

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.OrderViewHolder> {
    private List<Booking> bookingList;
    private List<PetService> services;

    public BookingAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
        this.services = new ServicesList().getServices();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booking_item_layout, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        PetService service = services.get(booking.getServiceId());
        holder.serviceNameTextView.setText(service.getTitle());
        holder.statusTextView.setText(booking.getStatus());
        holder.dateTextView.setText(booking.getBookingDate());

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("booking", booking);
            bundle.putParcelable("petService", service);
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_bookingDetailsFragment2, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView serviceNameTextView;
        TextView statusTextView;
        TextView dateTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceNameTextView = itemView.findViewById(R.id.serviceNameTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }
}