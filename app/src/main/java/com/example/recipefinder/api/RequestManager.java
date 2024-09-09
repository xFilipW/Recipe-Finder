package com.example.recipefinder.api;

import android.content.Context;

import com.example.recipefinder.R;
import com.example.recipefinder.api.cache.CacheManager;
import com.example.recipefinder.api.listeners.RandomRecipeResponseListener;
import com.example.recipefinder.api.models.RandomRecipeApiResponse;
import com.example.recipefinder.api.models.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RequestManager {
    private final Context context;
    private final Retrofit retrofit;
    private final CacheManager cacheManager;

    public RequestManager(Context context) {
        this.context = context;
        this.cacheManager = new CacheManager(context);
        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void getRandomRecipes(RandomRecipeResponseListener listener, String category) {
        if (!cacheManager.isCacheExpired()) {
            RandomRecipeApiResponse cachedRecipes = cacheManager.getCachedRecipes();
            if (cachedRecipes != null) {
                if (category == null || category.equals("All recipes")) {
                    listener.onSuccess(cachedRecipes, "Fetched from cache");
                } else {
                    listener.onSuccess(filterRecipesByCategory(cachedRecipes, category), "Filtered recipes from cache");
                }
                return;
            }
        }
        fetchRecipesInBatches(listener, category, 2);
    }

    private void fetchRecipesInBatches(RandomRecipeResponseListener listener, String category, int batchCount) {
        ArrayList<Recipe> allRecipes = new ArrayList<>();
        fetchBatch(listener, category, batchCount, 0, allRecipes);
    }

    private void fetchBatch(RandomRecipeResponseListener listener, String category, int batchCount, int currentBatch, ArrayList<Recipe> allRecipes) {
        if (currentBatch >= batchCount) {
            RandomRecipeApiResponse combinedResponse = new RandomRecipeApiResponse();
            combinedResponse.recipes = allRecipes;
            cacheManager.saveRecipes(combinedResponse);
            listener.onSuccess(combinedResponse, "Fetched " + allRecipes.size() + " recipes");
            return;
        }

        CallRandomRecipes callRandomRecipes = retrofit.create(CallRandomRecipes.class);
        Call<RandomRecipeApiResponse> call = callRandomRecipes.callRandomRecipe(
                context.getString(R.string.api_key),
                "100",
                category != null ? category : ""
        );
        call.enqueue(new Callback<RandomRecipeApiResponse>() {
            @Override
            public void onResponse(Call<RandomRecipeApiResponse> call, Response<RandomRecipeApiResponse> response) {
                if (!response.isSuccessful()) {
                    listener.onError(response.message());
                    return;
                }
                if (response.body() != null && response.body().recipes != null) {
                    allRecipes.addAll(response.body().recipes);
                }
                fetchBatch(listener, category, batchCount, currentBatch + 1, allRecipes);
            }

            @Override
            public void onFailure(Call<RandomRecipeApiResponse> call, Throwable throwable) {
                listener.onError(throwable.getMessage());
            }
        });
    }

    private RandomRecipeApiResponse filterRecipesByCategory(RandomRecipeApiResponse response, String category) {
        RandomRecipeApiResponse filteredResponse = new RandomRecipeApiResponse();
        filteredResponse.recipes = new ArrayList<>();

        for (Recipe recipe : response.recipes) {
            if (recipe.dishTypes != null && recipe.dishTypes.contains(category.toLowerCase())) {
                filteredResponse.recipes.add(recipe);
            }
        }
        return filteredResponse;
    }

    private interface CallRandomRecipes {
        @GET("recipes/random")
        Call<RandomRecipeApiResponse> callRandomRecipe(
                @Query("apiKey") String apiKey,
                @Query("number") String number,
                @Query("tags") String tags
        );
    }
}
