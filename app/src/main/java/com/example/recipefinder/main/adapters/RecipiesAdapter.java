package com.example.recipefinder.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipefinder.R;
import com.example.recipefinder.api.models.Recipe;
import com.example.recipefinder.databinding.ListItemMainRecipesBinding;
import com.example.recipefinder.main.viewHolders.RecipiesViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipiesAdapter extends RecyclerView.Adapter<RecipiesViewHolder> {

    Context context;
    List<Recipe> recipeList;

    public RecipiesAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipiesViewHolder(ListItemMainRecipesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipiesViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        String title = truncateTitle(recipe.title);

        holder.biding.tvTitle.setText(title);
        holder.biding.tvLikes.setText(recipe.aggregateLikes + " Likes");
        holder.biding.tvServings.setText(recipe.servings + " Servings");
        holder.biding.tvMinutes.setText(recipe.readyInMinutes + " Minutes");

        if (recipe.image != null && !recipe.image.isEmpty()) {
            Picasso.get()
                    .load(recipe.image)
                    .error(R.drawable.png_image_not_found)
                    .into(holder.biding.ivRecipeImage);
        } else {
            Picasso.get()
                    .load(R.drawable.png_image_not_found)
                    .into(holder.biding.ivRecipeImage);
        }
    }

    private String truncateTitle(String title) {
        if (title.length() > 30) {
            return title.substring(0, 30 - 3) + "...";
        } else {
            return title;
        }
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }
}