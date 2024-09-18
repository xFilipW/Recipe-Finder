package com.example.recipefinder.main.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipefinder.R;
import com.example.recipefinder.api.models.Recipe;
import com.example.recipefinder.database.RecipeTable;
import com.example.recipefinder.databinding.ListItemMainRecipesBinding;
import com.example.recipefinder.main.viewHolders.RecipiesViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipiesAdapter extends RecyclerView.Adapter<RecipiesViewHolder> {

    private final List<RecipeTable> recipeList = new ArrayList<RecipeTable>();

    @NonNull
    @Override
    public RecipiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemMainRecipesBinding binding = ListItemMainRecipesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RecipiesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipiesViewHolder holder, int position) {
        RecipeTable recipe = recipeList.get(position);
        holder.biding.tvTitle.setText(truncateTitle(recipe.getTitle()));
        loadImage(holder.biding.ivRecipeImage, recipe.getImage());
    }

    private void loadImage(ImageView imageView, String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get()
                    .load(imageUrl)
                    .error(R.drawable.png_image_not_found)
                    .into(imageView);
        } else {
            Picasso.get()
                    .load(R.drawable.png_image_not_found)
                    .into(imageView);
        }
    }

    private String truncateTitle(String title) {
        return title.length() > 25 ? title.substring(0, 20) + "..." : title;
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public void setData(List<RecipeTable> recipes) {
        if (recipes != null) {
            recipeList.clear();
            recipeList.addAll(recipes);
            notifyDataSetChanged();
        }
    }
}
