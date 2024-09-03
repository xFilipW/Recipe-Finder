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
import com.example.recipefinder.main.adapters.CategoriesAdapter;
import com.example.recipefinder.main.adapters.RecipiesAdapter;
import com.example.recipefinder.main.fragments.itemDecorators.HorizontalSpaceItemDecoration;
import com.example.recipefinder.main.fragments.itemDecorators.VerticalSpaceItemDecoration;
import com.example.recipefinder.main.repository.Repository;

public class MainHomeFragment extends Fragment {

    private FragmentMainHomeBinding binding;
    private RequestManager requestManager;
    private RecipiesAdapter recipiesAdapter;
    private Thread handler = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainHomeBinding.inflate(inflater, container, false);
        initializeUI();

        loadRandomRecipes(null);
        return binding.getRoot();
    }

    private void initializeUI() {
        setupCategoriesRecyclerView();
        setupRecipesRecyclerView();
        setupRequestManager();
    }

    private void setupCategoriesRecyclerView() {
        binding.rvCategories.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(new CategoriesAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClick(String category) {
                loadRecipes(category);
            }
        });
        categoriesAdapter.setData(Repository.CATEGORIES_DATA_LIST);
        binding.rvCategories.setAdapter(categoriesAdapter);
        binding.rvCategories.addItemDecoration(new HorizontalSpaceItemDecoration(categoriesAdapter.getItemCount(), 12, requireContext()));
    }

//    private String category = null;

//    private Runnable r = new Runnable() {
//        @Override
//        public void run() {
//            requestManager.getRandomRecipes(new RecipeResponseListener(), category);
//        }
//    };

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        handler = new Thread(r);
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//
//        if (handler != null) {
//            try {
//                handler.join();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }

    private void setupRequestManager() {
        requestManager = new RequestManager(requireContext());
    }

    private void loadRandomRecipes(@Nullable String category) {
        toggleLoadingState(true);
        requestManager.getRandomRecipes(new RecipeResponseListener(), category);
    }

    private class RecipeResponseListener implements RandomRecipeResponseListener {
        @Override
        public void didFetch(RandomRecipeApiResponse response, String message) {
            handleRecipeResponse(response);
            toggleLoadingState(false);
        }

        @Override
        public void didError(String message) {
            displayError(message);
            toggleLoadingState(false);
        }
    }

    private void handleRecipeResponse(RandomRecipeApiResponse response) {
        if (response.recipes == null || response.recipes.isEmpty()) {
            displayNoRecipesFound();
        } else {
            displayRecipes(response);
        }
    }

    private void displayRecipes(RandomRecipeApiResponse response) {
        binding.tvNoRecipeFound.setVisibility(View.GONE);
        recipiesAdapter.submitRecipes(response.recipes);
        binding.rvRecipes.setVisibility(View.VISIBLE);
    }

    private void setupRecipesRecyclerView() {
        binding.rvRecipes.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recipiesAdapter = new RecipiesAdapter();
        binding.rvRecipes.setAdapter(recipiesAdapter);
        binding.rvRecipes.addItemDecoration(new HorizontalSpaceItemDecoration(2, 16, requireContext()));
        binding.rvRecipes.addItemDecoration(new VerticalSpaceItemDecoration(2, 16, requireContext()));
    }

    private void displayNoRecipesFound() {
        binding.rvRecipes.setVisibility(View.GONE);
        binding.tvNoRecipeFound.setVisibility(View.VISIBLE);
    }

    private void displayError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void loadRecipes(String category) {
        binding.rvRecipes.setVisibility(View.GONE);
        loadRandomRecipes(category);
    }

    private void toggleLoadingState(boolean isLoading) {
        int visibility = isLoading ? View.GONE : View.VISIBLE;

        binding.rvCategories.setVisibility(visibility);
        binding.tvRecipes.setVisibility(visibility);
        binding.tvCategories.setVisibility(visibility);
        binding.etSearch.setVisibility(visibility);
        binding.tvAmountOfRecipes.setVisibility(visibility);

        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        if (isLoading) {
            binding.tvNoRecipeFound.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
