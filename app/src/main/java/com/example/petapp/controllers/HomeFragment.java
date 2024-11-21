package com.example.petapp.controllers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.Navigation;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.petapp.R;
import com.example.petapp.models.User;
import com.example.petapp.utils.UserSessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    private ImageView userProfileImage;
    private TextView userNameText, userEmailText;
    private MaterialButton signOutButton;

    private UserSessionManager userSessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        userProfileImage = view.findViewById(R.id.userProfileImage);
        userNameText = view.findViewById(R.id.userNameText);
        userEmailText = view.findViewById(R.id.userEmailText);
        signOutButton = view.findViewById(R.id.signOutButton);

        userSessionManager = new UserSessionManager(requireContext());

        // Display user information
        displayUserInfo();

        // Setup sign-out button
        signOutButton.setOnClickListener(v -> {
            userSessionManager.logout();
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_welcomeFragment);
        });

        // Setup ViewPager with FragmentStateAdapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapter);

        // Connect TabLayout with ViewPager
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Services");
                    break;
                case 1:
                    tab.setText("Bookings");
                    break;
            }
        }).attach();
    }

    private void displayUserInfo() {
        User currentUser = userSessionManager.getCurrentUser();
        if (currentUser != null) {
            userNameText.setText(currentUser.getName());
            userEmailText.setText(currentUser.getEmail());
            // Load user profile image from blob
            byte[] imageBlob = currentUser.getUserIcon();
            if (imageBlob != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBlob, 0, imageBlob.length);
                userProfileImage.setImageBitmap(bitmap);
            } else {
                // get the svg xml vector drawable
                Drawable drawable = AppCompatResources.getDrawable(requireContext(), R.drawable.vector_man);
                userProfileImage.setImageDrawable(drawable);
            }
        }
    }

    private static class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 1:
                    return new BookingFragment();
                default:
                    return new ServicesFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}