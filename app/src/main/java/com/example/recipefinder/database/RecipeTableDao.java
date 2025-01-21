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

    @Query("INSERT INTO recipeDetails (id, recipeId, title, ingredients, stepByStep, nutritionValue, tags, image, favorite) " +
            "VALUES (:recipeId, :title, :ingredients, :stepByStep, :nutritionValue, :tags, :image, :favorite)")
    void insertRecipeDetails(
            long recipeId,
            String title,
            String ingredients,
            String stepByStep,
            String nutritionValue,
            String tags,
            String image,
            int favorite
    );

    @Entity
    class RecipeTableUpdate {
        @ColumnInfo(name = "id")
        public long id;
        @ColumnInfo(name = "title")
        public String title;
    }
}
