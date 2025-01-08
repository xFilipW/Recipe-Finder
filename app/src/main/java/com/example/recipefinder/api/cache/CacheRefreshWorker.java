package com.example.recipefinder.api.cache;

import static com.example.recipefinder.api.cache.DatabaseUseCase.PREFS_NAME;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.recipefinder.api.RepositoryUseCase;
import com.example.recipefinder.database.AppDatabase;
import com.example.recipefinder.database.RecipeTable;
import com.example.recipefinder.shared.listeners.RandomRecipeResponseListener;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class CacheRefreshWorker extends Worker {

    private final DatabaseUseCase cacheManager;
    private final RepositoryUseCase repositoryUseCase;

    private static final String TAG = "CacheRefreshWorker";

    public CacheRefreshWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        cacheManager = new DatabaseUseCase(
                AppDatabase.getDatabase(context),
                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        );
        repositoryUseCase = new RepositoryUseCase(context);
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
        repositoryUseCase.getRecipes(new RandomRecipeResponseListener() {
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
