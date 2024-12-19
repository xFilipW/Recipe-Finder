package com.example.recipefinder.ui.main.fragments;

import static com.example.recipefinder.ui.main.fragments.MainHomeFragmentDirections.*;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.recipefinder.R;
import com.example.recipefinder.api.RepositoryUseCase;
import com.example.recipefinder.database.RecipeTable;
import com.example.recipefinder.databinding.FragmentMainHomeBinding;
import com.example.recipefinder.listeners.RandomRecipeResponseListener;
import com.example.recipefinder.ui.main.adapters.CategoriesAdapter;
import com.example.recipefinder.ui.main.adapters.RecipiesAdapter;
import com.example.recipefinder.ui.main.fragments.itemDecorators.HorizontalSpaceItemDecoration;
import com.example.recipefinder.ui.main.fragments.itemDecorators.VerticalSpaceItemDecoration;
import com.example.recipefinder.ui.main.listeners.OnItemClickListenerEx;
import com.example.recipefinder.ui.main.repository.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainHomeFragment extends Fragment {

    private FragmentMainHomeBinding binding;
    private RepositoryUseCase repositoryUseCase;
    private RecipiesAdapter recipiesAdapter;
    private NavController navController;
    private String activeCategory = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        initializeUI();
        loadRandomRecipes(null, true);
    }

    private void initializeUI() {
        setupSearchView();
        setupCategoriesRecyclerView();
        setupRecipesRecyclerView();
        setupRepository();
    }

    private void setupSearchView() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // left blank
            }

            @Override
            public void afterTextChanged(Editable phrase) {
                queryRecipesByPhraseAndCategory(activeCategory, phrase.toString());
            }
        });
    }

<<<<<<< Updated upstream
    private void queryRecipesByPhraseAndCategory(String category, String phrase) {
        if (phrase.isEmpty()) {
            repositoryUseCase.queryRecipesByPhraseAndCategory(
                    category,
                    phrase,
                    data -> {
                        recipiesAdapter.setData(data);
                        String searchMessage = getString(R.string.random_200_recipes_each_day);
                        binding.tvAmountOfRecipes.setText(searchMessage);
                    }
            );
        } else {
            repositoryUseCase.queryRecipesByPhraseAndCategory(
                    category,
                    phrase,
                    data -> {
                        recipiesAdapter.setData(data);
                        String searchMessage = String.format(
                                Locale.getDefault(),
                                "Amount of recipes for \"%s\": %d",
                                phrase,
                                data.size()
                        );
                        binding.tvAmountOfRecipes.setText(searchMessage);
                    }
            );
<<<<<<< Updated upstream
        }
=======
<<<<<<< Updated upstream
//        }
=======
        }
=======
    private void queryRecipesByPhraseAndCategory(String category, @NonNull String phrase) {
        String trimmedPhrase = phrase.trim();

        repositoryUseCase.queryRecipesByPhraseAndCategory(
                category,
                trimmedPhrase,
                data -> {
                    recipiesAdapter.setData(data);

                    String newPhrase;

                    if (trimmedPhrase.isEmpty()) {
                        newPhrase = (activeCategory == null || activeCategory.isEmpty())
                                ? "200 random recipes each day"
                                : "Recipes in " + activeCategory + ": " + data.size();
                    } else {
                        newPhrase = (activeCategory == null || activeCategory.isEmpty())
                                ? "Recipes for \"" + trimmedPhrase + "\": " + data.size()
                                : "Recipes for \"" + trimmedPhrase + "\" in " + activeCategory + ": " + data.size();
                    }

                    String searchMessage = String.format(
                            Locale.getDefault(),
                            "%s",
                            newPhrase
                    );
                    binding.tvAmountOfRecipes.setText(searchMessage);
                }
        );
>>>>>>> Stashed changes
>>>>>>> Stashed changes
>>>>>>> Stashed changes
    }

    /**
     * @noinspection DataFlowIssue
     */
    private void setupCategoriesRecyclerView() {
        binding.rvCategories.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
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

    private void loadRandomRecipes(@Nullable String category, boolean initialRun) {
        activeCategory = category;

        toggleLoadingState(true, initialRun, false);

        repositoryUseCase.getRecipes(new RandomRecipeResponseListener() {
            @Override
            public void onComplete(@NonNull List<RecipeTable> allRecipes) {
                if (allRecipes.isEmpty()) {
                    toggleLoadingState(false, initialRun, true);
                    displayNoRecipes();
                } else {
                    toggleLoadingState(false, initialRun, false);
                    displayRecipes(allRecipes);
                }
            }

            @Override
            public void onError(String message) {
                displayError(message);
                toggleLoadingState(false, initialRun, false);
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
        recipiesAdapter = new RecipiesAdapter(new OnItemClickListenerEx() {
            @Override
            public void onItemClick(long id) {
                ActionMainHomeFragmentToRecipeDetailsFragment navDirections = MainHomeFragmentDirections.actionMainHomeFragmentToRecipeDetailsFragment(id);
                //navDirections.setId(id);
                navController.navigate(navDirections);
            }
        });
        binding.rvRecipes.setAdapter(recipiesAdapter);
        binding.rvRecipes.addItemDecoration(new HorizontalSpaceItemDecoration(HorizontalSpaceItemDecoration.SpanCount.TWO, 16, requireContext()));
        binding.rvRecipes.addItemDecoration(new VerticalSpaceItemDecoration(VerticalSpaceItemDecoration.SpanCount.TWO, 16, requireContext()));
    }

    private void displayNoRecipes() {
        binding.rvRecipes.setVisibility(View.GONE);
        recipiesAdapter.setData(Collections.emptyList());
        binding.tvNoRecipeFound.setVisibility(View.VISIBLE);
    }

    private void displayError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void toggleLoadingState(boolean isLoading, boolean isInitial, boolean isEmpty) {
        if (isInitial) {
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
        } else { // specific category
            if (isLoading) {
                binding.progressBarRecipes.setVisibility(View.VISIBLE);
                binding.rvRecipes.setVisibility(View.GONE);
            } else {
                binding.progressBarRecipes.setVisibility(View.GONE);
                binding.rvRecipes.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
