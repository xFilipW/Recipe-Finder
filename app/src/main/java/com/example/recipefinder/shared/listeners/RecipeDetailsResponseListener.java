package com.example.recipefinder.shared.listeners;

import androidx.annotation.NonNull;

import com.example.recipefinder.database.RecipeDetailsTable;

import java.util.List;

public interface RecipeDetailsResponseListener {
    void onComplete(@NonNull List<RecipeDetailsTable> allRecipes);

    void onError(String message);
}
