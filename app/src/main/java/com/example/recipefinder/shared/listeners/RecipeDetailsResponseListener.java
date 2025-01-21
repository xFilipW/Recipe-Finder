package com.example.recipefinder.shared.listeners;

import androidx.annotation.NonNull;

import com.example.recipefinder.api.models.RecipeDetailsItem;
import com.example.recipefinder.database.RecipeDetailsTable;

import java.util.List;

public interface RecipeDetailsResponseListener {
    void onComplete(@NonNull RecipeDetailsItem recipeDetailsItem);

    void onError(String message);
}
