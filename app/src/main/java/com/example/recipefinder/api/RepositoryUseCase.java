package com.example.recipefinder.api;

import static com.example.recipefinder.api.cache.DatabaseUseCase.PREFS_NAME;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.recipefinder.R;
import com.example.recipefinder.api.cache.DatabaseUseCase;
import com.example.recipefinder.api.cache.OnQueryCompleteListener;
import com.example.recipefinder.api.models.RandomRecipeApiResponse;
import com.example.recipefinder.api.models.Recipe;
import com.example.recipefinder.database.AppDatabase;
import com.example.recipefinder.database.RecipeTable;
import com.example.recipefinder.listeners.RandomRecipeResponseListener;
import com.example.recipefinder.utils.RecipeUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RepositoryUseCase {
    private static final String FETCH_RECIPES_COUNT = "100";
    private static final String ALL_RECIPES = "All recipes";

    private final Context context;
    private final Retrofit retrofit;
    private final DatabaseUseCase databaseUseCase;

    private static final String TAG = "RequestManager";

    public RepositoryUseCase(Context context) {
        this.context = context;
        this.databaseUseCase = new DatabaseUseCase(
                AppDatabase.getDatabase(context),
                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        );
        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void getRecipes(RandomRecipeResponseListener listener, String category) {
        Log.d(TAG, "getRandomRecipes: fetching recipes, category=[" + category + "]");
        querySelectRecipesAll(cachedRecipes -> {
            Log.d(TAG, "getRandomRecipes: cached recipes size: " + cachedRecipes.size());

            databaseUseCase.isCacheExpired(
                    expired -> {
                        if (expired) {
                            Log.d(TAG, "getRandomRecipes: cache expired, fetching from API");
                            fetchRecipesFromApi(category, listener);
                        } else if (cachedRecipes.isEmpty()) {
                            Log.d(TAG, "getRandomRecipes: cache empty, fetching from API");
                            fetchRecipesFromApi(category, listener);
                        } else {
                            Log.d(TAG, "getRandomRecipes: cache not expired, fetching from cache");
                            if (category == null || category.equals(ALL_RECIPES)) {
                                listener.onComplete(cachedRecipes);
                            } else {
                                listener.onComplete(filterRecipesByCategory(cachedRecipes, category));
                            }
                        }
                    }
            );
        });
    }

    private void querySelectRecipesAll(OnQueryCompleteListener<List<RecipeTable>> listener) {
        databaseUseCase.querySelectRecipesAll(context, listener);
    }

    public void querySelectRecipesByTitle(String phrase, OnQueryCompleteListener<List<RecipeTable>> listener) {
        databaseUseCase.querySelectRecipesByTitle(context, phrase, listener);
    }

    private void fetchRecipesFromApi(String category, RandomRecipeResponseListener listener) {
        callGetRecipesRandom(category, new Callback<RandomRecipeApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<RandomRecipeApiResponse> call, @NonNull Response<RandomRecipeApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "fetchRecipesFromApi: response successful");

                    ArrayList<Recipe> allRecipes = new ArrayList<>();
                    if (response.body().recipes != null) {
                        allRecipes.addAll(response.body().recipes);
                    }
                    Log.d(TAG, "fetchRecipesFromApi: after response, got " + allRecipes.size() + " recipes");

                    queryRemoveRecipes(data -> {
                        queryInsertRecipes(allRecipes, listener);
                    });
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

    private void queryRemoveRecipes(OnQueryCompleteListener<Integer> listener) {
        databaseUseCase.queryRemoveRecipes(listener);
    }

    private void queryInsertRecipes(List<Recipe> recipes, RandomRecipeResponseListener listener) {
        databaseUseCase.queryInsertRecipes(RecipeUtils.mapList(recipes), longs -> {
            databaseUseCase.saveLastUpdate();
            listener.onComplete(RecipeUtils.mapList(recipes));
        });
    }

    private void callGetRecipesRandom(String category, Callback<RandomRecipeApiResponse> callback) {
        CallRandomRecipes callRandomRecipes = retrofit.create(CallRandomRecipes.class);
        Call<RandomRecipeApiResponse> call = callRandomRecipes.getRecipesRandom(
                context.getString(R.string.api_key),
                FETCH_RECIPES_COUNT,
                category != null ? category : ""
        );

        call.enqueue(callback);
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
        Call<RandomRecipeApiResponse> getRecipesRandom(
                @Query("apiKey") String apiKey,
                @Query("number") String number,
                @Query("tags") String tags
        );
    }
}
