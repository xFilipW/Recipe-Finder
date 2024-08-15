package com.example.recipefinder.main;

import com.example.recipefinder.R;
import com.example.recipefinder.main.models.CategoriesData;

import java.util.Arrays;
import java.util.List;

public class Repository {

    public final static List<CategoriesData> CATEGORIES_DATA_LIST =
            Arrays.asList(
                    new CategoriesData("All recipes", R.drawable.ic_all_recipes),
                    new CategoriesData("Main dish", R.drawable.ic_main_dish),
                    new CategoriesData("Breakfast", R.drawable.ic_breakfast),
                    new CategoriesData("Appetizer", R.drawable.ic_appetizer),
                    new CategoriesData("Dessert", R.drawable.ic_dessert),
                    new CategoriesData("Salad", R.drawable.ic_salad),
                    new CategoriesData("Bread", R.drawable.ic_bread),
                    new CategoriesData("Soup", R.drawable.ic_soup),
                    new CategoriesData("Sauce", R.drawable.ic_sauce),
                    new CategoriesData("Snack", R.drawable.ic_snack),
                    new CategoriesData("Drink", R.drawable.ic_drink)
            );
}
