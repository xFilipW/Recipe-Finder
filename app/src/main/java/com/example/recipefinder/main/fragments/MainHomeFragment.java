package com.example.recipefinder.main.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.recipefinder.api.RecipeCache;
import com.example.recipefinder.api.RequestManager;
import com.example.recipefinder.api.listeners.RandomRecipeResponseListener;
import com.example.recipefinder.api.models.RandomRecipeApiResponse;
import com.example.recipefinder.api.models.Recipe;
import com.example.recipefinder.databinding.FragmentMainHomeBinding;
import com.example.recipefinder.main.Repository;
import com.example.recipefinder.main.adapters.CategoriesAdapter;
import com.example.recipefinder.main.adapters.RecipiesAdapter;

import java.util.List;

public class MainHomeFragment extends Fragment {

    private FragmentMainHomeBinding binding;
    private RequestManager requestManager;
    private RecipiesAdapter recipiesAdapter;
    private RecipeCache recipeCache;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainHomeBinding.inflate(inflater, container, false);

        binding.rvCategories.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recipeCache = RecipeCache.getInstance(requireContext());

        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(this::onCategoryClick);
        categoriesAdapter.setData(Repository.CATEGORIES_DATA_LIST);
        binding.rvCategories.setAdapter(categoriesAdapter);
        showLoading(true);
        requestManager = new RequestManager(requireContext());
        requestManager.getRandomRecipes(recipeResponseListener);
        binding.tvNoRecipeFound.setVisibility(View.GONE);

        return binding.getRoot();
    }

    private final RandomRecipeResponseListener recipeResponseListener = new RandomRecipeResponseListener() {
        @Override
        public void didFetch(RandomRecipeApiResponse response, String message) {
            // Log the number of recipes received
            if (response != null && response.recipes != null) {
                int recipeCount = response.recipes.size();
                Log.d("MainHomeFragment", "Number of recipes fetched: " + recipeCount);
            }

            binding.rvRecipes.setLayoutManager(new GridLayoutManager(requireContext(), 2));
            recipiesAdapter = new RecipiesAdapter(requireContext(), response.recipes);
            binding.rvRecipes.setAdapter(recipiesAdapter);
            binding.tvNoRecipeFound.setVisibility(View.GONE);
            showLoading(false);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            binding.tvNoRecipeFound.setVisibility(View.GONE);
            showLoading(false);
        }
    };

    private void onCategoryClick(String category) {
        showLoading(true);
        binding.tvNoRecipeFound.setVisibility(View.GONE);

        if (category.equals("All recipes")) {
            requestManager.getRandomRecipes(recipeResponseListener);
        } else {
            List<Recipe> filteredRecipes = recipeCache.getRecipesByCategory(category);
            if (filteredRecipes.isEmpty()) {
                binding.tvNoRecipeFound.setVisibility(View.VISIBLE);
            } else {
                binding.tvNoRecipeFound.setVisibility(View.GONE);
            }
            recipiesAdapter = new RecipiesAdapter(requireContext(), filteredRecipes);
            binding.rvRecipes.setAdapter(recipiesAdapter);
            showLoading(false);
        }
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.rvRecipes.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.rvRecipes.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
