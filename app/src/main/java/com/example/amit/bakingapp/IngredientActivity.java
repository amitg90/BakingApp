package com.example.amit.bakingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class IngredientActivity extends Activity {

    public IngredientAdapter ingredientAdapter = null;
    public static RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);

        RecipeInfo recipeInfo;

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        int recipe_id = intent.getIntExtra(MainActivity.RECIPE_INDEX_STR, -1);

        if (recipe_id == -1) {
            Toast.makeText(this, "Invalid Position entered", Toast.LENGTH_LONG).show();
            return;
        }

        recipeInfo = RecipeDb.recipeInfoArrayList.get((recipe_id - 1));
        Log.e("Amit", "Detail Recipt triggered:" + recipeInfo.name);

        ingredientAdapter = new IngredientAdapter(recyclerView, this, recipeInfo.ingredients);
        recyclerView = findViewById(R.id.ingredient_rv_numbers);
        recyclerView.setAdapter(ingredientAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("MovieApp", "!!!Saving Position:");
    }
}
