package com.example.amit.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.example.amit.bakingapp.StepDetailFragment.recipe_id;

public class RecipeWidgetService extends RemoteViewsService {
    String[] mList = null;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.e("RecipeWidgetService", "RecipeWidgetService called");
        return new RecipeWidgetFactory(this.getApplicationContext(), intent);
    }

    class RecipeWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
        private Context mContext;
        private Cursor mCursor = null;

        public RecipeWidgetFactory(Context applicationContext, Intent intent) {
            mContext = applicationContext;
        }

        @Override
        public void onCreate() {
            Log.e("RecipeWidgetFactory", "!!!onCreate called");
        }

        @Override
        public void onDataSetChanged() {
            Log.e("RecipeWidgetFactory", "!!!onDataSetChanged called");

            SharedPreferences sharedPreferences = mContext.getSharedPreferences(RecipeDetail.SHARED_FILE, Context.MODE_PRIVATE);
            String str = sharedPreferences.getString(MainActivity.RECIPE_INGREDIENT_STR, null);
            Gson gson = new Gson();

            if (str != null) {
                mList = gson.fromJson(str, String[].class);
            } else {
                Log.e("RecipeWidgetFactory", "!!!NO DATA FOR WIDGET:");
            }
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (mList != null) {
                Log.e("RecipeWidgetFactory", "getCount called:" + Integer.toString(mList.length));
                return mList.length;
            }
            else {
                Log.e("RecipeWidgetFactory", "getCount called: 0");
                return 0;
            }
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Ingredients ingredients;

          //  Log.e("RecipeWidgetFactory", "getViewAt called:" + position);

            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
            //rv.setTextViewText(R.id.widget_item_tv, mCursor.getString(1));
            //Log.e("RecipeWidgetFactory", "Sending:" + mList[position]);

            rv.setTextViewText(R.id.widget_item_tv, mList[position]);

            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
