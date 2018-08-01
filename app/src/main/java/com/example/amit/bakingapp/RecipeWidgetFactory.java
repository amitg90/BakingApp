package com.example.amit.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class RecipeWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private Cursor mCursor = null;

    public RecipeWidgetFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
//        if (mCursor != null) {
//            mCursor.close();
//        }
//
//        final long identityToken = Binder.clearCallingIdentity();
//        Uri uri = Contract.PATH_TODOS_URI;
//        mCursor = mContext.getContentResolver().query(uri,
//                null,
//                null,
//                null,
//                Contract._ID + " DESC");
//
//        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        //return mCursor == null ? 0 : mCursor.getCount();
        return 10;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.e("RecipeWidgetFactory", "getViewAt called");

//        if (position == AdapterView.INVALID_POSITION ||
//                mCursor == null || !mCursor.moveToPosition(position)) {
//            return null;
//        }

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        //rv.setTextViewText(R.id.widget_item_tv, mCursor.getString(1));
        rv.setTextViewText(R.id.widget_item_tv, "Testing");

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
        //return mCursor.moveToPosition(position) ? mCursor.getLong(0) : position;
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
