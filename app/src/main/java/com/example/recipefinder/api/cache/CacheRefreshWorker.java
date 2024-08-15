package com.example.recipefinder.api.cache;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.recipefinder.api.RequestManager;
import com.example.recipefinder.api.listeners.RandomRecipeResponseListener;
import com.example.recipefinder.api.models.RandomRecipeApiResponse;

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
        requestManager.getRandomRecipes(new RandomRecipeResponseListener() {
            @Override
            public void didFetch(RandomRecipeApiResponse recipes, String message) {
                cacheManager.saveRecipes(recipes);
            }

            @Override
            public void didError(String errorMessage) {
                // Log error if needed
            }
        });
    }
}
