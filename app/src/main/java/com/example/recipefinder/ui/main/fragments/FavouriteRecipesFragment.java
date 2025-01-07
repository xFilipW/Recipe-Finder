package com.example.recipefinder.ui.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.recipefinder.api.RepositoryUseCase;
import com.example.recipefinder.databinding.FragmentFavouriteRecipesBinding;
import com.example.recipefinder.ui.main.adapters.RecipiesAdapter;
import com.example.recipefinder.ui.main.itemDecorators.HorizontalSpaceItemDecoration;
import com.example.recipefinder.ui.main.itemDecorators.VerticalSpaceItemDecoration;

public class FavouriteRecipesFragment extends Fragment {

    private FragmentFavouriteRecipesBinding binding;
    private RecipiesAdapter recipiesAdapter;
    private RepositoryUseCase repositoryUseCase;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavouriteRecipesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRepository();
        setupRecipesRecyclerView();

//        repositoryUseCase.querySelectRecipesByTitle(
//                SearchResultsFragmentArgs.fromBundle(getArguments()).getSearchQuery(),
//                data -> {
//                    recipiesAdapter.setData(data);
//                }
//        );
    }

    private void setupRepository() {
        repositoryUseCase = new RepositoryUseCase(requireContext());
    }

    private void setupRecipesRecyclerView() {
        binding.rvRecipes.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recipiesAdapter = new RecipiesAdapter(onItemClick -> {
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
