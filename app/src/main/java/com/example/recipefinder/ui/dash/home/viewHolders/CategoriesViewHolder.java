package com.example.recipefinder.ui.dash.home.viewHolders;

import androidx.recyclerview.widget.RecyclerView;

import com.example.recipefinder.databinding.ListItemCategoriesBinding;

public class CategoriesViewHolder extends RecyclerView.ViewHolder {

    public final ListItemCategoriesBinding binding;

    public CategoriesViewHolder(ListItemCategoriesBinding itemView) {
        super(itemView.getRoot());
        binding = itemView;
    }
}
