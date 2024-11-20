package com.example.petapp.controllers;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.petapp.R;
import com.example.petapp.models.PetService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

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
            servicePriceTextView.setText(String.format("$%.2f", petService.getPrice()));
            serviceLongDescriptionTextView.setText(petService.getLongDescription());
            iconView.setImageResource(petService.getIconResId());
        }

        // Back button click listener
        backButton.setOnClickListener(v -> {
            Navigation.findNavController(view).navigateUp();
        });

        // Add to cart button click listener
        addToCartButton.setOnClickListener(v -> {
            // Implement add to cart functionality
            // For example:
//            CartManager.getInstance().addService(petService);
            Snackbar.make(view, "Added to cart", Snackbar.LENGTH_SHORT).show();
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