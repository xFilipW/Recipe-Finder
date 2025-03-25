package com.example.recipefinder.api;

import static com.example.recipefinder.api.cache.DatabaseUseCase.PREFS_NAME;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.recipefinder.R;
import com.example.recipefinder.api.cache.DatabaseUseCase;
import com.example.recipefinder.api.cache.OnQueryCompleteListener;
import com.example.recipefinder.api.models.RandomRecipesApiResponse;
import com.example.recipefinder.api.models.RecipeDetailsApiResponse;
import com.example.recipefinder.api.models.RecipeDetailsItem;
import com.example.recipefinder.database.AppDatabase;
import com.example.recipefinder.database.RecipeTable;
import com.example.recipefinder.shared.listeners.RandomRecipesResponseListener;
import com.example.recipefinder.shared.listeners.RecipeDetailsResponseListener;
import com.example.recipefinder.shared.utils.PixelUtils;
import com.example.recipefinder.shared.utils.RecipeUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class RepositoryUseCase {
    private static final String FETCH_RECIPES_COUNT = "100";
    public static final String ALL_RECIPES = "All recipes";

    private final Context context;
    private final Retrofit retrofit;
    private final DatabaseUseCase databaseUseCase;

    private static final String TAG = "RequestManager";

    /**
     * @noinspection MismatchedQueryAndUpdateOfCollection
     * Created for strong reference.
     */
    private final List<Target> picassoTargets = new ArrayList<>();
    private final ExecutorService picassoExecutor = Executors.newSingleThreadExecutor();
    private final Handler picassoMainHandler = new Handler(Looper.getMainLooper());

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

    public void getRecipes(RandomRecipesResponseListener listener, String category) {
        Log.d(TAG, "getRandomRecipes: fetching recipes, category=[" + category + "]");
        queryRecipes(databaseRecipes -> {
            Log.d(TAG, "getRandomRecipes: cached recipes size: " + databaseRecipes.size());

            if (databaseUseCase.isCacheExpired()) {
                Log.d(TAG, "getRandomRecipes: cache expired, fetching from API");
                fetchRandomRecipesFromApi(category, listener);
            } else if (databaseRecipes.isEmpty()) {
                Log.d(TAG, "getRandomRecipes: cache empty, fetching from API");
                fetchRandomRecipesFromApi(category, listener);
            } else {
                Log.d(TAG, "getRandomRecipes: cache not expired, fetching from cache");
                if (category == null || category.equals(ALL_RECIPES)) {
                    listener.onComplete(databaseRecipes);
                } else {
                    listener.onComplete(filterRecipesByCategory(databaseRecipes, category));
                }
            }
        });
    }

    public void getRecipeDetails(long id, RecipeDetailsResponseListener listener) {
        fetchRecipeDetailsFromApi(id, listener);
    }

    public void queryRecipes(OnQueryCompleteListener<List<RecipeTable>> listener) {
        databaseUseCase.queryRecipes(listener);
    }

    public void queryRecipesByPhraseAndCategory(@NonNull String phrase, @NonNull String category, OnQueryCompleteListener<List<RecipeTable>> listener) {
        databaseUseCase.queryRecipesByPhraseAndCategory(phrase, category, listener);
    }

    private void fetchRandomRecipesFromApi(String category, RandomRecipesResponseListener listener) {
        callGetRandomRecipes(category, new Callback<RandomRecipesApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<RandomRecipesApiResponse> call, @NonNull Response<RandomRecipesApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<RecipeDetailsItem> allRecipes = new ArrayList<>();
                    List<RecipeDetailsItem> filteredRecipes = new ArrayList<>();

                    if (response.body().recipes != null) {
                        allRecipes.addAll(response.body().recipes);
                    }

                    // Thread-safe incrementation!
                    AtomicInteger processedCount = new AtomicInteger(0);

                    for (RecipeDetailsItem recipe : allRecipes) {
                        Target target = new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                picassoExecutor.execute(() -> {
                                    boolean hasWhiteFrame = PixelUtils.hasWhiteFrame(bitmap);

                                    picassoMainHandler.post(() -> {
                                        if (!hasWhiteFrame) {
                                            Log.d(TAG, "onBitmapLoaded: recipe does not have white frame, adding: " + recipe.getImage());
                                            filteredRecipes.add(recipe);
                                        } else {
                                            Log.w(TAG, "onBitmapLoaded: recipe has white frame, skipping: " + recipe.getImage());
                                        }
                                        checkIfAllProcessed();
                                    });
                                });
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                checkIfAllProcessed();
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                                // left blank intentionally
                            }

                            private void checkIfAllProcessed() {
                                if (processedCount.incrementAndGet() == allRecipes.size()) {
                                    picassoTargets.clear();
                                    
                                    queryRemoveRecipes(onQueryComplete -> {
                                        queryInsertRecipes(filteredRecipes, listener);
                                    });
                                }
                            }
                        };

                        picassoTargets.add(target);

                        Picasso.get()
                                .load(recipe.getImage())
                                .config(Bitmap.Config.ARGB_8888)
                                .into(target);
                    }
                } else {
                    listener.onError("Response body or response body recipes is null");
                }
            }

            @Override
            public void onFailure(@NonNull Call<RandomRecipesApiResponse> call, @NonNull Throwable throwable) {
                listener.onError(throwable.getMessage());
            }
        });
    }

    // Helper method to determine if a pixel is considered "white" with a small tolerance.

    private void fetchRecipeDetailsFromApi(long id, RecipeDetailsResponseListener listener) {
        callGetRecipeDetails(id, new Callback<RecipeDetailsApiResponse>() {
            @Override
            public void onResponse(Call<RecipeDetailsApiResponse> call, Response<RecipeDetailsApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RecipeDetailsItem body = response.body();
                    listener.onComplete(body);
                } else {
                    listener.onError("Response body or response body recipes is null");
                }
            }

            @Override
            public void onFailure(Call<RecipeDetailsApiResponse> call, Throwable throwable) {
                listener.onError(throwable.getMessage());
            }
        });
    }

    private void queryRemoveRecipes(OnQueryCompleteListener<Integer> listener) {
        databaseUseCase.queryRemoveRecipes(listener);
    }

    private void queryInsertRecipes(List<RecipeDetailsItem> recipes, RandomRecipesResponseListener listener) {
        databaseUseCase.queryInsertRecipes(RecipeUtils.mapList(recipes), longs -> {
            databaseUseCase.saveLastUpdate();
            listener.onComplete(RecipeUtils.mapList(recipes));
        });
    }

    private void callGetRandomRecipes(String category, Callback<RandomRecipesApiResponse> callback) {
        RecipesAPI recipesAPI = retrofit.create(RecipesAPI.class);
        Call<RandomRecipesApiResponse> call = recipesAPI.getRecipesRandom(
                context.getString(R.string.api_key),
                FETCH_RECIPES_COUNT,
                category != null ? category : ""
        );

        call.enqueue(callback);
    }

    private void callGetRecipeDetails(long id, Callback<RecipeDetailsApiResponse> callback) {
        RecipesAPI recipesAPI = retrofit.create(RecipesAPI.class);
        Call<RecipeDetailsApiResponse> call = recipesAPI.getRecipeDetails(
                String.valueOf(id), context.getString(R.string.api_key)
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

    public void addRecipeToRecipeDetailsIfNotExists(RecipeDetailsItem currentRecipeDetailsItem, OnQueryCompleteListener<Void> listener) {
        databaseUseCase.insertRecipeToDetailsIfNotExists(currentRecipeDetailsItem, new OnQueryCompleteListener<Void>() {
            @Override
            public void onComplete(Void data) {
                listener.onComplete(data);
            }
        });
    }

    public void isFavorite(long id, OnQueryCompleteListener<Integer> onQueryCompleteListener) {
        databaseUseCase.isRecipeFavorite(id, new OnQueryCompleteListener<Integer>() {
            @Override
            public void onComplete(Integer data) {
                onQueryCompleteListener.onComplete(data);
            }
        });
    }

    public void setFavorite(long id, int isFavorite, OnQueryCompleteListener<Integer> onQueryCompleteListener) {
        databaseUseCase.setRecipeFavorite(id, isFavorite, new OnQueryCompleteListener<Integer>() {
            @Override
            public void onComplete(Integer data) {
                onQueryCompleteListener.onComplete(data);
            }
        });
    }

    public void getFavoriteRecipes(OnQueryCompleteListener<List<RecipeTable>> listOnQueryCompleteListener) {
        databaseUseCase.getFavoriteRecipes(listOnQueryCompleteListener);
    }

    private interface RecipesAPI {
        @GET("recipes/random")
        Call<RandomRecipesApiResponse> getRecipesRandom(
                @Query("apiKey") String apiKey,
                @Query("number") String number,
                @Query("tags") String tags
        );

        @GET("recipes/{id}/information")
        Call<RecipeDetailsApiResponse> getRecipeDetails(
                @Path("id") String id,
                @Query("apiKey") String apiKey
        );
    }
}
