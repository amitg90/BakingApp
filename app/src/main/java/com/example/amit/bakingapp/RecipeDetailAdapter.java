package com.example.amit.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.ViewHolder> {
    RecipeInfo recipeInfo;
    Context context;
    RecyclerView recyclerView = null;
    CustomGridItemClick customGridItemClick;

    public RecipeDetailAdapter(RecyclerView recyclerView, CustomGridItemClick listener, Context context,
                               RecipeInfo recipeInfo) {
        this.recipeInfo = recipeInfo;
        this.context = context;
        this.recyclerView = recyclerView;
        this.customGridItemClick = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.detail_grid_view_item, parent, shouldAttachToParentImmediately);
        final ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("RecipeDetailAdapter", "Adapter Sending position:" + viewHolder.getAdapterPosition());
                Log.e("RecipeAdapter", "lastPlayerPosition set to 0");
                RecipeDetail.restored_lastPlayerPosition = 0;
                customGridItemClick.onItemClick(view, recipeInfo, viewHolder.getAdapterPosition());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeDetailAdapter.ViewHolder holder, int position) {
        if (position == 0) {
            // put ingredient info
            holder.descriptionView.setText("Ingredients");
        } else {
            // put steps info
            Steps step = recipeInfo.steps.get(position - 1);
            holder.descriptionView.setText(step.shortDescription);
        }
    }

    @Override
    public int getItemCount() {
        // ingredient + steps
        return (1 + recipeInfo.steps.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionView;

        public ViewHolder(View itemView) {
            super(itemView);
            descriptionView = itemView.findViewById(R.id.detail_grid_text_view);
        }
    }
}
