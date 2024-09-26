package com.example.recipefinder.listeners;

import androidx.annotation.NonNull;

import com.example.recipefinder.database.RecipeTable;

import java.util.List;

public interface RandomRecipeResponseListener {
    void onComplete(@NonNull List<RecipeTable> allRecipes);

    void onError(String message);
}
