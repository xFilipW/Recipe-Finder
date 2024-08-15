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

    private List<CategoriesData> categoriesData = new ArrayList<>();
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(String category);
    }

    public CategoriesAdapter(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<CategoriesData> categoriesData) {
        this.categoriesData = categoriesData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoriesViewHolder(ListItemMainCategoriesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        String categoriesTitle = categoriesData.get(position).getTitle();
        holder.biding.tvTitle.setText(categoriesTitle);
        holder.biding.ivImage.setImageResource(categoriesData.get(position).getImage());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCategoryClick(categoriesTitle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriesData.size();
    }
}
