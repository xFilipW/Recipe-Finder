package com.example.recipefinder.api;

import android.content.Context;

import com.example.recipefinder.R;
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
    private final Context context;
    private final Retrofit retrofit;

    public RequestManager(Context context) {
        this.context = context;
        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void getRandomRecipes(RandomRecipeResponseListener listener) {
        System.out.println("getRandomRecipes: method invoked");

        RecipeCache cache = RecipeCache.getInstance(context);
        RandomRecipeApiResponse cachedRecipes = cache.getCachedRecipes();

        if (cachedRecipes != null && cachedRecipes.recipes != null && !cachedRecipes.recipes.isEmpty()) {
            System.out.println("getRandomRecipes: fetched from cache");
            System.out.println("Cached recipes count: " + cachedRecipes.recipes.size());
            listener.didFetch(cachedRecipes, "Fetched from cache");
            return;
        }

        System.out.println("getRandomRecipes: cache is empty or invalid, fetching from API");
        fetchRecipesFromApi(listener, cache);
    }

    private void fetchRecipesFromApi(RandomRecipeResponseListener listener, RecipeCache cache) {
        System.out.println("fetchRecipesFromApi: method invoked");

        CallRandomRecipes callRandomRecipes = retrofit.create(CallRandomRecipes.class);

        String includedCategories = "main course,breakfast,appetizer,dessert,salad,bread,soup,sauce,snack,drink";
        String apiKey = context.getString(R.string.api_key);

        System.out.println("fetchRecipesFromApi: API Key = " + apiKey);

        Call<RandomRecipeApiResponse> call = callRandomRecipes.callRandomRecipe(
                apiKey,
                "100",
                includedCategories
        );

        call.enqueue(new Callback<RandomRecipeApiResponse>() {
            @Override
            public void onResponse(Call<RandomRecipeApiResponse> call, Response<RandomRecipeApiResponse> response) {
                System.out.println("API Response Code: " + response.code());

                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    System.out.println("API Error: " + response.message());
                    return;
                }

                if (response.body() != null && response.body().recipes != null && !response.body().recipes.isEmpty()) {
                    System.out.println("API Response Body: " + response.body().recipes.size() + " recipes fetched");
                    cache.setCachedRecipes(response.body());
                    listener.didFetch(response.body(), response.message());
                    System.out.println("Recipes fetched and cached");
                } else {
                    System.out.println("API Response Body is null or empty");
                    listener.didError("No recipes found in the response");
                }
            }

            @Override
            public void onFailure(Call<RandomRecipeApiResponse> call, Throwable throwable) {
                listener.didError(throwable.getMessage());
                System.out.println("API Failure: " + throwable.getMessage());
            }
        });
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
