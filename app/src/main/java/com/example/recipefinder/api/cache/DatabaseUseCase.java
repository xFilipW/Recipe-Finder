package com.example.recipefinder.api.cache;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.recipefinder.api.models.RecipeDetailsItem;
import com.example.recipefinder.database.AppDatabase;
import com.example.recipefinder.database.RecipeTable;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseUseCase {
    public static final String PREFS_NAME = "RecipeCache";

    private static final String LAST_UPDATE_TIME_KEY = "LastUpdateTime";
    public static final int MARK_AS_FAVORITE = 1;

    private final SharedPreferences sharedPreferences;
    private final AppDatabase appDatabase;

    public DatabaseUseCase(AppDatabase appDatabase, SharedPreferences sharedPreferences) {
        this.appDatabase = appDatabase;
        this.sharedPreferences = sharedPreferences;
    }

    private static final String TAG = "CacheManager";

    public void queryInsertRecipes(List<RecipeTable> recipes, OnQueryCompleteListener<Long[]> listener) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            Log.d(TAG, "saveRecipes: saving recipes (" + recipes.size() + ")");
            Long[] longs = appDatabase.recipeTableDao().insertRecipes(
                    recipes.toArray(new RecipeTable[0])
            );
            Log.d(TAG, "saveRecipes: saved recipes (" + longs.length + ")");
            handler.post(() -> listener.onComplete(longs));
        });
    }

    public void queryRemoveRecipes(OnQueryCompleteListener<Integer> listener) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            Log.d(TAG, "removeRecipes: removing recipes (all)");
            int longs = appDatabase.recipeTableDao().deleteRecipes();
            Log.d(TAG, "removeRecipes: removed recipes (" + longs + ")");
            handler.post(() -> listener.onComplete(longs));
        });
    }

    public void queryRecipes(OnQueryCompleteListener<List<RecipeTable>> listener) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            List<RecipeTable> recipeTables = appDatabase.recipeTableDao().queryRecipes();
            handler.post(() -> listener.onComplete(recipeTables));
        });
    }

    public void querySelectRecipesByTitle(String phrase, OnQueryCompleteListener<List<RecipeTable>> listener) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            List<RecipeTable> recipeTables = appDatabase.recipeTableDao().queryRecipesByTitle(phrase);
            handler.post(() -> listener.onComplete(recipeTables));
        });
    }

    public void queryRecipesByPhraseAndCategory(@NonNull String category, @NonNull String phrase, OnQueryCompleteListener<List<RecipeTable>> listener) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            List<RecipeTable> recipeTables = appDatabase.recipeTableDao().queryRecipesByPhraseAndCategory(phrase, category);
            handler.post(() -> listener.onComplete(recipeTables));
        });
    }

    public void isCacheExpired(OnServiceCompleteListener<Boolean> onServiceCompleteListener) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            long lastUpdateTime = sharedPreferences.getLong(LAST_UPDATE_TIME_KEY, 0);

            Calendar lastCalendar = Calendar.getInstance();
            lastCalendar.setTimeInMillis(lastUpdateTime);
            Log.d(TAG, "lastUpdateTime: " + lastCalendar.getTime());

            Calendar midnight = Calendar.getInstance();
            midnight.set(Calendar.HOUR_OF_DAY, 0);
            midnight.set(Calendar.MINUTE, 0);
            midnight.set(Calendar.SECOND, 0);
            midnight.set(Calendar.MILLISECOND, 0);
            Log.d(TAG, "midnight: " + midnight.getTime());

            handler.post(() -> onServiceCompleteListener.onComplete(
                    lastUpdateTime < midnight.getTimeInMillis()
            ));
        });
    }

    public void saveLastUpdate() {
        Log.d(TAG, "saveLastUpdate: saving last update time");
        sharedPreferences.edit()
                .putLong(LAST_UPDATE_TIME_KEY, System.currentTimeMillis())
                .apply();
    }

    public void insertRecipeDetailsToFavorite(RecipeDetailsItem currentRecipeDetailsItem, OnQueryCompleteListener<Void> listener) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            appDatabase.recipeTableDao().insertRecipeDetails(
                    currentRecipeDetailsItem.getId(),
                    currentRecipeDetailsItem.getTitle(),
                    new Gson().toJson(currentRecipeDetailsItem.getExtendedIngredients()),
                    new Gson().toJson(currentRecipeDetailsItem.getAnalyzedInstructions()),
                    new Gson().toJson(currentRecipeDetailsItem.getNutrition()),
                    buildTags(currentRecipeDetailsItem),
                    currentRecipeDetailsItem.getImage(),
                    TextUtils.join(", ", currentRecipeDetailsItem.getDishTypes()),
                    MARK_AS_FAVORITE
            );
            handler.post(() -> listener.onComplete(null));
        });
    }

    private String buildTags(RecipeDetailsItem currentRecipeDetailsItem) {
        StringBuilder sb = new StringBuilder();
        if (currentRecipeDetailsItem.isVegetarian())
            sb.append("vegetarian").append(", ");
        if (currentRecipeDetailsItem.isVegan())
            sb.append("vegan").append(", ");
        if (currentRecipeDetailsItem.isGlutenFree())
            sb.append("glutenFree").append(", ");
        if (currentRecipeDetailsItem.isDairyFree())
            sb.append("dairyFree").append(", ");

        if (sb.length() >= ", ".length())
            sb.setLength(sb.length() - ", ".length());

        return sb.toString();
    }
}
