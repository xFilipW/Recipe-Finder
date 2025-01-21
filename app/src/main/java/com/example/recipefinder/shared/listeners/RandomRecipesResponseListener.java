package com.example.recipefinder.shared.listeners;

import androidx.annotation.NonNull;

import com.example.recipefinder.database.RecipeTable;

import java.util.List;

public interface RandomRecipesResponseListener {
    void onComplete(@NonNull List<RecipeTable> allRecipes);

    void onError(String message);
}