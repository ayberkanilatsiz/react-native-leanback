package com.rs.leanbacknative.managers;

import androidx.annotation.NonNull;
import androidx.leanback.app.VerticalGridFragment;

import com.facebook.react.uimanager.ThemedReactContext;
import com.rs.leanbacknative.layouts.LeanbackCategoryLayout;
import com.rs.leanbacknative.managers.CategoryGridManager;

public class CategoryManager extends CategoryGridManager {
    private static final String REACT_CLASS = "LeanBackCategory";

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @NonNull
    @Override
    public LeanbackCategoryLayout createViewInstance(ThemedReactContext context) {
        VerticalGridFragment gridFragment = new VerticalGridFragment();
        LeanbackCategoryLayout leanbackCategoryLayout = new LeanbackCategoryLayout(context, gridFragment, 1);

        addView(leanbackCategoryLayout, gridFragment.getView(), 0);

        return leanbackCategoryLayout;
    }
}
