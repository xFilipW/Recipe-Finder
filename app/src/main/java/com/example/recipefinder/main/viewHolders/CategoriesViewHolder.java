package com.example.recipefinder.main.viewHolders;

import androidx.recyclerview.widget.RecyclerView;
import com.example.recipefinder.databinding.ListItemMainCategoriesBinding;

public class CategoriesViewHolder extends RecyclerView.ViewHolder {

    public final ListItemMainCategoriesBinding biding;

    public CategoriesViewHolder(ListItemMainCategoriesBinding itemView) {
        super(itemView.getRoot());
        biding = itemView;
    }
}
