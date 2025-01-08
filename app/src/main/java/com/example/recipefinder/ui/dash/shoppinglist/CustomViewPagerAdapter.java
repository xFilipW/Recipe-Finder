package com.example.recipefinder.ui.dash.shoppinglist;

import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.recipefinder.ui.dash.shoppinglist.subfragments.ByCategoryFragment;
import com.example.recipefinder.ui.dash.shoppinglist.subfragments.ByRecipeFragment;

class CustomViewPagerAdapter extends FragmentStateAdapter {
    private final SparseArray<Fragment> fragmentMap = new SparseArray<>();

    public static final int ITEM_0_CATEGORY = 0;
    public static final int ITEM_1_RECIPE = 1;

    public CustomViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;

        switch (position) {
            case ITEM_0_CATEGORY:
                fragment = new ByCategoryFragment();
                break;
            case ITEM_1_RECIPE:
                fragment = new ByRecipeFragment();
                break;
        }

        fragmentMap.put(position, fragment);
        return fragment;
    }

    public Fragment getFragmentAtPosition(int position) {
        return fragmentMap.get(position);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
