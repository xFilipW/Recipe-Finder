package com.example.recipefinder.api.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.recipefinder.database.AppDatabase;
import com.example.recipefinder.database.RecipeTable;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CacheManager {
    private static final String PREFS_NAME = "RecipeCache";
    private static final String RECIPE_KEY = "CachedRecipes";
    private static final String LAST_UPDATE_TIME_KEY = "LastUpdateTime";

    private final SharedPreferences sharedPreferences;
    private final Context context;

    public CacheManager(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    private static final String TAG = "CacheManager";

    public void saveRecipes(List<RecipeTable> recipes, OnQueryCompleteListener<Long[]> listener) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            Log.d(TAG, "saveRecipes: saving recipes (" + recipes.size() + ")");
            Long[] longs = AppDatabase.getDatabase(context).recipeTableDao().bulkInsert(
                    recipes.toArray(new RecipeTable[0])
            );
            Log.d(TAG, "saveRecipes: saved recipes (" + longs.length + ")");
            handler.post(() -> {
                listener.onComplete(longs);
            });
        });
    }

    public void removeRecipes(OnQueryCompleteListener<Integer> listener) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            Log.d(TAG, "removeRecipes: removing recipes (all)");
            int longs = AppDatabase.getDatabase(context).recipeTableDao().deleteRecipes();
            Log.d(TAG, "removeRecipes: removed recipes (" + longs + ")");
            handler.post(() -> {
                listener.onComplete(longs);
            });
        });
    }

    public void getCachedRecipes(Context context, OnQueryCompleteListener<List<RecipeTable>> listener) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            List<RecipeTable> recipeTables = AppDatabase.getDatabase(context).recipeTableDao().queryAll();
            handler.post(() -> {
                listener.onComplete(recipeTables);
            });
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
}
