package com.example.recipefinder.ui.dash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.recipefinder.R;
import com.example.recipefinder.databinding.FragmentDashBinding;

public class DashFragment extends Fragment {

    private FragmentDashBinding binding;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDashBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavHostFragment dashHostFragment = (NavHostFragment) getChildFragmentManager()
                .findFragmentById(R.id.dashFragmentContainer);

        if (dashHostFragment != null) {
            navController = dashHostFragment.getNavController();
        } else {
            throw new IllegalArgumentException("NavController missing!");
        }

        binding.mewoBottomNavigationView.add(new MeowBottomNavigation.Model(1, R.drawable.ic_shopping_cart));
        binding.mewoBottomNavigationView.add(new MeowBottomNavigation.Model(2, R.drawable.ic_home));
        binding.mewoBottomNavigationView.add(new MeowBottomNavigation.Model(3, R.drawable.ic_favourite));

        binding.mewoBottomNavigationView.setOnShowListener(item -> {
            // TODO: 09/09/2024
        });

        binding.mewoBottomNavigationView.setOnReselectListener(item -> {
            // TODO: 08/01/2025
        });

        binding.mewoBottomNavigationView.show(2, true);

        binding.mewoBottomNavigationView.setOnClickMenuListener(model -> {
            switch (model.getId()) {
                case 1:
                    navController.navigate(R.id.shoppingListFragment);
                    break;
                case 2:
                    navController.navigate(R.id.homeFragment);
                    break;
                case 3:
                    navController.navigate(R.id.favouriteRecipesFragment);
                    break;
            }
        });
    }
}
