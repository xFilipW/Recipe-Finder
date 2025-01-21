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

        binding.mewBottomNavigationView.add(new MeowBottomNavigation.Model(1, R.drawable.ic_shopping_cart));
        binding.mewBottomNavigationView.add(new MeowBottomNavigation.Model(2, R.drawable.ic_home));
        binding.mewBottomNavigationView.add(new MeowBottomNavigation.Model(3, R.drawable.ic_favorite));

        binding.mewBottomNavigationView.setOnShowListener(item -> {
            // TODO: 09/09/2024
        });

        binding.mewBottomNavigationView.setOnReselectListener(item -> {
            // TODO: 08/01/2025
        });

        binding.mewBottomNavigationView.show(2, false);

        binding.mewBottomNavigationView.setOnClickMenuListener(model -> {
            switch (model.getId()) {
                case 1:
                    navController.navigate(R.id.shoppingListFragment);
                    break;
                case 2:
                    navController.navigate(R.id.homeFragment);
                    break;
                case 3:
                    navController.navigate(R.id.favoriteRecipesFragment);
                    break;
            }
        });
    }
}
