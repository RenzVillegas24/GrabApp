package com.example.petapp.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petapp.R;
import com.example.petapp.models.PetService;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.Locale;

public class PetServicesAdapter extends RecyclerView.Adapter<PetServicesAdapter.ServiceViewHolder> {
    private List<PetService> services;
    private OnBookClickListener bookClickListener;
    private OnItemClickListener itemClickListener;

    // Interface for book click callback
    public interface OnBookClickListener {
        void onBookClick(PetService service);
    }

    // Interface for item click callback
    public interface OnItemClickListener {
        void onItemClick(PetService service);
    }

    public PetServicesAdapter(List<PetService> services,
                              OnBookClickListener bookClickListener,
                              OnItemClickListener itemClickListener) {
        this.services = services;
        this.bookClickListener = bookClickListener;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_item_layout, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        PetService service = services.get(position);
        holder.bind(service);
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    class ServiceViewHolder extends RecyclerView.ViewHolder {
        private ImageView serviceIcon;
        private TextView serviceTitle;
        private TextView serviceDescription;
        private TextView servicePrice;
        private MaterialButton bookButton;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);

            serviceIcon = itemView.findViewById(R.id.serviceIcon);
            serviceTitle = itemView.findViewById(R.id.serviceTitle);
            serviceDescription = itemView.findViewById(R.id.serviceDescription);
            servicePrice = itemView.findViewById(R.id.servicePrice);
            bookButton = itemView.findViewById(R.id.bookServiceButton);
        }

        public void bind(PetService service) {
            serviceIcon.setImageResource(service.getIconResId());
            serviceTitle.setText(service.getTitle());
            serviceDescription.setText(service.getDescription());
            servicePrice.setText(String.format(Locale.getDefault(), "₱%.2f", service.getPrice()));

            // Make entire item view clickable
            itemView.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(service);
                    Log.i("Service", "bind: " + service.getTitle());
                }
            });

            bookButton.setOnClickListener(v -> {
                if (bookClickListener != null) {
                    bookClickListener.onBookClick(service);
                }
            });
        }
    }
}