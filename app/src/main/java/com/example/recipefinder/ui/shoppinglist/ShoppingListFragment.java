package com.example.recipefinder.ui.shoppinglist;

import static com.example.recipefinder.ui.shoppinglist.CustomViewPagerAdapter.ITEM_0_CATEGORY;
import static com.example.recipefinder.ui.shoppinglist.CustomViewPagerAdapter.ITEM_1_RECIPE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.recipefinder.databinding.FragmentShoppingListBinding;
import com.example.recipefinder.ui.shoppinglist.listeners.ShoppingListListener;

public class ShoppingListFragment extends Fragment {

    private FragmentShoppingListBinding binding;
    private static final String TAG = "ShoppingListFragment";
    private ViewPager2.OnPageChangeCallback onPageChangeCallback;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentShoppingListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CustomViewPagerAdapter viewPagerAdapter = new CustomViewPagerAdapter(getChildFragmentManager(), getLifecycle());
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.viewPager.setOffscreenPageLimit(1);

        binding.customTabView.setOnTabSelected(tabNumber -> {
            binding.viewPager.setCurrentItem(tabNumber, false);
        });

        onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                Fragment nextFragment = viewPagerAdapter.getFragmentAtPosition(position + 1);
                Fragment previousFragment = viewPagerAdapter.getFragmentAtPosition(position - 1);

                switch (position) {
                    case ITEM_0_CATEGORY:
                        binding.viewPager.setUserInputEnabled(nextFragment == null
                                || ((ShoppingListListener) nextFragment).getShoppingListCount() != 0);
                        break;
                    case ITEM_1_RECIPE:
                        binding.viewPager.setUserInputEnabled(previousFragment == null
                                || ((ShoppingListListener) previousFragment).getShoppingListCount() != 0);
                        break;
                }
            }
        };
        binding.viewPager.registerOnPageChangeCallback(onPageChangeCallback);

        binding.customTabView.selectTab(ITEM_0_CATEGORY);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.viewPager.unregisterOnPageChangeCallback(onPageChangeCallback);
        binding = null;
    }
}
