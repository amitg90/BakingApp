package com.example.amit.bakingapp;

import android.view.View;

public interface CustomGridItemClick {
    public void onItemClick(View view, RecipeInfo recipeInfo, int position, long lastPlayerPosition);
}
