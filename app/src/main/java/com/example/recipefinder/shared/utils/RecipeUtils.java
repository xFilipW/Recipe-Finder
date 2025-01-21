package com.example.recipefinder.shared.utils;

import com.example.recipefinder.api.models.RecipeDetailsItem;
import com.example.recipefinder.database.RecipeTable;
import com.google.gson.Gson;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeUtils {
    public static List<RecipeTable> mapList(List<RecipeDetailsItem> allRecipes) {
        Gson gson = new Gson();

        return allRecipes.stream()
                .map(recipe -> new RecipeTable(
                        0,
                        recipe.getId(),
                        gson.toJson(recipe),
                        recipe.getTitle(),
                        String.join(",", recipe.getDishTypes()),
                        recipe.getImage())
                ).collect(Collectors.toList());
    }
}

