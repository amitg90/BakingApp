package com.example.amit.bakingapp;

import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class RecipeListFragment extends Fragment {
    Context context = null;
    public RecipeDetailAdapter recipeDetailAdapter = null;
    public static RecyclerView recyclerView;
    Bundle bundle = null;
    public View layout_view = null;
    public ImageView recipe_imageView = null;
    public TextView  recipe_servings_tv = null;

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
        layout_view = view.findViewById(R.id.recipe_layout_view);
        recipe_imageView = view.findViewById(R.id.recipe_image_view);
        recipe_servings_tv = view.findViewById(R.id.servings);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecipeInfo recipeInfo;
        int recipe_id;

        Log.e("RecipeListFragment", "onActivityCreated called");

        // Get the Intent that started this activity and extract the string
        if (bundle != null) {
            recipe_id = bundle.getInt(RecipeDetail.RECIPE_ID_STR, -1);
            Log.e("RecipeListFragment", "Bundle NOT Null: Recipe ID:" + Integer.toString(recipe_id));

            if (recipe_id == -1) {
                Toast.makeText(context, "Invalid ID entered", Toast.LENGTH_LONG).show();
                return;
            }

            // recipe ID starts from 1, array list position are 0 based
            recipeInfo = RecipeDb.recipeInfoArrayList.get((recipe_id - 1));
            Log.e("Amit", "Detail Receipt triggered:" + recipeInfo.name);

            recipe_servings_tv.setText("Servings: " + Integer.toString(recipeInfo.servings));

            // check if we have image to show
            String default_image = "https://www.fnordware.com/superpng/pnggrad16rgb.png";
            if (recipeInfo.image.isEmpty() == false) {
                Log.e("Amit", "Loading Image:" + recipeInfo.image);
                Picasso.with(context).load(recipeInfo.image).into(recipe_imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.e("Amit", "Loading Image: Success");
                    }

                    @Override
                    public void onError() {
                        Log.e("Amit", "Loading Image: Failed");
                    }
                });
            } else {
                Log.e("Amit", "Loading Default Image:" + recipeInfo.name);
                Picasso.with(context).load(default_image).into(recipe_imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.e("Amit", "Loading Image: Success");
                    }

                    @Override
                    public void onError() {
                        Log.e("Amit", "Loading Image: Failed");
                    }
                });
            }

            recipeDetailAdapter = new RecipeDetailAdapter(recyclerView, (CustomGridItemClick) context, context, recipeInfo);
            recyclerView.setAdapter(recipeDetailAdapter);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            Log.e("RecipeListFragment", "Bundle is Null");
        }
    }
}
