package com.example.recipefinder.ui.recipeDetails;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.recipefinder.R;
import com.example.recipefinder.api.RepositoryUseCase;
import com.example.recipefinder.api.cache.OnQueryCompleteListener;
import com.example.recipefinder.api.models.RecipeDetailsItem;
import com.example.recipefinder.databinding.FragmentRecipeDetailsBinding;
import com.example.recipefinder.shared.listeners.RecipeDetailsResponseListener;

public class RecipeDetailsFragment extends Fragment {

    private FragmentRecipeDetailsBinding binding;
    private RepositoryUseCase repositoryUseCase;
    private boolean isInCart = false;
    private final boolean isFavorite = false;
    private Toast customToast;
    private RecipeDetailsItem currentRecipeDetailsItem;

    private enum State {READY, LOADING, IDLE}

    private State currentState = State.IDLE;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        long recipeId = RecipeDetailsFragmentArgs.fromBundle(getArguments()).getId();
        repositoryUseCase = new RepositoryUseCase(requireContext());

        setupBackNavigation();
        setupActionBarListeners(recipeId);
        updateState(State.LOADING);

        fetchRecipeDetails(recipeId);
    }

    private void setupBackNavigation() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        NavHostFragment.findNavController(requireParentFragment()).popBackStack();
                    }
                }
        );
    }

    private void setupActionBarListeners(long recipeId) {
        binding.ivShoppingCart.setOnClickListener(v -> updateCartStatus());
        binding.ivFavorite.setOnClickListener(v -> changeFavoriteStatus(recipeId));
    }

    private void fetchRecipeDetails(long id) {
        repositoryUseCase.getRecipeDetails(id, new RecipeDetailsResponseListener() {
            @Override
            public void onComplete(@NonNull RecipeDetailsItem recipeDetailsItem) {
                currentRecipeDetailsItem = recipeDetailsItem;

                repositoryUseCase.addRecipeToRecipeDetailsIfNotExists(
                        currentRecipeDetailsItem,
                        data -> { // FIXME: 18.02.2025 void zle, bardziej boolean informujacy o stanie transakcji bd
                            displayRecipeDetails(recipeDetailsItem);
                            updateState(State.READY);
                        }
                );

                updateFavoriteStatus(id);
            }

            @Override
            public void onError(String message) {
                showErrorToast("Failed to fetch recipe details");
            }
        });
    }

    private void displayRecipeDetails(RecipeDetailsItem recipeDetailsItem) {
        updateView(binding.ivDishPicture, recipeDetailsItem.getImage());
        updateText(binding.tvNameOfTheDish, recipeDetailsItem.getTitle());
        updateText(binding.tvReadyInMinutesValue, recipeDetailsItem.getCookingMinutes() + " min");
        updateText(binding.tvNumberOfServingsValue, recipeDetailsItem.getServings() + " servings");
        updateText(binding.tvPreparationTimeValue, getPreparationTimeText(recipeDetailsItem.getPreparationMinutes()));
        updateText(binding.tvAggregateLikes, "(" + recipeDetailsItem.getAggregateLikes() + ")");
    }

    private void updateState(State state) {
        currentState = state;
        binding.clLoading.setVisibility(state == State.LOADING ? View.VISIBLE : View.GONE);
    }

    private void updateCartStatus() {

    }

    private void updateFavoriteIconStatus(int isFavorite) {
        binding.ivFavorite.setImageResource(isFavorite == 1 ? R.drawable.ic_favorite : R.drawable.ic_favorite_outline);
    }

    private void changeFavoriteStatus(long id) {
        repositoryUseCase.isFavorite(id, new OnQueryCompleteListener<Integer>() {
            @Override
            public void onComplete(Integer data) {
                int opposite = data == 0 ? 1 : 0;
                repositoryUseCase.setFavorite(id, opposite, new OnQueryCompleteListener<Integer>() {
                    @Override
                    public void onComplete(Integer currentFavorite) {
                        updateFavoriteIconStatus(currentFavorite);
                        showFavoriteToast(currentFavorite);
                    }
                });
            }
        });
    }

    private void updateFavoriteStatus(long id) {
        repositoryUseCase.isFavorite(id, new OnQueryCompleteListener<Integer>() {
            @Override
            public void onComplete(Integer data) {
                updateFavoriteIconStatus(data);
            }
        });
    }

    private void showFavoriteToast(Integer favoriteValue) {
        String message = favoriteValue == 0 ? "Remove from favorites" : "Added to favorites";
        int iconResId = favoriteValue == 0 ? R.drawable.ic_cancel : R.drawable.ic_check_circle;

        if (customToast != null) {
            customToast.cancel();
        }

        customToast = new Toast(requireContext());

        LayoutInflater inflater = getLayoutInflater();
        View customToastView = inflater.inflate(R.layout.widget_custom_toast, (ViewGroup) getView(), false);

        ((TextView) customToastView.findViewById(R.id.tvToastMessage)).setText(message);
        ((ImageView) customToastView.findViewById(R.id.ivToastIcon)).setImageResource(iconResId);

        customToast.setView(customToastView);
        customToast.setDuration(Toast.LENGTH_SHORT);

        customToast.show();
    }

    // FIXME: 4.03.2025 rozdzielic na oddzielne rzeczy tj favorite i basket
    private void toggleState(String itemType, boolean isAdded, int addedIconResId, int removedIconResId) {
        int iconResId = isAdded ? addedIconResId : removedIconResId;
        String message = isAdded ? "Added to " + itemType : "Removed from " + itemType;
        int toastIconResId = isAdded ? R.drawable.ic_check_circle : R.drawable.ic_cancel;

        if (itemType.equals("basket")) {
            binding.ivShoppingCart.setImageResource(iconResId);
        } else if (itemType.equals("favorites")) {
            binding.ivFavorite.setImageResource(iconResId);
        }

        showToast(message, toastIconResId);
    }

    private void updateText(TextView textView, String text) {
        textView.setText(text);
    }

    private void updateView(ImageView imageView, String imageUrl) {
        imageView.setImageURI(Uri.parse(imageUrl));
    }

    private String getPreparationTimeText(int preparationTime) {
        if (preparationTime < 10) return "Short";
        if (preparationTime < 20) return "Medium";
        return "Long";
    }

    private void showToast(String message, int iconResId) {

    }

    private void showErrorToast(String message) {
        showToast(message, R.drawable.ic_cancel);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
