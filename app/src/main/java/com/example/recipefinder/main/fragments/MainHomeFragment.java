package com.example.recipefinder.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.recipefinder.api.RequestManager;
import com.example.recipefinder.api.listeners.RandomRecipeResponseListener;
import com.example.recipefinder.api.models.RandomRecipeApiResponse;
import com.example.recipefinder.databinding.FragmentMainHomeBinding;
import com.example.recipefinder.main.Repository;
import com.example.recipefinder.main.adapters.CategoriesAdapter;
import com.example.recipefinder.main.adapters.RecipiesAdapter;

public class MainHomeFragment extends Fragment {

    private FragmentMainHomeBinding binding;
    private RequestManager requestManager;
    private RecipiesAdapter recipiesAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainHomeBinding.inflate(inflater, container, false);

        setupCategoriesRecyclerView();
        loadRandomRecipes();

        return binding.getRoot();
    }

    private void setupCategoriesRecyclerView() {
        binding.rvCategories.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        CategoriesAdapter categoriesAdapter = new CategoriesAdapter();
        categoriesAdapter.setData(Repository.CATEGORIES_DATA_LIST);
        binding.rvCategories.setAdapter(categoriesAdapter);
    }

    private void loadRandomRecipes() {
        showLoading(true);
        requestManager = new RequestManager(requireContext());
        requestManager.getRandomRecipes(recipeResponseListener);
    }

    private final RandomRecipeResponseListener recipeResponseListener = new RandomRecipeResponseListener() {
        @Override
        public void didFetch(RandomRecipeApiResponse response, String message) {
            setupRecipesRecyclerView(response);
            showLoading(false);
        }

        @Override
        public void didError(String message) {
            showError(message);
        }
    };

    private void setupRecipesRecyclerView(RandomRecipeApiResponse response) {
        binding.rvRecipes.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recipiesAdapter = new RecipiesAdapter(requireContext(), response.recipes);
        binding.rvRecipes.setAdapter(recipiesAdapter);
    }

    private void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        showLoading(false);
    }

    private void showLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.rvRecipes.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
