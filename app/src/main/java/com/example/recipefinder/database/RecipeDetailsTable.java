package com.example.recipefinder.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipeDetails")
public class RecipeDetailsTable {

    public RecipeDetailsTable(long id, long recipeId, String title, String image,
                              int favorite, String ingredients, String stepByStep, String nutritionValue, String tags) {
        this.id = id;
        this.recipeId = recipeId;
        this.title = title;
        this.ingredients = ingredients;
        this.stepByStep = stepByStep;
        this.nutritionValue = nutritionValue;
        this.tags = tags;
        this.image = image;
        this.favorite = favorite;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "recipeId")
    private long recipeId;

    @ColumnInfo(name = "title")
    private String title;

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getStepByStep() {
        return stepByStep;
    }

    public void setStepByStep(String stepByStep) {
        this.stepByStep = stepByStep;
    }

    public String getNutritionValue() {
        return nutritionValue;
    }

    public void setNutritionValue(String nutritionValue) {
        this.nutritionValue = nutritionValue;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDishTypes() {
        return dishTypes;
    }

    public void setDishTypes(String dishTypes) {
        this.dishTypes = dishTypes;
    }

    @ColumnInfo(name = "ingredients")
    private String ingredients;

    @ColumnInfo(name = "stepByStep")
    private String stepByStep;

    @ColumnInfo(name = "nutritionValue")
    private String nutritionValue;

    @ColumnInfo(name = "tags")
    private String tags;

    @ColumnInfo(name = "dishTypes")
    private String dishTypes;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "favorite" , defaultValue = "0")
    private int favorite;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }
}
