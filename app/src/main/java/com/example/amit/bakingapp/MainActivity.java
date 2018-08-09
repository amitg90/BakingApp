package com.example.amit.bakingapp;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CustomGridItemClick {

    public RecipeAdapter recipeAdapter = null;
    public static RecyclerView recyclerView;
    public static String RECIPE_INDEX_STR = "recipe_index";
    public static String RECIPE_INGREDIENT_STR = "recipe_ingredients";

    public boolean sharedDataExist() {
        SharedPreferences sharedPreferences = getSharedPreferences(RecipeDetail.SHARED_FILE, Context.MODE_PRIVATE);
        String str = sharedPreferences.getString(MainActivity.RECIPE_INGREDIENT_STR, null);

        if (str == null) {
            return (false);
        }

        return (true);
    }

    public void updateWidgetDefault() {

        if (sharedDataExist() == true) {
            Log.i("updateWidgetDefault", "!!Default Exist:");
            return;
        }

        if (RecipeDb.recipeInfoArrayList.size() == 0) {
            Log.e("updateWidgetDefault", "!!NO DATA AVAILABLE, Looks like No network connectivity");
            return;
        }

        RecipeInfo recipeInfo = RecipeDb.recipeInfoArrayList.get(0);

        List<String> list = new ArrayList<String>();

        // store current recipe ID in shared preference and then update widget
        SharedPreferences sharedPreferences = getSharedPreferences(RecipeDetail.SHARED_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        for (int i = 0; i< recipeInfo.ingredients.size();i++) {
            list.add(recipeInfo.ingredients.get(i).ingredient);
        }

        // This can be any object. Does not have to be an arraylist.
        String json = gson.toJson(list);

        editor.putString(MainActivity.RECIPE_INGREDIENT_STR, json);
        editor.apply();

        Log.e("updateWidgetDefault", "!!Trigger Updating Widgets:");

    }

    public void postRecipeAsyncTaskDone() {
        GridLayoutManager gridLayoutManager;
        RecipeInfo recipeInfo;

        for (int i = 0; i < RecipeDb.recipeInfoArrayList.size(); i++) {
            Log.e("MainActivity", RecipeDb.recipeInfoArrayList.get(i).name);
            recipeInfo = RecipeDb.recipeInfoArrayList.get(i);

            for (int j = 0; j < recipeInfo.steps.size(); j++) {
                Log.e("MainActivity", " " + recipeInfo.steps.get(j).description);
            }
        }

        Log.e("Amit", "postRecipeAsyncTaskDone called " + String.valueOf(RecipeDb.recipeInfoArrayList.size()));
        recipeAdapter = new RecipeAdapter(recyclerView, this, this, RecipeDb.recipeInfoArrayList);
        recyclerView = findViewById(R.id.rv_numbers);
        recyclerView.setAdapter(recipeAdapter);
        gridLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.grid_columns));

        // update default widget if needed
        updateWidgetDefault();

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
    public void onItemClick(View view, RecipeInfo recipeInfo, int position, long lastPlayerPosition) {
        // start recipe detail activity
        Intent intent = new Intent(this, RecipeDetail.class);
        intent.putExtra(RECIPE_INDEX_STR, recipeInfo.id); //Optional parameters
        startActivity(intent);
    }
}
