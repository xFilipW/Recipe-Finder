package com.example.recipefinder.api.listeners;

import com.example.recipefinder.database.RecipeTable;

import java.util.List;

public interface RandomRecipeResponseListener {
    void onSuccess(List<RecipeTable> allRecipes);

    void onError(String message);
}
