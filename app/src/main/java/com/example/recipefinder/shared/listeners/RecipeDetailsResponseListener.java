package com.example.recipefinder.shared.listeners;

import androidx.annotation.NonNull;

import com.example.recipefinder.api.models.RecipeDetailsItem;

public interface RecipeDetailsResponseListener {
    void onComplete(@NonNull RecipeDetailsItem recipeDetailsItem);

    void onError(String message);
}
