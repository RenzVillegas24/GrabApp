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
import com.example.petapp.models.PetService;

import java.util.ArrayList;
import java.util.List;

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

        List<PetService> services = createServicesList();

        servicesAdapter = new PetServicesAdapter(
                services,
                service -> {
            if (getContext() != null) {
                Toast.makeText(getContext(),
                        "Booked: " + service.getTitle(),
                        Toast.LENGTH_SHORT).show();
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


    private List<PetService> createServicesList() {
        List<PetService> services = new ArrayList<>();

        services.add(new PetService(
                1,
                "Basic Grooming",
                "Includes brushing and basic cleaning",
                "A basic grooming service includes essential care to maintain the pet's hygiene and appearance.\nThis service helps keep the pet clean, reduces shedding, prevents matting, and can also contribute to overall health by keeping.",
                25.99,
                R.drawable.basic_grooming
        ));

        services.add(new PetService(
                2,
                "Special Grooming",
                "Full spa treatment for your pet",
                "The special grooming service goes beyond basic hygiene and includes styling the pet's fur to enhance its appearance.\nThe main difference from basic grooming is that special grooming involves artistic and breed-specific fur styling, while basic grooming only involves minimal trimming to maintain cleanliness.",
                45.50,
                R.drawable.special_grooming
        ));

        services.add(new PetService(
                3,
                "Pet Bath",
                "Thorough bath with premium shampoo",
                "A pet bath service involves thoroughly cleaning the pet's coat and skin using pet-safe shampoos and conditioners, tailored to the specific needs of the pet's breed and skin type.\nThis process not only removes dirt, allergens, and loose fur but also helps in maintaining a healthy, shiny coat. The service also includes drying the pet's fur.",
                35.00,
                R.drawable.pet_bath
        ));

        services.add(new PetService(
                4,
                "Nail Trimming",
                "Precise and gentle nail care",
                "Pet nail trimming is a grooming service that involves carefully cutting a pet's nail to a safe length using specialized clippers or grinders. This process helps prevent overgrown nails, which can cause discomfort, pain, and even injury to pets when they walk or run.\nTrimming also reduces the risk of nails snagging or breaking which can lead to infections.",
                15.99,
                R.drawable.nail_trimming
        ));

        services.add(new PetService(
                5,
                "Ear Cleaning",
                "Gentle and thorough ear cleaning",
                "Pet ear cleaning is a grooming service focused on maintaining a pet's ear health by gently removing dirt, wax buildup, and debris from the ear canal and outer ear. Using veterinarian-approved ear cleaning solutions, the process helps to prevent infections, irritation, and ear mites.",
                20.00,
                R.drawable.ear_cleaning
        ));

        services.add(new PetService(
                6,
                "Vaccination",
                "Essential health protection",
                "A pet rabies vaccination service provides essential protection against the rabies virus, a deadly disease that affects both animals and humans. The vaccine is administered by a veterinarian and is typically required by law to ensure public health safety.\nThis service not only shields pets from the virus but also prevents the spread of rabies through bites or scratches.",
                55.50,
                R.drawable.pet_vaccination
        ));

        services.add(new PetService(
                7,
                "Deworming",
                "Comprehensive parasite prevention",
                "A pet deworming service involves the administration of medication to eliminate internal parasites such as roundworms, hookworms, tapeworms, and whipworms.\nThis service is essential for maintaining the overall health of pets, as worms can cause serious health issues such as malnutrition, digestive problems, and even organ damage.",
                40.00,
                R.drawable.deworm
        ));

        return services;
    }
}