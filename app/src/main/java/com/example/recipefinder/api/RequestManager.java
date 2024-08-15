package com.example.recipefinder.api;

import android.content.Context;

import com.example.recipefinder.R;
import com.example.recipefinder.api.cache.CacheManager;
import com.example.recipefinder.api.listeners.RandomRecipeResponseListener;
import com.example.recipefinder.api.models.RandomRecipeApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RequestManager {
    Context context;
    Retrofit retrofit;
    CacheManager cacheManager;

    public RequestManager(Context context) {
        this.context = context;
        this.cacheManager = new CacheManager(context);
        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void getRandomRecipes(RandomRecipeResponseListener listener) {
        if (!cacheManager.isCacheExpired()) {
            RandomRecipeApiResponse cachedRecipes = cacheManager.getCachedRecipes();
            if (cachedRecipes != null) {
                listener.didFetch(cachedRecipes, "Fetched from cache");
                return;
            }
        }

        CallRandomRecipes callRandomRecipes = retrofit.create(CallRandomRecipes.class);
        Call<RandomRecipeApiResponse> call = callRandomRecipes.callRandomRecipe(context.getString(R.string.api_key), "100");
        call.enqueue(new Callback<RandomRecipeApiResponse>() {
            @Override
            public void onResponse(Call<RandomRecipeApiResponse> call, Response<RandomRecipeApiResponse> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }
                cacheManager.saveRecipes(response.body());
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RandomRecipeApiResponse> call, Throwable throwable) {
                listener.didError(throwable.getMessage());
            }
        });
    }

    private interface CallRandomRecipes {
        @GET("recipes/random")
        Call<RandomRecipeApiResponse> callRandomRecipe(
                @Query("apiKey") String apiKey,
                @Query("number") String number
        );
    }
}
