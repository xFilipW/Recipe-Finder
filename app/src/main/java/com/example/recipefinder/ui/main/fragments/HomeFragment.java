package com.example.recipefinder.ui.main.fragments;

import static com.example.recipefinder.ui.main.fragments.HomeFragmentDirections.ActionMainHomeFragmentToRecipeDetailsFragment;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.recipefinder.api.RepositoryUseCase;
import com.example.recipefinder.database.RecipeTable;
import com.example.recipefinder.databinding.FragmentHomeBinding;
import com.example.recipefinder.listeners.RandomRecipeResponseListener;
import com.example.recipefinder.ui.main.adapters.CategoriesAdapter;
import com.example.recipefinder.ui.main.adapters.RecipiesAdapter;
import com.example.recipefinder.ui.main.fragments.itemDecorators.HorizontalSpaceItemDecoration;
import com.example.recipefinder.ui.main.fragments.itemDecorators.VerticalSpaceItemDecoration;
import com.example.recipefinder.ui.main.listeners.SimpleTextWatcher;
import com.example.recipefinder.ui.main.repository.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RepositoryUseCase repositoryUseCase;
    private RecipiesAdapter recipiesAdapter;
    private NavController navController;
    private String activeCategory = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        initializeUI();
        loadRandomRecipes();
    }

    private void initializeUI() {
        setupSearchView();
        setupCategoriesRecyclerView();
        setupRecipesRecyclerView();
        setupRepository();
    }

    private void setupSearchView() {
        binding.etSearch.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable phrase) {
                queryRecipesByPhraseAndCategory(activeCategory, phrase.toString());
            }
        });
    }

    private void queryRecipesByPhraseAndCategory(@NonNull String category, @NonNull String phrase) {
        String trimmedPhrase = phrase.trim();

        repositoryUseCase.queryRecipesByPhraseAndCategory(
                category,
                trimmedPhrase,
                data -> {
                    if (data.isEmpty()) {
                        displayNoRecipes();
                    } else {
                        displayRecipes(data);
                    }

                    String newPhrase = handlePhraseMessage(trimmedPhrase, data);

                    String searchMessage = String.format(
                            Locale.getDefault(),
                            "%s",
                            newPhrase
                    );

                    binding.tvAmountOfRecipes.setText(searchMessage);
                }
        );
    }

    @NonNull
    private String handlePhraseMessage(@NonNull String trimmedPhrase, List<RecipeTable> data) {
        String newPhrase;

        if (activeCategory == null)
            throw new IllegalArgumentException("activeCategory cannot be null");

        if (trimmedPhrase.isEmpty()) {
            newPhrase = activeCategory.isEmpty()
                    ? "200 random recipes each day"
                    : "Recipes in " + activeCategory + ": " + data.size();
        } else {
            newPhrase = activeCategory.isEmpty()
                    ? "Recipes for \"" + trimmedPhrase + "\": " + data.size()
                    : "Recipes for \"" + trimmedPhrase + "\" in " + activeCategory + ": " + data.size();
        }
        return newPhrase;
    }

    /**
     * @noinspection DataFlowIssue
     */
    private void setupCategoriesRecyclerView() {
        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(category -> {
            activeCategory = category.equals(RepositoryUseCase.ALL_RECIPES) ? "" : category;
            queryRecipesByPhraseAndCategory(activeCategory, binding.etSearch.getText().toString());
        });
        binding.rvCategories.setAdapter(categoriesAdapter);
        binding.rvCategories.addItemDecoration(new HorizontalSpaceItemDecoration(HorizontalSpaceItemDecoration.SpanCount.MORE, 12, requireContext()));
        ((CategoriesAdapter) binding.rvCategories.getAdapter()).setData(Repository.CATEGORIES_DATA_LIST);
    }

    private void setupRepository() {
        repositoryUseCase = new RepositoryUseCase(requireContext());
    }

    private void loadRandomRecipes() {
        activeCategory = "";

        toggleLoadingState(true);

        repositoryUseCase.getRecipes(new RandomRecipeResponseListener() {
            @Override
            public void onComplete(@NonNull List<RecipeTable> allRecipes) {
                if (allRecipes.isEmpty()) {
                    toggleLoadingState(false);
                    displayNoRecipes();
                } else {
                    toggleLoadingState(false);
                    displayRecipes(allRecipes);
                }
            }

            @Override
            public void onError(String message) {
                displayError(message);
                toggleLoadingState(false);
            }
        }, "");
    }

    private void displayRecipes(List<RecipeTable> recipes) {
        binding.tvNoRecipesFound.setVisibility(View.GONE);
        recipiesAdapter.setData(recipes);
        binding.rvRecipes.setVisibility(View.VISIBLE);
    }

    private void setupRecipesRecyclerView() {
        binding.rvRecipes.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recipiesAdapter = new RecipiesAdapter(id -> {
            ActionMainHomeFragmentToRecipeDetailsFragment navDirections = HomeFragmentDirections.actionMainHomeFragmentToRecipeDetailsFragment(id);
            //navDirections.setId(id);
            navController.navigate(navDirections);
        });
        binding.rvRecipes.setAdapter(recipiesAdapter);
        binding.rvRecipes.addItemDecoration(new HorizontalSpaceItemDecoration(HorizontalSpaceItemDecoration.SpanCount.TWO, 16, requireContext()));
        binding.rvRecipes.addItemDecoration(new VerticalSpaceItemDecoration(VerticalSpaceItemDecoration.SpanCount.TWO, 16, requireContext()));
    }

    private void displayNoRecipes() {
        binding.rvRecipes.setVisibility(View.GONE);
        recipiesAdapter.setData(Collections.emptyList());
        binding.tvNoRecipesFound.setVisibility(View.VISIBLE);
    }

    private void displayError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void toggleLoadingState(boolean isLoading) {
        int goneWhenLoading = isLoading ? View.GONE : View.VISIBLE;
        int visibleWhenLoading = isLoading ? View.VISIBLE : View.GONE;

        binding.rvCategories.setVisibility(goneWhenLoading);
        binding.tvRecipes.setVisibility(goneWhenLoading);
        binding.tvCategories.setVisibility(goneWhenLoading);
        binding.etSearch.setVisibility(goneWhenLoading);
        binding.tvAmountOfRecipes.setVisibility(goneWhenLoading);

        binding.progressBar.setVisibility(visibleWhenLoading);

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
