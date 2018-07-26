package com.example.amit.bakingapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class RecipeAsyncTask extends AsyncTask<RecipeInfo, Void,Void> {
    MainActivity parentActivity;

    public RecipeAsyncTask(MainActivity parentActivity) {
        this.parentActivity = parentActivity;
    }

    @Override
    protected Void doInBackground(RecipeInfo... recipeInfos) {
        URL url;
        JSONObject obj;
        JSONObject oneObject;
        JSONArray result;
        RecipeInfo recipeInfo;
        Ingredients inputIngredients;
        Steps inputSteps;

        url = RecipeUtils.buildRecipeUrl();

        try {
            String response = RecipeUtils.getResponseFromHttpUrl(url);

            JSONArray jArray = new JSONArray(response);

            for (int i = 0; i < jArray.length(); i++) {
                recipeInfo = new RecipeInfo();
                recipeInfo.name = jArray.getJSONObject(i).getString("name");
                recipeInfo.id = jArray.getJSONObject(i).getInt("id");
                recipeInfo.servings = jArray.getJSONObject(i).getInt("servings");
                recipeInfo.image = jArray.getJSONObject(i).getString("image");
                String ingredientStr = jArray.getJSONObject(i).getString("ingredients");

                JSONArray ingredients = new JSONArray(ingredientStr);
                for (int j=0;j<ingredients.length();j++){
                    inputIngredients = new Ingredients();
                    JSONObject obj1 = ingredients.getJSONObject(j);
                    inputIngredients.ingredient = obj1.getString("ingredient");
                    inputIngredients.measure = obj1.getString("measure");
                    inputIngredients.quantity = obj1.getInt("quantity");
                    recipeInfo.ingredients.add(inputIngredients);
                }
                String stepsStr = jArray.getJSONObject(i).getString("steps");
                JSONArray steps = new JSONArray(stepsStr);

                for (int j=0;j<steps.length();j++){
                    inputSteps = new Steps();
                    JSONObject obj1 = steps.getJSONObject(j);
                    inputSteps.description = obj1.getString("description");
                    inputSteps.id = obj1.getInt("id");
                    inputSteps.shortDescription = obj1.getString("shortDescription");
                    inputSteps.thumbnailURL = obj1.getString("thumbnailURL");
                    inputSteps.videoURL = obj1.getString("videoURL");
                    recipeInfo.steps.add(inputSteps);
                }
                RecipeDb.recipeInfoArrayList.add(recipeInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        parentActivity.postRecipeAsyncTaskDone();
    }
}
