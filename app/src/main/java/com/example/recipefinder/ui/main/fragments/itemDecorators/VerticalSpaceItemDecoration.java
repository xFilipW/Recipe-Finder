package com.example.recipefinder.ui.main.fragments.itemDecorators;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private SpanCount spanCount;
    private int spacing;

    public enum SpanCount {
        TWO(2);

        public final int value;

        SpanCount(int value) {
            this.value = value;
        }
    }

    public VerticalSpaceItemDecoration(SpanCount spanCount, int spacingInDp, Context context) {
        this.spanCount = spanCount;
        this.spacing = Math.round(spacingInDp * context.getResources().getDisplayMetrics().density);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int row = position / spanCount.value;
        int totalItems = state.getItemCount();
        int totalRows = (totalItems + spanCount.value - 1) / spanCount.value;

        if (row > 0) {
            outRect.top = spacing;
        }

        if (row == totalRows - 1) {
            outRect.bottom = spacing;
        }
    }
}
