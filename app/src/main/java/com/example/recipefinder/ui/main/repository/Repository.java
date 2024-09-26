package com.example.recipefinder.ui.main.repository;

import com.example.recipefinder.R;
import com.example.recipefinder.ui.main.models.CategoryData;

import java.util.Arrays;
import java.util.List;

public class Repository {

    public final static List<CategoryData> CATEGORIES_DATA_LIST =
            Arrays.asList(
                    new CategoryData("All recipes", R.drawable.ic_all_recipes),
                    new CategoryData("Main dish", R.drawable.ic_main_dish),
                    new CategoryData("Dessert", R.drawable.ic_dessert),
                    new CategoryData("Appetizer", R.drawable.ic_appetizer),
                    new CategoryData("Breakfast", R.drawable.ic_breakfast),
                    new CategoryData("Side dish", R.drawable.ic_side_dish),
                    new CategoryData("Salad", R.drawable.ic_salad),
                    new CategoryData("Soup", R.drawable.ic_soup),
                    new CategoryData("Bread", R.drawable.ic_bread),
                    new CategoryData("Sauce", R.drawable.ic_sauce),
                    new CategoryData("Snack", R.drawable.ic_snack),
                    new CategoryData("Drink", R.drawable.ic_drink),
                    new CategoryData("Marinade", R.drawable.ic_marinade)
            );
}
