package com.example.amit.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class IngredientFragment extends Fragment {
    public IngredientAdapter ingredientAdapter = null;
    public static RecyclerView recyclerView;
    Context context = null;
    Bundle bundle = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("IngredientFragment", "onCreate called");

        bundle = getArguments();
        if (bundle == null) {
            Log.e("IngredientFragment", "BUNDLE IS NULL");
        } else {
            Log.e("IngredientFragment", "!!!GOT BUNDLE");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.e("IngredientFragment", "onCreateView called");

        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        recyclerView = view.findViewById(R.id.recipe_detail_rv_list);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecipeInfo recipeInfo;
        int position;

        Log.e("IngredientFragment", "onActivityCreated called");

        // Get the Intent that started this activity and extract the string
        if (bundle != null) {
            Log.e("IngredientFragment", "Bundle NOT Null");

            position = bundle.getInt(MainActivity.RECIPE_INDEX_STR, -1);

            if (position == -1) {
                Toast.makeText(context, "Invalid Position entered", Toast.LENGTH_LONG).show();
                return;
            }

            recipeInfo = RecipeDb.recipeInfoArrayList.get(position);
            Log.e("Amit", "Ingredient List triggered:" + recipeInfo.name);

            ingredientAdapter = new IngredientAdapter(recyclerView, context, recipeInfo.ingredients);
            recyclerView.setAdapter(ingredientAdapter);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            Log.e("IngredientFragment", "Bundle is Null");
        }
    }
}
