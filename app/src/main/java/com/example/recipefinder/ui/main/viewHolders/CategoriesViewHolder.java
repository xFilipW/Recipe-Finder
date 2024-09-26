package com.example.recipefinder.ui.main.viewHolders;

import androidx.recyclerview.widget.RecyclerView;
import com.example.recipefinder.databinding.ListItemMainCategoriesBinding;

public class CategoriesViewHolder extends RecyclerView.ViewHolder {

    public final ListItemMainCategoriesBinding binding;

    public CategoriesViewHolder(ListItemMainCategoriesBinding itemView) {
        super(itemView.getRoot());
        binding = itemView;
    }
}
