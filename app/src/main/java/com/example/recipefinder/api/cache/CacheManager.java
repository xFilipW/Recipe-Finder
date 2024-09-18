package com.example.recipefinder.api.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.recipefinder.database.AppDatabase;
import com.example.recipefinder.database.RecipeTable;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.List;

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

    private static final String TAG = "CacheManager";

    public void saveRecipes(Context context, List<RecipeTable> recipes) {
        Log.d(TAG, "saveRecipes() called with: context = [" + context + "], recipes = [" + recipes + "]");
//        String json = gson.toJson(recipes);
//        sharedPreferences.edit()
//                .putString(RECIPE_KEY, json)
//                .putLong(LAST_UPDATE_TIME_KEY, System.currentTimeMillis())
//                .apply();
        Long[] longs = AppDatabase.getDatabase(context).recipeTableDao().bulkInsert(
                recipes.toArray(new RecipeTable[0])
        );

        Log.d(TAG, "saveRecipes() returned: " + longs);
    }

    public List<RecipeTable> getCachedRecipes(Context context) {
//        String json = sharedPreferences.getString(RECIPE_KEY, null);
//        return json != null ? gson.fromJson(json, RandomRecipeApiResponse.class) : null;
        return AppDatabase.getDatabase(context).recipeTableDao().queryAll();
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
