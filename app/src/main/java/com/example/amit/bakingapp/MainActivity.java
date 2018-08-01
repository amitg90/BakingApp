package com.example.amit.bakingapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements CustomGridItemClick {

    public RecipeAdapter recipeAdapter = null;
    public static RecyclerView recyclerView;
    public static String RECIPE_INDEX_STR = "recipe_index";
    public static String RECIPE_INGREDIENT_STR = "recipe_ingredients";

    public void postRecipeAsyncTaskDone() {
        GridLayoutManager gridLayoutManager;

        Log.e("Amit", "postRecipeAsyncTaskDone called " + String.valueOf(RecipeDb.recipeInfoArrayList.size()));
        recipeAdapter = new RecipeAdapter(recyclerView, this, this, RecipeDb.recipeInfoArrayList);
        recyclerView = findViewById(R.id.rv_numbers);
        recyclerView.setAdapter(recipeAdapter);
        gridLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.grid_columns));

//        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            Log.e("Amit!!", "Portrait View");
//           // gridLayoutManager = new GridLayoutManager(this,
//             //       1, GridLayoutManager.VERTICAL, false);
//            gridLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.grid_columns));
//        } else {
//            Log.e("Amit!!", "Landscape View");
//            gridLayoutManager = new GridLayoutManager(this,
//                    3, GridLayoutManager.VERTICAL, false);
//        }
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecipeInfo[] recipeInfos = {};

        // Async task to read recipe Json from network
        RecipeAsyncTask recipeAsyncTask = new RecipeAsyncTask(this);
        recipeAsyncTask.execute(recipeInfos);
    }

    @Override
    public void onItemClick(View view, RecipeInfo recipeInfo, int position) {
        // start recipe detail activity
        Intent intent = new Intent(this, RecipeDetail.class);
        intent.putExtra(RECIPE_INDEX_STR, position); //Optional parameters
        startActivity(intent);
    }
}
