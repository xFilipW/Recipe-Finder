package com.example.recipefinder.ui.main.fragments;

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
import com.example.recipefinder.database.RecipeTable;
import com.example.recipefinder.databinding.FragmentMainHomeBinding;
import com.example.recipefinder.listeners.RandomRecipeResponseListener;
import com.example.recipefinder.ui.main.adapters.CategoriesAdapter;
import com.example.recipefinder.ui.main.adapters.RecipiesAdapter;
import com.example.recipefinder.ui.main.fragments.itemDecorators.HorizontalSpaceItemDecoration;
import com.example.recipefinder.ui.main.fragments.itemDecorators.VerticalSpaceItemDecoration;
import com.example.recipefinder.ui.main.repository.Repository;

import java.util.List;

public class MainHomeFragment extends Fragment {

    private FragmentMainHomeBinding binding;
    private RequestManager requestManager;
    private RecipiesAdapter recipiesAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainHomeBinding.inflate(inflater, container, false);
        initializeUI();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadRandomRecipes(null, true);
    }

    private void initializeUI() {
        setupCategoriesRecyclerView();
        setupRecipesRecyclerView();
        setupRequestManager();
    }

    /**
     * @noinspection DataFlowIssue
     */
    private void setupCategoriesRecyclerView() {
        binding.rvCategories.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(category -> {
            loadCategory(category, false);
        });
        binding.rvCategories.setAdapter(categoriesAdapter);
        binding.rvCategories.addItemDecoration(new HorizontalSpaceItemDecoration(HorizontalSpaceItemDecoration.SpanCount.MORE, 12, requireContext()));
        ((CategoriesAdapter) binding.rvCategories.getAdapter()).setData(Repository.CATEGORIES_DATA_LIST);
    }

    private void setupRequestManager() {
        requestManager = new RequestManager(requireContext());
    }

    private void loadRandomRecipes(@Nullable String category, boolean isInitial) {
        toggleLoadingState(true, isInitial);

        requestManager.getRandomRecipes(new RandomRecipeResponseListener() {
            @Override
            public void onComplete(@NonNull List<RecipeTable> allRecipes) {
                if (allRecipes.isEmpty()) {
                    displayNoRecipesFound();
                } else {
                    displayRecipes(allRecipes);
                }
                toggleLoadingState(false, isInitial);
            }

            @Override
            public void onError(String message) {
                displayError(message);
                toggleLoadingState(false, isInitial);
            }
        }, category);
    }

    private void displayRecipes(List<RecipeTable> recipes) {
        binding.tvNoRecipeFound.setVisibility(View.GONE);
        recipiesAdapter.setData(recipes);
        binding.rvRecipes.setVisibility(View.VISIBLE);
    }

    private void setupRecipesRecyclerView() {
        binding.rvRecipes.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recipiesAdapter = new RecipiesAdapter();
        binding.rvRecipes.setAdapter(recipiesAdapter);
        binding.rvRecipes.addItemDecoration(new HorizontalSpaceItemDecoration(HorizontalSpaceItemDecoration.SpanCount.TWO, 16, requireContext()));
        binding.rvRecipes.addItemDecoration(new VerticalSpaceItemDecoration(VerticalSpaceItemDecoration.SpanCount.TWO, 16, requireContext()));
    }

    private void displayNoRecipesFound() {
        binding.rvRecipes.setVisibility(View.GONE);
        binding.tvNoRecipeFound.setVisibility(View.VISIBLE);
    }

    private void displayError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void loadCategory(String category, boolean isInitialize) {
        binding.rvRecipes.setVisibility(View.GONE);
        loadRandomRecipes(category, isInitialize);
    }

    private void toggleLoadingState(boolean isLoading, boolean isInitial) {
        int visibility = isLoading ? View.GONE : View.VISIBLE;
        if (isInitial) {

            binding.rvCategories.setVisibility(visibility);
            binding.tvRecipes.setVisibility(visibility);
            binding.tvCategories.setVisibility(visibility);
            binding.etSearch.setVisibility(visibility);
            binding.tvAmountOfRecipes.setVisibility(visibility);

            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);

            if (isLoading) {
                binding.tvNoRecipeFound.setVisibility(View.GONE);
            }
        } else {
            binding.rvRecipes.setVisibility(visibility);
            binding.progressBarRecipes.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
