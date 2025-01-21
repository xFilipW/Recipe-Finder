package com.example.recipefinder.ui.recipeDetails;

import static com.example.recipefinder.ui.recipeDetails.RecipeDetailsFragment.State.LOADING;
import static com.example.recipefinder.ui.recipeDetails.RecipeDetailsFragment.State.READY;

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
import com.example.recipefinder.api.models.RecipeDetailsItem;
import com.example.recipefinder.databinding.FragmentRecipeDetailsBinding;
import com.example.recipefinder.shared.listeners.RecipeDetailsResponseListener;

public class RecipeDetailsFragment extends Fragment {

    private FragmentRecipeDetailsBinding binding;
    private RepositoryUseCase repositoryUseCase;
    private boolean isInCart = false;
    private boolean isFavorite = false;
    private Toast customToast;
    private RecipeDetailsItem currentRecipeDetailsItem;

    enum State {
        READY, LOADING, IDLE
    }

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

        setupOnBackPressed();
        setupActionBarListeners();
        setState(LOADING);
        fetchRecipeDetails(recipeId);
    }

    private void setState(State state) {
        switch (state) {
            case READY:
                binding.clLoading.setVisibility(View.GONE);
                break;
            case LOADING:
                binding.clLoading.setVisibility(View.VISIBLE);
                break;
            case IDLE:
                break;
        }
    }

    private void setupOnBackPressed() {
        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        NavHostFragment.findNavController(requireParentFragment()).popBackStack();
                    }
                }
        );
    }

    private void setupActionBarListeners() {
        binding.ivShoppingCart.setOnClickListener(v -> toggleCartStatus());
        binding.ivFavorite.setOnClickListener(v -> toggleFavoriteStatus());
    }

    private void toggleCartStatus() {
        isInCart = !isInCart;
        int iconResId = isInCart ? R.drawable.ic_shopping_cart : R.drawable.ic_shopping_cart_outline;
        String message = isInCart ? "Added to basket" : "Removed from basket";
        int toastIconResId = isInCart ? R.drawable.ic_check_circle : R.drawable.ic_cancel;

        binding.ivShoppingCart.setImageResource(iconResId);
        showToast(message, toastIconResId);
    }

    private void toggleFavoriteStatus() {
        isFavorite = !isFavorite;
        int iconResId = isFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_outline;
        String message = isFavorite ? "Added to favorites" : "Removed from favorites";
        int toastIconResId = isFavorite ? R.drawable.ic_check_circle : R.drawable.ic_cancel;

        binding.ivFavorite.setImageResource(iconResId);

        //zapis do bd
        repositoryUseCase.addRecipeDetailsToFavorite(currentRecipeDetailsItem);

        showToast(message, toastIconResId);
    }

    private void fetchRecipeDetails(long id) {
        repositoryUseCase.getRecipeDetails(id, new RecipeDetailsResponseListener() {
            @Override
            public void onComplete(@NonNull RecipeDetailsItem recipeDetailsItem) {
                currentRecipeDetailsItem = recipeDetailsItem;
                updatePreparationTime(recipeDetailsItem.getPreparationMinutes());
                updateLikes(recipeDetailsItem.getAggregateLikes());
                setState(READY);
            }

            @Override
            public void onError(String message) {
                // Handle error if needed
            }
        });
    }

    private void updatePreparationTime(int preparationTime) {
        String preparationTimeText = getPreparationTimeText(preparationTime);
        binding.tvPreparationTimeValue.setText(preparationTimeText);
    }

    private String getPreparationTimeText(int preparationTime) {
        if (preparationTime < 10) {
            return "Short";
        } else if (preparationTime < 20) {
            return "Medium";
        } else {
            return "Long";
        }
    }

    private void updateLikes(int aggregateLikes) {
        binding.tvAggregateLikes.setText("(" + aggregateLikes + ")");
    }

    private void showToast(String message, int iconResId) {
        if (customToast != null)
            customToast.cancel();

        LayoutInflater inflater = getLayoutInflater();
        View customToastView = inflater.inflate(R.layout.wiget_custom_toast, (ViewGroup) getView(), false);

        TextView toastMessage = customToastView.findViewById(R.id.tvToastMessage);
        toastMessage.setText(message);

        ImageView toastIcon = customToastView.findViewById(R.id.ivToastIcon);
        toastIcon.setImageResource(iconResId);

        customToast = new Toast(requireContext());
        customToast.setDuration(Toast.LENGTH_SHORT);
        customToast.setView(customToastView);
        customToast.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}