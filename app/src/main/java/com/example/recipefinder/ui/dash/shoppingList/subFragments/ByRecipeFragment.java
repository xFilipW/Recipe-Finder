package com.example.recipefinder.ui.dash.shoppingList.subFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.recipefinder.databinding.FragmentByRecipeBinding;
import com.example.recipefinder.ui.dash.shoppingList.listeners.ShoppingListListener;

public class ByRecipeFragment extends Fragment implements ShoppingListListener {

    private FragmentByRecipeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentByRecipeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public int getShoppingListCount() {
        return 0;
    }
}
