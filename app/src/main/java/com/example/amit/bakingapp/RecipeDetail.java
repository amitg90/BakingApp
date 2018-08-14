package com.example.amit.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.List;

public class RecipeDetail extends AppCompatActivity implements CustomGridItemClick {

    public RecipeDetailAdapter recipeDetailAdapter = null;
    public static RecyclerView recyclerView;
    public static RecipeListFragment recipeListFragment = null;
    public static StepDetailFragment stepDetailFragment = null;
    public static IngredientFragment ingredientFragment = null;
    public static RecipeInfo recipeInfo = null;
    public static SharedPreferences mPrefs = null;
    public static String SHARED_FILE = "com.example.amit.bakingapp";
    public static String STEP_ID_POSITION_STR = "step_id";
    public static String LAST_PLAYER_POSITION_STR = "lastPlayerPosition";
    public static String RECIPE_ID_STR = "recipe_id";
    public static SharedPreferences sharedPreferences = null;

    public static long restored_lastPlayerPosition = 0;
    private int restored_recipe_id = 0;
    public static int current_step_id_position = -1;
    private int current_orientation;

    public void handleFragments() {

        Log.e("RecipeDetail", "handleFragments called");

        createRecipeListFragment();

        // check if detail fragment needs to be intialized
        if (getResources().getInteger(R.integer.size) == 2) {
            Log.e("RecipeDetail", "calling createRecipeStepDetailFragment: Tablet mode");
            createRecipeStepDetailFragment();
        }

        if (current_step_id_position > 0) {
            if (current_step_id_position <= recipeInfo.steps.size()) {
                Log.e("RecipeDetail", "Triggering onItemClick:");
                onItemClick(null, recipeInfo, current_step_id_position, restored_lastPlayerPosition);
            } else {
                Log.e("RecipeDetail", "NO Triggering FOR onItemClick:");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Log.e("RecipeDetail","onCreate Called!!!");

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        int recipe_id = intent.getIntExtra(RecipeDetail.RECIPE_ID_STR, -1);
        current_step_id_position = intent.getIntExtra(RecipeDetail.STEP_ID_POSITION_STR, -1);

        if (recipe_id == -1) {
            Toast.makeText(this, "Invalid Position entered", Toast.LENGTH_LONG).show();
            return;
        }

        Log.e("RecipeDetail", "Bundle Read position = " + current_step_id_position);

        sharedPreferences = getSharedPreferences(SHARED_FILE, MODE_PRIVATE);
        recipeInfo = RecipeDb.recipeInfoArrayList.get((recipe_id - 1));
        setTitle(recipeInfo.name);

        current_orientation = getResources().getConfiguration().orientation;

        saveStateInSharedPreference(true);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_recipe_detail);

        Log.e("RecipeDetail", "onConfigurationChanged triggered");

        if (StepDetailFragment.mExoPlayer != null) {
            Log.e("RecipeDetail", "ExoPlayer Current Position Pulled");
            restored_lastPlayerPosition = StepDetailFragment.mExoPlayer.getCurrentPosition();
        }

        if (newConfig.orientation != current_orientation) {
            current_orientation = newConfig.orientation;

            //killRecipeListFragment();
            //createRecipeListFragment();

//            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                Log.e("RecipeDetail", "Landscape");
//                // we changed orientation, remove Ingredients fragment if we are
//                // landscape and sw600
//                Log.e("RecipeDetail", "!!!!mode:" + getResources().getInteger(R.integer.size));
////                if (getResources().getInteger(R.integer.size) == 2) {
////                    // we are sw600
////                    killRecipeListFragment();
////                }
//            } else {
//                Log.e("RecipeDetail", "Portrait");
//
//                Log.e("RecipeDetail", "!!!!mode:" + getResources().getInteger(R.integer.size));
//                if (getResources().getInteger(R.integer.size) == 2) {
//                    // we are sw600
//                    createRecipeListFragment();
//                }
//            }

            // kill and restart fragments

            onItemClick(null, recipeInfo, current_step_id_position, restored_lastPlayerPosition);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void prevClick(View view) {
        Log.e("RecipeDetail", "Previous clicked");
        if (current_step_id_position > 2) {
            current_step_id_position--;
        } else {
            // nothing to do
            return;
        }

        stepDetailFragment.lastPlayerPosition = 0;

        FragmentManager fragmentManager = getSupportFragmentManager();//Get Fragment Manager
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        // create list fragment instance
        Bundle bundle = new Bundle();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        bundle.putInt(RecipeDetail.RECIPE_ID_STR, recipeInfo.id);
        bundle.putInt(RecipeDetail.STEP_ID_POSITION_STR, current_step_id_position);

        long lastPlayerPosition = 0;
        bundle.putLong(RecipeDetail.LAST_PLAYER_POSITION_STR, lastPlayerPosition);
        Log.e("lastPlayerPosition", "prevClick lastPlayerPosition:" + lastPlayerPosition);

        Log.e("RecipeDetail", "Setting New Step ID: " + current_step_id_position);

        stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setArguments(bundle);

        // check if detail fragment needs to be intialized
        if (getResources().getInteger(R.integer.size) == 2) {
            fragmentTransaction.replace(R.id.fragmentContainer1, stepDetailFragment).addToBackStack(null);
        } else {
            fragmentTransaction.replace(R.id.fragmentContainer, stepDetailFragment).addToBackStack(null);
        }

        fragmentTransaction.commit();
    }

    public void nextClick(View view) {
        Log.e("RecipeDetail", "Next clicked:" + recipeInfo.name);

        Log.e("RecipeDetail", "Current current_step_id_position:" + current_step_id_position);

        if ((current_step_id_position + 1) <= recipeInfo.steps.size()) {
            current_step_id_position++;
        } else {
            // nothing to do
            return;
        }

       // FragmentManager fragmentManager = getSupportFragmentManager();//Get Fragment Manager
        //fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        killStepDetailFragment();

        // reset lastPlayerPosition
        restored_lastPlayerPosition = 0;

        createRecipeListFragment();

        createRecipeStepDetailFragment();
    }

    public void prepareRecipeDetailBundle(Bundle bundle) {
        bundle.putInt(RecipeDetail.RECIPE_ID_STR, recipeInfo.id);
        Log.e("RecipeDetail", "Setting Recipe ID: " + recipeInfo.id);
        bundle.putInt(RecipeDetail.STEP_ID_POSITION_STR, current_step_id_position);
        Log.e("RecipeDetail", "Setting Step ID POSITION: " + current_step_id_position);
        bundle.putLong(RecipeDetail.LAST_PLAYER_POSITION_STR, restored_lastPlayerPosition);
        Log.e("RecipeDetail", "Setting lastPlayerPosition:" + restored_lastPlayerPosition);
    }

    @Override
    public void onItemClick(View view, RecipeInfo recipeInfo, int position, long lastPlayerPosition) {

        Log.e("RecipeDetail", "onItemClick called!!:" + position);

        // start fragment
        FragmentManager fragmentManager = getSupportFragmentManager();//Get Fragment Manager

        //create fragment instance
        if (position == 0) {
            createIngredientFragment();
            return;
        } else {
            current_step_id_position = position;
            createRecipeListFragment();
            createRecipeStepDetailFragment();
        }
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

                Log.e("RecipeDetail", "!!Trigger Updating Widgets:");

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

    private void saveStateInSharedPreference(boolean defaults) {
        Log.e("RecipeDetailFragment", "saveStateInSharedPreference Called");

        if (sharedPreferences != null) {
            if (defaults == true) {
                Log.e("RecipeDetail", "Saving DEFAULT State in Shared Memory");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(RecipeDetail.STEP_ID_POSITION_STR, -1);
                editor.putLong(RecipeDetail.LAST_PLAYER_POSITION_STR, 0);
                editor.putInt(RecipeDetail.RECIPE_ID_STR, -1);
                editor.apply();
            } else {
                Log.e("RecipeDetail", "Saving Current State in Shared Memory");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(RecipeDetail.STEP_ID_POSITION_STR, current_step_id_position);
                editor.putInt(RecipeDetail.RECIPE_ID_STR, recipeInfo.id);
                if (StepDetailFragment.mExoPlayer != null) {
                    Log.e("RecipeDetail", "ExoPlayer Current Position Saved");
                    editor.putLong(RecipeDetail.LAST_PLAYER_POSITION_STR, StepDetailFragment.mExoPlayer.getCurrentPosition());
                }
                editor.apply();
            }
        }
    }

    private void getStateFromSharedPreference() {
        Log.e("RecipeDetail", "getStateFromSharedPreference Called");

        if (sharedPreferences != null) {
            if (current_step_id_position < 1) {
                current_step_id_position = sharedPreferences.getInt(RecipeDetail.STEP_ID_POSITION_STR, -1);
            }
            restored_lastPlayerPosition = sharedPreferences.getLong(RecipeDetail.LAST_PLAYER_POSITION_STR, 0);
            restored_recipe_id = sharedPreferences.getInt(RecipeDetail.RECIPE_ID_STR, 0);
            Log.e("RecipeDetail", "Read Shared Preference: recipe id: " + restored_recipe_id +
                    ":lastPlayer:" + restored_lastPlayerPosition + ":stepid:" + current_step_id_position);
        }
    }

    private void killStepDetailFragment() {
        if (stepDetailFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();//Get Fragment Manager
            Log.e("RecipeDetail", "Killing Fragment RecipeListFragment");
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(stepDetailFragment);
            fragmentTransaction.commit();
            stepDetailFragment = null;
        }
    }

    private void killRecipeListFragment() {
        if (recipeListFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();//Get Fragment Manager
            Log.e("RecipeDetail", "Killing Fragment RecipeListFragment");
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(recipeListFragment);
            fragmentTransaction.commit();
            recipeListFragment = null;
        }
    }

    private void killIngredientFragment() {
        if (ingredientFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();//Get Fragment Manager
            Log.e("RecipeDetail", "Killing Fragment IngredientFragment");
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(ingredientFragment);
            fragmentTransaction.commit();
            ingredientFragment = null;
        } else {
            Log.e("RecipeDetail", "ingredientFragment is NULL");
        }
    }

    private void createRecipeStepDetailFragment() {
        Log.e("RecipeDetail", "Calling createRecipeStepDetailFragment");

        if (current_step_id_position < 1) {
            Log.e("RecipeDetail", "Cannot create createRecipeStepDetailFragment:" + current_step_id_position);
            return;
        }

        Bundle bundle = new Bundle();
        prepareRecipeDetailBundle(bundle);

        stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setArguments(bundle);

        // check if detail fragment needs to be intialized
//        if (current_orientation == Configuration.ORIENTATION_LANDSCAPE && ) {
//            Log.e("RecipeDetail", "Landscape");
//            // we changed orientation, remove Ingredients fragment if we are
//            // landscape and sw600
//            Log.e("RecipeDetail", "!!!!mode:" + getResources().getInteger(R.integer.size));
//            if (getResources().getInteger(R.integer.size) == 2) {
//                // we are sw600
//                killRecipeListFragment();
//            }
//        }

        FragmentManager fragmentManager = getSupportFragmentManager();//Get Fragment Manager
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (current_orientation == Configuration.ORIENTATION_LANDSCAPE && getResources().getInteger(R.integer.size) == 2) {
                // tablet landscape
            Log.e("RecipeDetail", "tablet landscape mode");

            fragmentTransaction.replace(R.id.fragmentContainer1, stepDetailFragment).addToBackStack(null);
        } else if (current_orientation == Configuration.ORIENTATION_PORTRAIT && getResources().getInteger(R.integer.size) == 2) {
            // tablet portrait
            fragmentTransaction.replace(R.id.fragmentContainer1, stepDetailFragment).addToBackStack(null);
        } else {
            // Regular portrait/Landscape
            fragmentTransaction.replace(R.id.fragmentContainer, stepDetailFragment).addToBackStack(null);
        }

        fragmentTransaction.commit();
    }

    private void createIngredientFragment() {
        Log.e("RecipeDetail", "Calling createIngredientFragment");
        FragmentManager fragmentManager = getSupportFragmentManager();//Get Fragment Manager

        Bundle bundle = new Bundle();
        bundle.putInt(RecipeDetail.RECIPE_ID_STR, recipeInfo.id);

        ingredientFragment = new IngredientFragment();
        ingredientFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (getResources().getInteger(R.integer.size) == 1) {
            fragmentTransaction.replace(R.id.fragmentContainer, ingredientFragment).addToBackStack(null);
        } else {
            fragmentTransaction.replace(R.id.fragmentContainer1, ingredientFragment).addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    private void createRecipeListFragment() {
        Log.e("RecipeDetail", "Calling createRecipeListFragment");

        // create list fragment instance
        Bundle bundle = new Bundle();
        bundle.putInt(RecipeDetail.RECIPE_ID_STR, recipeInfo.id);
        recipeListFragment = new RecipeListFragment();
        recipeListFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();//Get Fragment Manager

        if (current_orientation == Configuration.ORIENTATION_LANDSCAPE && getResources().getInteger(R.integer.size) == 2) {
            // tablet landscape
            // we dont create list fragment in this mode
            Log.e("RecipeDetail", "Tablet Landscape mode");
            return;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (current_orientation == Configuration.ORIENTATION_PORTRAIT && getResources().getInteger(R.integer.size) == 2) {
            // tablet portrait
            Log.e("RecipeDetail", "Tablet Portrait mode");
            fragmentTransaction.replace(R.id.fragmentContainer, recipeListFragment).addToBackStack(null);
        } else {
            // Regular portrait/Landscape
            Log.e("RecipeDetail", "Regular Portrait/Landscape mode");
            fragmentTransaction.replace(R.id.fragmentContainer, recipeListFragment).addToBackStack(null);
        }

        fragmentTransaction.commit();
    }

    @Override
    protected void onPause() {
        Log.e("RecipeDetail", "onPause called");

        // save current state shared preference
        saveStateInSharedPreference(false);

        // Kill fragments and release player
        killStepDetailFragment();

        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.e("RecipeDetail", "onResume called");

        super.onResume();

        // restore any last state we have
        getStateFromSharedPreference();

        handleFragments();

        saveStateInSharedPreference(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // when app is destroyed, clear cache
        saveStateInSharedPreference(true);
    }
}
