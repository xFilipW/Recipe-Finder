package com.example.recipefinder.api.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.recipefinder.api.models.RandomRecipeApiResponse;
import com.google.gson.Gson;

import java.util.Calendar;

public class CacheManager {
    private static final String PREFS_NAME = "RecipeCache";
    private static final String RECIPE_KEY = "CachedRecipes";
    private static final String LAST_UPDATE_TIME_KEY = "LastUpdateTime";

    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public CacheManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    public void saveRecipes(RandomRecipeApiResponse recipes) {
        String json = gson.toJson(recipes);
        sharedPreferences.edit()
                .putString(RECIPE_KEY, json)
                .putLong(LAST_UPDATE_TIME_KEY, System.currentTimeMillis())
                .apply();
    }

    public RandomRecipeApiResponse getCachedRecipes() {
        String json = sharedPreferences.getString(RECIPE_KEY, null);
        return json != null ? gson.fromJson(json, RandomRecipeApiResponse.class) : null;
    }

    public boolean isCacheExpired() {
        long lastUpdateTime = sharedPreferences.getLong(LAST_UPDATE_TIME_KEY, 0);
        Calendar midnight = Calendar.getInstance();
        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);
        midnight.set(Calendar.MILLISECOND, 0);
        return lastUpdateTime < midnight.getTimeInMillis();
    }

    public void clearCache() {
        sharedPreferences.edit()
                .remove(RECIPE_KEY)
                .remove(LAST_UPDATE_TIME_KEY)
                .apply();
    }
}
