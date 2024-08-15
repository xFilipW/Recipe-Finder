package com.example.recipefinder.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipefinder.R;
import com.example.recipefinder.api.models.Recipe;
import com.example.recipefinder.databinding.ListItemMainRecipesBinding;
import com.example.recipefinder.main.viewHolders.RecipiesViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipiesAdapter extends RecyclerView.Adapter<RecipiesViewHolder> {

    private final Context context;
    private final List<Recipe> recipeList;

    public RecipiesAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemMainRecipesBinding binding = ListItemMainRecipesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RecipiesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipiesViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.biding.tvTitle.setText(truncateTitle(recipe.title));
        holder.biding.tvLikes.setText(String.format("%d Likes", recipe.aggregateLikes));
        holder.biding.tvServings.setText(String.format("%d Servings", recipe.servings));
        holder.biding.tvMinutes.setText(String.format("%d Minutes", recipe.readyInMinutes));

        loadImage(holder.biding.ivRecipeImage, recipe.image);
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
        return title.length() > 30 ? title.substring(0, 27) + "..." : title;
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }
}
