package com.example.recipefinder.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.recipefinder.R;
import com.example.recipefinder.databinding.ActivityMainBinding;
import com.example.recipefinder.main.fragments.MainFavouriteFragment;
import com.example.recipefinder.main.fragments.MainHomeFragment;
import com.example.recipefinder.main.fragments.MainRandomFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.add(new MeowBottomNavigation.Model(1, R.drawable.ic_random));
        binding.bottomNavigationView.add(new MeowBottomNavigation.Model(2, R.drawable.ic_home));
        binding.bottomNavigationView.add(new MeowBottomNavigation.Model(3, R.drawable.ic_favourite));
        binding.bottomNavigationView.show(2, true);

        loadFragment(new MainHomeFragment());

        binding.bottomNavigationView.setOnClickMenuListener(model -> {
            Fragment fragment = null;
            switch (model.getId()) {
                case 1:
                    fragment = new MainRandomFragment();
                    break;
                case 2:
                    fragment = new MainHomeFragment();
                    break;
                case 3:
                    fragment = new MainFavouriteFragment();
                    break;
            }
            if (fragment != null) {
                loadFragment(fragment);
            }
            return null;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
