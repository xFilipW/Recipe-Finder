package com.example.recipefinder.api;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.recipefinder.api.models.RandomRecipeApiResponse;
import com.example.recipefinder.api.models.Recipe;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class RecipeCache {
    private static final String PREFS_NAME = "RecipeCachePrefs";
    private static final String RECIPES_KEY = "CachedRecipes";
    private static RecipeCache instance;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private RecipeCache(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    public static RecipeCache getInstance(Context context) {
        if (instance == null) {
            instance = new RecipeCache(context);
        }
        return instance;
    }

    public RandomRecipeApiResponse getCachedRecipes() {
        String json = sharedPreferences.getString(RECIPES_KEY, null);
        return json != null ? gson.fromJson(json, RandomRecipeApiResponse.class) : null;
    }

    public void setCachedRecipes(RandomRecipeApiResponse recipes) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(RECIPES_KEY, gson.toJson(recipes));
        editor.apply();
    }

    public void clearCache() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(RECIPES_KEY);
        editor.apply();
    }

    public List<Recipe> getRecipesByCategory(String category) {
        RandomRecipeApiResponse cachedResponse = getCachedRecipes();
        if (cachedResponse == null) {
            return new ArrayList<>();
        }

        List<Recipe> filteredRecipes = new ArrayList<>();
        for (Recipe recipe : cachedResponse.recipes) {
            if (recipe.dishTypes != null && recipe.dishTypes.contains(category.toLowerCase())) {
                filteredRecipes.add(recipe);
            }
        }

        return filteredRecipes;
    }
}
