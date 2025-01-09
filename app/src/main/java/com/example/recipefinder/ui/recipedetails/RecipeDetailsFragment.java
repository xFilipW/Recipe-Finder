package com.example.recipefinder.ui.recipedetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.recipefinder.api.RepositoryUseCase;
import com.example.recipefinder.databinding.FragmentRecipeDetailsBinding;

public class RecipeDetailsFragment extends Fragment {

    private FragmentRecipeDetailsBinding binding;
    private RepositoryUseCase repositoryUseCase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        long id = RecipeDetailsFragmentArgs.fromBundle(getArguments()).getId();
        repositoryUseCase = new RepositoryUseCase(requireContext());

        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        NavHostFragment.findNavController(requireParentFragment()).popBackStack();
                    }
                }
        );

        repositoryUseCase.getRecipeDetails(id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
