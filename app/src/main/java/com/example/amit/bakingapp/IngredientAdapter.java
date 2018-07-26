package com.example.amit.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class IngredientAdapter  extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
    ArrayList<Ingredients> ingredientsArrayList;
    Context context;
    RecyclerView recyclerView = null;
    CustomGridItemClick customGridItemClick;

    public IngredientAdapter(RecyclerView recyclerView, Context context,
                            ArrayList<Ingredients> ingredientsArrayList) {
        this.ingredientsArrayList = ingredientsArrayList;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.ingredient_grid_view_item, parent, shouldAttachToParentImmediately);
        final IngredientAdapter.ViewHolder viewHolder = new IngredientAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final IngredientAdapter.ViewHolder holder, int position) {
        Ingredients ingredients;
        String imageUri;

        ingredients = ingredientsArrayList.get(position);
        holder.descriptionView.setText(ingredients.ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredientsArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionView;

        public ViewHolder(View itemView) {
            super(itemView);
            descriptionView = itemView.findViewById(R.id.ingredient_grid_text_view);
        }
    }
}