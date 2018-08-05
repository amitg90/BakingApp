package com.example.amit.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetail extends AppCompatActivity implements CustomGridItemClick {

    public RecipeDetailAdapter recipeDetailAdapter = null;
    public static RecyclerView recyclerView;
    public static RecipeListFragment recipeListFragment = null;
    public static RecipeDetailFragment recipeDetailFragment = null;
    public static IngredientFragment ingredientFragment = null;
    public static RecipeInfo recipeInfo = null;
    public static SharedPreferences mPrefs = null;
    public static String SHARED_FILE = "com.example.amit.bakingapp";
    public static String POSITION_STR = "position_id";
    int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        int recipe_id = intent.getIntExtra(MainActivity.RECIPE_INDEX_STR, -1);

        if (recipe_id == -1) {
            Toast.makeText(this, "Invalid Position entered", Toast.LENGTH_LONG).show();
            return;
        }

        recipeInfo = RecipeDb.recipeInfoArrayList.get((recipe_id - 1));
        setTitle(recipeInfo.name);

        FragmentManager fragmentManager = getSupportFragmentManager();//Get Fragment Manager

        // create list fragment instance
        Bundle bundle = new Bundle();
        Log.e("Amit", "Setting Recipe index:" + Integer.toString(recipeInfo.id));

        bundle.putInt(MainActivity.RECIPE_INDEX_STR, recipeInfo.id);
        recipeListFragment = new RecipeListFragment();
        recipeListFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, recipeListFragment);

        // check if detail fragment needs to be intialized
        if (getResources().getInteger(R.integer.size) == 2) {
            bundle = new Bundle();
            bundle.putInt(StepDetailActivity.RECIPE_ID_STR, recipeInfo.id);
            bundle.putInt(StepDetailActivity.STEP_ID_STR, 0);

            recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragmentContainer1, recipeDetailFragment);
        }

        fragmentTransaction.commit();
        Log.e("Amit", "Set Bundle Arguments!!:");

        int position = get_position();
        if (position != -1) {
            onItemClick(null, recipeInfo, (position + 1));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelable("DETAIL_ACTIVITY_SAVED_LAYOUT_MANAGER", getLayoutManager().onSaveInstanceState());
        Log.d("MovieApp", "!!!Saving Position:");
    }

    public void prevClick(View view) {
        Log.e("Amit", "Previous clicked");
        if (RecipeDetailFragment.step_id > 0) {
            RecipeDetailFragment.step_id--;
            store_position(RecipeDetailFragment.step_id);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();//Get Fragment Manager
        // fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        // create list fragment instance
        Bundle bundle = new Bundle();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        bundle.putInt(StepDetailActivity.RECIPE_ID_STR, recipeInfo.id);
        bundle.putInt(StepDetailActivity.STEP_ID_STR, RecipeDetailFragment.step_id);

        recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setArguments(bundle);

        // check if detail fragment needs to be intialized
        if (getResources().getInteger(R.integer.size) == 2) {
            fragmentTransaction.replace(R.id.fragmentContainer1, recipeDetailFragment);
        } else {
            fragmentTransaction.replace(R.id.fragmentContainer, recipeDetailFragment);
        }

        fragmentTransaction.commit();
    }

    public void nextClick(View view) {
        Log.e("Amit", "Next clicked:" + recipeInfo.steps.size());
        if (RecipeDetailFragment.step_id < recipeInfo.steps.size()) {
            RecipeDetailFragment.step_id++;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();//Get Fragment Manager
       // fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        // create list fragment instance
        Bundle bundle = new Bundle();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        bundle.putInt(StepDetailActivity.RECIPE_ID_STR, recipeInfo.id);
        bundle.putInt(StepDetailActivity.STEP_ID_STR, RecipeDetailFragment.step_id);
        store_position(RecipeDetailFragment.step_id);

        recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setArguments(bundle);

        // check if detail fragment needs to be intialized
        if (getResources().getInteger(R.integer.size) == 2) {
            fragmentTransaction.replace(R.id.fragmentContainer1, recipeDetailFragment);
        } else {
            fragmentTransaction.replace(R.id.fragmentContainer, recipeDetailFragment);
        }

        fragmentTransaction.commit();
    }

    public void store_position(int position) {

        if (position == -1) {
            return;
        }
        // store current recipe ID in shared preference and then update widget
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(RecipeDetail.POSITION_STR, position);
        Log.e("Amit!!!!!!!", "Storing Position:" + position);
        editor.apply();
    }

    public int get_position() {
        // store current recipe ID in shared preference and then update widget
        SharedPreferences sharedPreferences = getSharedPreferences(RecipeDetail.SHARED_FILE, MODE_PRIVATE);
        int position = sharedPreferences.getInt(RecipeDetail.POSITION_STR, -1);
        Log.e("Amit!!!!!!!", "Got Position:" + position);
        return position;
    }

    @Override
    public void onItemClick(View view, RecipeInfo recipeInfo, int position) {

        Log.e("Amit", "onItemClick called!!:");

        // start fragment
        FragmentManager fragmentManager = getSupportFragmentManager();//Get Fragment Manager

        //create fragment instance
        Bundle bundle = new Bundle();
        if (position == 0) {
            bundle.putInt(MainActivity.RECIPE_INDEX_STR, recipeInfo.id);

            ingredientFragment = new IngredientFragment();
            ingredientFragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (getResources().getInteger(R.integer.size) == 1) {
                fragmentTransaction.replace(R.id.fragmentContainer, ingredientFragment).addToBackStack(null);
            } else {
                fragmentTransaction.replace(R.id.fragmentContainer1, ingredientFragment).addToBackStack(null);
            }
            fragmentTransaction.commit();
            return;
        } else {
            this.position = position;

            bundle.putInt(StepDetailActivity.RECIPE_ID_STR, recipeInfo.id);
            bundle.putInt(StepDetailActivity.STEP_ID_STR, (position - 1));
        }

        recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // check if detail fragment needs to be intialized
        if (getResources().getInteger(R.integer.size) == 2) {
            fragmentTransaction.replace(R.id.fragmentContainer1, recipeDetailFragment).addToBackStack(null);
        } else {
            fragmentTransaction.replace(R.id.fragmentContainer, recipeDetailFragment).addToBackStack(null);
        }

        fragmentTransaction.commit();

        Log.e("Amit", "Set Bundle Arguments!!:");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        List<String> list = new ArrayList<String>();

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_to_widget:

                // store current recipe ID in shared preference and then update widget
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_FILE, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();

                for (int i = 0; i< recipeInfo.ingredients.size();i++) {
                    list.add(recipeInfo.ingredients.get(i).ingredient);
                }

                // This can be any object. Does not have to be an arraylist.
                String json = gson.toJson(list);

                editor.putString(MainActivity.RECIPE_INGREDIENT_STR, json);
                editor.apply();

                Log.e("RecipeWidgetFactory", "!!Trigger Updating Widgets:");

                // trigger widget to refresh
                updateWidgets();
                return (true);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        store_position(this.position);
    }
}
