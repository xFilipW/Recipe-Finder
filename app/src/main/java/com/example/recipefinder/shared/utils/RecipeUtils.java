package com.example.recipefinder.shared.utils;

import com.example.recipefinder.api.models.Recipe;
import com.example.recipefinder.database.RecipeTable;
import com.google.gson.Gson;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeUtils {
    public static List<RecipeTable> mapList(List<Recipe> allRecipes) {
        Gson gson = new Gson();

        return allRecipes.stream()
                .map(recipe -> new RecipeTable(
                        0,
                        gson.toJson(recipe),
                        recipe.title,
                        String.join(",", recipe.dishTypes),
                        recipe.image,
                        0)
                ).collect(Collectors.toList());
    }
}

