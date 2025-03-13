package com.example.recipefinder.ui.dash.favoriteRecipes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.recipefinder.R;
import com.example.recipefinder.api.RepositoryUseCase;
import com.example.recipefinder.api.cache.OnQueryCompleteListener;
import com.example.recipefinder.database.RecipeTable;
import com.example.recipefinder.databinding.FragmentFavoriteRecipesBinding;
import com.example.recipefinder.shared.itemDecorators.HorizontalSpaceItemDecoration;
import com.example.recipefinder.shared.itemDecorators.VerticalSpaceItemDecoration;
import com.example.recipefinder.ui.dash.home.adapters.RecipiesAdapter;

import java.util.List;

public class FavoriteRecipesFragment extends Fragment {

    private FragmentFavoriteRecipesBinding binding;
    private RecipiesAdapter recipiesAdapter;
    private RepositoryUseCase repositoryUseCase;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoriteRecipesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private static final String TAG = "FavoriteRecipesFragment";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRepository();
        setupRecipesRecyclerView();
        repositoryUseCase.getFavoriteRecipes(new OnQueryCompleteListener<List<RecipeTable>>() {
            @Override
            public void onComplete(List<RecipeTable> data) {
                Log.d(TAG, "favorite recipes: " + data.size());
                // TODO: 11.03.2025  przekazac dane do rv
                if (!data.isEmpty()) {
                    recipiesAdapter.setData(data);
                    binding.rvRecipes.setVisibility(View.VISIBLE);
                    binding.ivNoFavoritesYet.setVisibility(View.GONE);
                    binding.tvNoRecipesFound.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setupRepository() {
        repositoryUseCase = new RepositoryUseCase(requireContext());
    }

    private void setupRecipesRecyclerView() {
        binding.rvRecipes.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recipiesAdapter = new RecipiesAdapter(id -> {
            FragmentActivity activity = requireActivity();
            FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
            Fragment mainFragmentContainer = supportFragmentManager.findFragmentById(R.id.mainFragmentContainer);
            NavController mainNavController = NavHostFragment.findNavController(mainFragmentContainer);

            Bundle bundle = new Bundle();
            bundle.putLong("id", id);
            mainNavController.navigate(R.id.recipeDetailsFragment, bundle);
        });
        binding.rvRecipes.setAdapter(recipiesAdapter);
        binding.rvRecipes.addItemDecoration(new HorizontalSpaceItemDecoration(HorizontalSpaceItemDecoration.SpanCount.TWO, 16, requireContext()));
        binding.rvRecipes.addItemDecoration(new VerticalSpaceItemDecoration(VerticalSpaceItemDecoration.SpanCount.TWO, 16, requireContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
