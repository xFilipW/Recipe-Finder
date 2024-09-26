package com.example.recipefinder.ui.main.models;

public class CategoryData {

    private final String title;
    private final int image;

    public CategoryData(String title, int image) {
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
