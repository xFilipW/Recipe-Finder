package com.example.recipefinder;

import android.app.Application;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.recipefinder.api.cache.CacheRefreshWorker;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MyApplication extends Application {

    private static final String UNIQUE_WORK_NAME = "CacheRefreshWork";

    @Override
    public void onCreate() {
        super.onCreate();
        scheduleOneTimeWork();
        schedulePeriodicWork();
    }

    private void scheduleOneTimeWork() {
        OneTimeWorkRequest testWorkRequest = new OneTimeWorkRequest.Builder(CacheRefreshWorker.class).build();
        WorkManager.getInstance(this).enqueue(testWorkRequest);
    }

    private void schedulePeriodicWork() {
        long initialDelay = calculateInitialDelay();
        Constraints constraints = createNetworkConstraints();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(CacheRefreshWorker.class, 1, TimeUnit.DAYS)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(UNIQUE_WORK_NAME, ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest);
    }

    private long calculateInitialDelay() {
        Calendar now = Calendar.getInstance();
        Calendar nextRun = Calendar.getInstance();
        nextRun.set(Calendar.HOUR_OF_DAY, 0);
        nextRun.set(Calendar.MINUTE, 0);
        nextRun.set(Calendar.SECOND, 0);

        if (now.after(nextRun)) {
            nextRun.add(Calendar.DAY_OF_MONTH, 1);
        }

        return nextRun.getTimeInMillis() - now.getTimeInMillis();
    }

    private Constraints createNetworkConstraints() {
        return new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
    }
}
