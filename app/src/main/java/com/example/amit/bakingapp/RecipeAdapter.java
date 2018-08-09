package com.example.amit.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    ArrayList<RecipeInfo> recipeList;
    Context context;
    RecyclerView recyclerView = null;
    CustomGridItemClick customGridItemClick;

    public RecipeAdapter(RecyclerView recyclerView, CustomGridItemClick listener, Context context,
                         ArrayList<RecipeInfo> recipeList) {
        this.recipeList = recipeList;
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

        View view = inflater.inflate(R.layout.grid_view_item, parent, shouldAttachToParentImmediately);
        final ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customGridItemClick.onItemClick(view, recipeList.get(viewHolder.getAdapterPosition()),
                        viewHolder.getAdapterPosition(), 0);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeAdapter.ViewHolder holder, int position) {
        RecipeInfo recipeInfo;
        String imageUri;

        recipeInfo = recipeList.get(position);
        holder.descriptionView.setText(recipeInfo.name);
    }

    @Override
    public int getItemCount() {
        Log.e("RecipeAdapter", "!!Count:" + Integer.toString(recipeList.size()));
        return recipeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionView;

        public ViewHolder(View itemView) {
            super(itemView);
            descriptionView = itemView.findViewById(R.id.grid_text_view);
        }
    }
}
