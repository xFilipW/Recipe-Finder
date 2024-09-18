package com.example.recipefinder.api;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.recipefinder.R;
import com.example.recipefinder.api.cache.CacheManager;
import com.example.recipefinder.api.listeners.RandomRecipeResponseListener;
import com.example.recipefinder.api.models.RandomRecipeApiResponse;
import com.example.recipefinder.api.models.Recipe;
import com.example.recipefinder.database.RecipeTable;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RequestManager {
    private static final String FETCH_RECIPES_COUNT = "100";

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

    public void getRandomRecipes(Context context, RandomRecipeResponseListener randomRecipeResponseListener, String category) {
        if (!cacheManager.isCacheExpired()) {
            List<RecipeTable> cachedRecipes = cacheManager.getCachedRecipes(context);
            if (cachedRecipes != null) {
                if (category == null || category.equals("All recipes")) {
                    randomRecipeResponseListener.onSuccess(cachedRecipes);
                } else {
                    randomRecipeResponseListener.onSuccess(filterRecipesByCategory(cachedRecipes, category));
                }
                return;
            }
        }

        callRandomRecipes(randomRecipeResponseListener, category);
    }

    private void callRandomRecipes(RandomRecipeResponseListener listener, String category) {
        CallRandomRecipes callRandomRecipes = retrofit.create(CallRandomRecipes.class);
        Call<RandomRecipeApiResponse> call = callRandomRecipes.callRandomRecipe(
                context.getString(R.string.api_key),
                FETCH_RECIPES_COUNT,
                category != null ? category : ""
        );

        call.enqueue(new Callback<RandomRecipeApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<RandomRecipeApiResponse> call, @NonNull Response<RandomRecipeApiResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().recipes != null) {
                    // TODO: 17.09.2024
                    // cacheManager.saveRecipes(getApplicationContext(), allRecipes);

                    ArrayList<Recipe> allRecipes = new ArrayList<>(response.body().recipes);
                    listener.onSuccess(RecipeUtils.mapList(allRecipes));
                } else {
                    listener.onError("Response body or response body recipes is null");
                }
            }

            @Override
            public void onFailure(@NonNull Call<RandomRecipeApiResponse> call, @NonNull Throwable throwable) {
                listener.onError(throwable.getMessage());
            }
        });
    }

    private List<RecipeTable> filterRecipesByCategory(List<RecipeTable> response, String category) {
        List<RecipeTable> filteredRecipes = new ArrayList<>();

        for (RecipeTable recipe : response) {
            if (recipe.getDishTypes() != null && recipe.getDishTypes().contains(category.toLowerCase())) {
                filteredRecipes.add(recipe);
            }
        }
        return filteredRecipes;
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
