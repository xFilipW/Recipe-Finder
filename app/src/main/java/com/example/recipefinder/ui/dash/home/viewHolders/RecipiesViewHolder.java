package com.example.recipefinder.ui.dash.home.viewHolders;

import androidx.recyclerview.widget.RecyclerView;

import com.example.recipefinder.databinding.ListItemRecipesBinding;

public class RecipiesViewHolder extends RecyclerView.ViewHolder {

    public final ListItemRecipesBinding biding;

    public RecipiesViewHolder(ListItemRecipesBinding itemView) {
        super(itemView.getRoot());
        biding = itemView;
    }
}
