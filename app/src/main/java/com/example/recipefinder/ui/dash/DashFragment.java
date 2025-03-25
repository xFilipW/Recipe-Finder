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

        binding.bottomNavBar.setMenuResource(R.menu.bottom_nav_menu);

        binding.bottomNavBar.setItemSelected(R.id.itemHome, true);

        binding.bottomNavBar.setOnItemSelectedListener(id -> {
            if (navController == null) return;

            if (id == R.id.itemShoppingCart) {
                navController.navigate(R.id.shoppingListFragment);
            } else if (id == R.id.itemHome) {
                navController.navigate(R.id.homeFragment);
            } else if (id == R.id.itemFavorite) {
                navController.navigate(R.id.favoriteRecipesFragment);
            }
        });
    }
}
