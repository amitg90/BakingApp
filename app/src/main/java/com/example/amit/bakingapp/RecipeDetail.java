package com.example.amit.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class RecipeDetail extends FragmentActivity implements CustomGridItemClick {

    public RecipeDetailAdapter recipeDetailAdapter = null;
    public static RecyclerView recyclerView;
    public static RecipeListFragment recipeListFragment = null;
    public static RecipeDetailFragment recipeDetailFragment = null;
    public static IngredientFragment ingredientFragment = null;

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

        FragmentManager fragmentManager = getSupportFragmentManager();//Get Fragment Manager

        // create list fragment instance
        Bundle bundle = new Bundle();
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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelable("DETAIL_ACTIVITY_SAVED_LAYOUT_MANAGER", getLayoutManager().onSaveInstanceState());
        Log.d("MovieApp", "!!!Saving Position:");
    }

    public void prevClick(View view) {

    }

    public void Click(View view) {

    }

    @Override
    public void onItemClick(View view, RecipeInfo recipeInfo, int position) {

        Log.e("Amit", "onItemClick called!!:");

        // start fragment
        FragmentManager fragmentManager = getSupportFragmentManager();//Get Fragment Manager

        //create fragment instance
        Bundle bundle = new Bundle();
        if (position == 0) {
            bundle.putInt(MainActivity.RECIPE_INDEX_STR, position);

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

//        // start activity
//        if (position == 0) {
//            // ingredient
//            Intent intent = new Intent(this, IngredientActivity.class);
//            intent.putExtra(MainActivity.RECIPE_INDEX_STR, position); //Optional parameters
//            startActivity(intent);
//        } else {
//            // steps activity
//            Intent intent = new Intent(this, StepDetailActivity.class);
//            intent.putExtra(StepDetailActivity.RECIPE_ID_STR, recipeInfo.id); //Optional parameters
//            intent.putExtra(StepDetailActivity.STEP_ID_STR, (position - 1)); //Optional parameters
//            startActivity(intent);
//        }
    }
}
