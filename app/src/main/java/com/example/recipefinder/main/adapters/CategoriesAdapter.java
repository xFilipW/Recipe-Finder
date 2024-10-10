package com.example.recipefinder.main.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipefinder.databinding.ListItemMainCategoriesBinding;
import com.example.recipefinder.main.models.CategoryData;
import com.example.recipefinder.main.viewHolders.CategoriesViewHolder;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesViewHolder> {

    private final List<CategoryData> categoryDataList = new ArrayList<>();
    private final OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(String category);
    }

    public CategoriesAdapter(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<CategoryData> data) {
        if(data != null) {
            categoryDataList.clear();
            categoryDataList.addAll(data);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoriesViewHolder(ListItemMainCategoriesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        CategoryData category = categoryDataList.get(position);
        holder.binding.tvTitle.setText(category.getTitle());
        holder.binding.ivImage.setImageResource(category.getImage());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCategoryClick(category.getTitle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryDataList.size();
    }
}
