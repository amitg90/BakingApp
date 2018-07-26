package com.example.amit.bakingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class RecipeDetail extends Activity implements CustomGridItemClick {

    public RecipeDetailAdapter recipeDetailAdapter = null;
    public static RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        RecipeInfo recipeInfo;

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        int position = intent.getIntExtra(MainActivity.RECIPE_INDEX_STR, -1);

        if (position == -1) {
            Toast.makeText(this, "Invalid Position entered", Toast.LENGTH_LONG).show();
            return;
        }

        recipeInfo = RecipeDb.recipeInfoArrayList.get(position);
        Log.e("Amit", "Detail Recipt triggered:" + recipeInfo.name);

        recipeDetailAdapter = new RecipeDetailAdapter(recyclerView, this, this, recipeInfo);
        recyclerView = findViewById(R.id.detail_rv_numbers);
        recyclerView.setAdapter(recipeDetailAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelable("DETAIL_ACTIVITY_SAVED_LAYOUT_MANAGER", getLayoutManager().onSaveInstanceState());
        Log.d("MovieApp", "!!!Saving Position:");
    }

    @Override
    public void onItemClick(View view, RecipeInfo recipeInfo, int position) {
        // start activity
        if (position == 0) {
            // ingredient
            Intent intent = new Intent(this, IngredientActivity.class);
            intent.putExtra(MainActivity.RECIPE_INDEX_STR, position); //Optional parameters
            startActivity(intent);
        } else {
            // steps activity
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra(StepDetailActivity.RECIPE_ID_STR, recipeInfo.id); //Optional parameters
            intent.putExtra(StepDetailActivity.STEP_ID_STR, (position - 1)); //Optional parameters
            startActivity(intent);
        }

    }

    public void prevClick(View view) {

    }

    public void Click(View view) {

    }
}
