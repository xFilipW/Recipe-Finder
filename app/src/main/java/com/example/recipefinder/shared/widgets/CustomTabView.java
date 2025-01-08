package com.example.recipefinder.shared.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.recipefinder.R;
import com.example.recipefinder.ui.dash.shoppinglist.listeners.OnTabSelected;

public class CustomTabView extends ConstraintLayout {

    private LinearLayout tab1, tab2;
    private TextView tab1Title, tab2Title;
    private View tab1Underline, tab2Underline;
    private OnTabSelected onTabSelectedListener;

    public CustomTabView(Context context) {
        super(context);
        init(context);
    }

    public CustomTabView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomTabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widget_custom_tab_view, this, true);

        tab1 = findViewById(R.id.tab1);
        tab2 = findViewById(R.id.tab2);
        tab1Title = findViewById(R.id.tab1Title);
        tab2Title = findViewById(R.id.tab2Title);
        tab1Underline = findViewById(R.id.tab1Underline);
        tab2Underline = findViewById(R.id.tab2Underline);

        tab1.setOnClickListener(v -> selectTab(0));
        tab2.setOnClickListener(v -> selectTab(1));
    }

    public void selectTab(int tabNumber) {
        if (tabNumber == 0) {
            tab1Underline.setVisibility(View.VISIBLE);
            tab2Underline.setVisibility(View.INVISIBLE);

            tab1Title.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            tab2Title.setTextColor(ContextCompat.getColor(getContext(), R.color.grey));

            if (onTabSelectedListener != null) {
                onTabSelectedListener.onTabSelected(tabNumber);
            }
        } else if (tabNumber == 1) {
            tab1Underline.setVisibility(View.INVISIBLE);
            tab2Underline.setVisibility(View.VISIBLE);

            tab1Title.setTextColor(ContextCompat.getColor(getContext(), R.color.grey));
            tab2Title.setTextColor(ContextCompat.getColor(getContext(), R.color.black));

            if (onTabSelectedListener != null) {
                onTabSelectedListener.onTabSelected(tabNumber);
            }
        }
    }

    public void setOnTabSelected(OnTabSelected onTabSelected) {
        this.onTabSelectedListener = onTabSelected;
    }
}