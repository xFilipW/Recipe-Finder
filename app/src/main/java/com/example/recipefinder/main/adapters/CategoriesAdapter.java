package com.example.recipefinder.main.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipefinder.databinding.ListItemMainCategoriesBinding;
import com.example.recipefinder.main.models.CategoriesData;
import com.example.recipefinder.main.viewHolders.CategoriesViewHolder;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesViewHolder> {

    private final List<CategoriesData> categoriesData = new ArrayList<>();

    public void setData(List<CategoriesData> categoriesData) {
        this.categoriesData.clear();
        this.categoriesData.addAll(categoriesData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoriesViewHolder(ListItemMainCategoriesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        CategoriesData category = categoriesData.get(position);
        holder.binding.tvTitle.setText(category.getTitle());
        holder.binding.ivImage.setImageResource(category.getImage());
    }

    @Override
    public int getItemCount() {
        return categoriesData.size();
    }
}
