package com.example.recipefinder.api.cache;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.recipefinder.api.RequestManager;
import com.example.recipefinder.api.listeners.RandomRecipeResponseListener;
import com.example.recipefinder.api.models.Recipe;
import com.example.recipefinder.database.RecipeTable;

import java.util.ArrayList;
import java.util.List;

public class CacheRefreshWorker extends Worker {

    private final CacheManager cacheManager;
    private final RequestManager requestManager;

    public CacheRefreshWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.cacheManager = new CacheManager(context);
        this.requestManager = new RequestManager(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        if (cacheManager.isCacheExpired()) {
            refreshCache();
        }
        return Result.success();
    }

    private void refreshCache() {
        cacheManager.clearCache();
        requestManager.getRandomRecipes(getApplicationContext(), new RandomRecipeResponseListener() {
            @Override
            public void onSuccess(List<RecipeTable> allRecipes) {
                cacheManager.saveRecipes(getApplicationContext(), allRecipes);
            }

            @Override
            public void onError(String errorMessage) {
                // Log error if needed
            }
        }, null);
    }
}
