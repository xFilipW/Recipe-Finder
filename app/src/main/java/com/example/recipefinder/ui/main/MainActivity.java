package com.example.recipefinder.ui.main;

import static androidx.navigation.Navigation.findNavController;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.recipefinder.R;
import com.example.recipefinder.databinding.ActivityMainBinding;
import com.example.recipefinder.ui.main.fragments.MainFavouriteFragment;
import com.example.recipefinder.ui.main.fragments.MainHomeFragment;
import com.example.recipefinder.ui.main.fragments.MainRandomFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainer2);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        } else {
            throw new IllegalArgumentException("NavController missing!");
        }

        binding.mewoBottomNavigationView.add(new MeowBottomNavigation.Model(1, R.drawable.ic_random));
        binding.mewoBottomNavigationView.add(new MeowBottomNavigation.Model(2, R.drawable.ic_home));
        binding.mewoBottomNavigationView.add(new MeowBottomNavigation.Model(3, R.drawable.ic_favourite));

        binding.mewoBottomNavigationView.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                // TODO: 09/09/2024  
            }
        });

        binding.mewoBottomNavigationView.show(2, true);

        binding.mewoBottomNavigationView.setOnClickMenuListener(model -> {
            switch (model.getId()) {
                case 1:
                    navController.navigate(R.id.mainRandomFragment);
                    break;
                case 2:
                    navController.navigate(R.id.mainHomeFragment);
                    break;
                case 3:
                    navController.navigate(R.id.mainFavouriteFragment);
                    break;
            }
        });
    }
}
