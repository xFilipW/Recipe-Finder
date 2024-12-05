package com.example.recipefinder.ui.main.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipefinder.R;
import com.example.recipefinder.database.RecipeTable;
import com.example.recipefinder.databinding.ListItemMainRecipesBinding;
import com.example.recipefinder.ui.main.listeners.OnItemClickListenerEx;
import com.example.recipefinder.ui.main.viewHolders.RecipiesViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class RecipiesAdapter extends RecyclerView.Adapter<RecipiesViewHolder> {

    private final List<RecipeTable> recipeList = new ArrayList<>();
    private static final String TAG = "RecipiesAdapter";
    private final Predicate<RecipeTable> notNullNorEmpty = recipe -> {
        boolean hasImage = recipe.getImage() != null && !recipe.getImage().isEmpty();
        if (!hasImage) {
            Log.d(TAG, "Recipe with null or empty image: " + recipe.getTitle());
        }
        return hasImage;
    };
    private final OnItemClickListenerEx onItemClickListener;

    public RecipiesAdapter(OnItemClickListenerEx onItemClickListenerEx) {
        this.onItemClickListener = onItemClickListenerEx;
    }

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
        holder.biding.recipeCard.setClickable(true);
        holder.biding.recipeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(recipe.getId());
            }
        });
    }

    /**
     * Load image into imageView using Picasso.
     * If imageUrl is empty, load default image.
     *
     * @param imageView imageView to load image into
     * @param imageUrl  imageUrl to loadImage
     */
    private void loadImage(ImageView imageView, @NonNull String imageUrl) {
        imageUrl = imageUrl.isEmpty() ? null : imageUrl;

        Picasso.get()
                .load(imageUrl)
                .error(R.drawable.png_image_not_found)
                .into(imageView);
    }

    private String truncateTitle(String title) {
        return title.length() > 25 ? title.substring(0, 20) + "..." : title;
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    /**
     * Set data to adapter. If data is null, do nothing.
     * If data is not null, clear the current data and add new data (not null nor empty).
     * Notify the adapter that data has been changed.
     *
     * @param recipes
     */
    public void setData(List<RecipeTable> recipes) {
        if (recipes != null) {
            recipeList.clear();

            recipes.stream()
                    .filter(notNullNorEmpty)
                    .forEach(recipeList::add);

            //notifyItemRangeChanged(0, recipeList.size());
            notifyDataSetChanged();
        }
    }
}
