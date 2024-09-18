package com.example.recipefinder.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipe")
public class RecipeTable {

    public RecipeTable(long id, String json, String title, String dishTypes, String image) {
        this.id = id;
        this.json = json;
        this.title = title;
        this.dishTypes = dishTypes;
        this.image = image;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "json")
    private String json;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "dishTypes")
    private String dishTypes;

    @ColumnInfo(name = "image")
    private String image;

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

    public String getDishTypes() {
        return dishTypes;
    }

    public void setDishTypes(String dishTypes) {
        this.dishTypes = dishTypes;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
