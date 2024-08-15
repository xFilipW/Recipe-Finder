package com.example.recipefinder.main.models;

public class CategoriesData {

    private final String title;
    private final int image;

    public CategoriesData(String title, int image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public int getImage() {
        return image;
    }
}
