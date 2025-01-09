package com.example.recipefinder.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipeDetails")
public class RecipeDetailsTable {

    public RecipeDetailsTable(long id, long recipeId, String json, String title, String image,
                              int favourite, String ingredients, String stepByStep, String nutritionValue, String tags) {
        this.id = id;
        this.recipeId = recipeId;
        this.json = json;
        this.title = title;
        this.ingredients = ingredients;
        this.stepByStep = stepByStep;
        this.nutritionValue = nutritionValue;
        this.tags = tags;
        this.image = image;
        this.favourite = favourite;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "recipeId")
    private long recipeId;

    @ColumnInfo(name = "json")
    private String json;

    @ColumnInfo(name = "title")
    private String title;

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

    @ColumnInfo(name = "favourite", defaultValue = "0")
    private int favourite;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
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

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }
}
