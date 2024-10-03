package com.example.recipefinder.api.cache;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.recipefinder.api.RequestManager;
import com.example.recipefinder.database.RecipeTable;
import com.example.recipefinder.listeners.RandomRecipeResponseListener;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class CacheRefreshWorker extends Worker {

    private final CacheManager cacheManager;
    private final RequestManager requestManager;

    private static final String TAG = "CacheRefreshWorker";

    public CacheRefreshWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.cacheManager = new CacheManager(context);
        this.requestManager = new RequestManager(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        cacheManager.isCacheExpired(expired -> {
            if (expired) {
                refreshCache();
            }
            countDownLatch.countDown();
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return Result.success();
    }

    private void refreshCache() {
        requestManager.getRandomRecipes(new RandomRecipeResponseListener() {
            @Override
            public void onComplete(@NonNull List<RecipeTable> allRecipes) {
                // Left empty intentionally
            }

            @Override
            public void onError(String errorMessage) {
                // Left empty intentionally
            }
        }, null);
    }
}
