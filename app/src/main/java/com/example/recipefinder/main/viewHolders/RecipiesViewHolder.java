package com.example.recipefinder.main.viewHolders;

import androidx.recyclerview.widget.RecyclerView;

import com.example.recipefinder.databinding.ListItemMainRecipesBinding;

public class RecipiesViewHolder extends RecyclerView.ViewHolder {

    public final ListItemMainRecipesBinding biding;

    public RecipiesViewHolder(ListItemMainRecipesBinding itemView) {
        super(itemView.getRoot());
        biding = itemView;
    }
}
