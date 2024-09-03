package com.example.recipefinder.main.fragments.itemDecorators;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class HorizontalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;

    public HorizontalSpaceItemDecoration(int spanCount, int spacingInDp, Context context) {
        this.spanCount = spanCount;
        this.spacing = Math.round(spacingInDp * context.getResources().getDisplayMetrics().density);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);

        if (spanCount == 2) {
            int column = position % spanCount;

            if (column == 0) {
                outRect.right = spacing / 2;
            } else {
                outRect.left = spacing / 2;
            }
        } else {
            int totalItems = state.getItemCount();
            int totalCols = (totalItems + spanCount - 1);
            int column = position;

            if (column == 0) {
                outRect.right = spacing / 2;
            } else if (column == totalCols - 1) {
                outRect.left = spacing / 2;
            } else {
                outRect.left = spacing / 2;
                outRect.right = spacing / 2;
            }
        }
    }
}
