package com.example.amit.bakingapp;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class RecipeListFragment extends Fragment {
    Context context = null;
    public RecipeDetailAdapter recipeDetailAdapter = null;
    public static RecyclerView recyclerView;
    Bundle bundle = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("RecipeListFragment", "onCreate called");

        bundle = getArguments();
        if (bundle == null) {
            Log.e("RecipeListFragment", "BUNDLE IS NULL");
        } else {
            Log.e("RecipeListFragment", "!!!GOT BUNDLE");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.e("RecipeListFragment", "onCreateView called");

        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        recyclerView = view.findViewById(R.id.recipe_detail_rv_list);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecipeInfo recipeInfo;
        int position;

        Log.e("RecipeListFragment", "onActivityCreated called");

        // Get the Intent that started this activity and extract the string
        if (bundle != null) {
            Log.e("RecipeListFragment", "Bundle NOT Null");

            position = bundle.getInt(MainActivity.RECIPE_INDEX_STR, -1);

            if (position == -1) {
                Toast.makeText(context, "Invalid Position entered", Toast.LENGTH_LONG).show();
                return;
            }

            recipeInfo = RecipeDb.recipeInfoArrayList.get(position);
            Log.e("Amit", "Detail Receipt triggered:" + recipeInfo.name);

            recipeDetailAdapter = new RecipeDetailAdapter(recyclerView, (CustomGridItemClick) context, context, recipeInfo);
            recyclerView.setAdapter(recipeDetailAdapter);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            Log.e("RecipeListFragment", "Bundle is Null");
        }
    }
}
