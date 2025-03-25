package com.example.recipefinder.database;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecipeTableDao {

    @Query("SELECT * FROM recipe where recipe.id = :id")
    LiveData<List<RecipeTable>> querySingleLive(long id);

    @Insert
    Long[] insertRecipes(RecipeTable... RecipeTables);

    @Update(entity = RecipeTable.class)
    int update(RecipeTableUpdate RecipeTableUpdate);

    @Query("DELETE FROM recipe")
    int deleteRecipes();

    @Query("SELECT * FROM recipe WHERE recipe.title LIKE '%' || :phrase || '%' ORDER BY recipe.title")
    List<RecipeTable> queryRecipesByTitle(String phrase);

    @Query("SELECT * FROM recipe WHERE recipe.title LIKE '%' || :phrase || '%' AND recipe.dishTypes LIKE '%' || :category || '%' ORDER BY recipe.title")
    List<RecipeTable> queryRecipesByPhraseAndCategory(@NonNull String phrase, @NonNull String category);

    @Query("SELECT * FROM recipe ORDER BY recipe.title")
    List<RecipeTable> queryRecipes();

    @Query("INSERT OR IGNORE INTO recipeDetails (recipeId, title, ingredients, stepByStep, nutritionValue, tags, image, dishTypes, favorite) " +
            "VALUES (:recipeId, :title, :ingredients, :stepByStep, :nutritionValue, :tags, :image, :dishTypes, :favorite)")
    void insertRecipeDetailsIfNotExists(
            long recipeId,
            String title,
            String ingredients,
            String stepByStep,
            String nutritionValue,
            String tags,
            String image,
            String dishTypes,
            int favorite
    );

    @Query("SELECT favorite FROM recipeDetails WHERE recipeId = :id")
    int isFavorite(long id);

    @Query("UPDATE recipeDetails SET favorite = :value WHERE recipeId = :id")
    void setFavorite(long id, int value);

    @Query("DELETE FROM recipeDetails WHERE recipeId = :id")
    int deleteFavorite(long id);

    @Query("SELECT * FROM recipeDetails WHERE favorite = 1")
    List<RecipeTable> getFavoriteRecipes();

    @Entity
    class RecipeTableUpdate {
        @ColumnInfo(name = "id")
        public long id;
        @ColumnInfo(name = "title")
        public String title;
    }
}
