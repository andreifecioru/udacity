package com.android.example.bakingapp.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.android.example.bakingapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.android.example.bakingapp.ui.BakingTimeWidget.INGREDIENT_SET_KEY;


public class BakingTimeWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BakingTimeRemoteViewsFactory(this.getApplicationContext());
    }
}

class BakingTimeRemoteViewsFactory
        implements RemoteViewsService.RemoteViewsFactory {
    private static final String LOG_TAG = BakingTimeRemoteViewsFactory.class.getSimpleName();

    private final Context mContext;

    private List<String> mRecipeIngredients;

    BakingTimeRemoteViewsFactory(Context context) {
        mContext = context;
    }


    @Override
    public void onCreate() { }

    @Override
    public void onDataSetChanged() {
        // Try to restore info about the last accessed recipe from the
        // app's preference store.
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        mRecipeIngredients = new ArrayList<>();
        mRecipeIngredients.addAll(sharedPreferences.getStringSet(INGREDIENT_SET_KEY,
                Collections.<String>emptySet()));

        Log.d(LOG_TAG, "Data-set changed");
    }

    @Override
    public void onDestroy() { }

    @Override
    public int getCount() {
        if (mRecipeIngredients == null) return 0;
        return mRecipeIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mRecipeIngredients == null || mRecipeIngredients.isEmpty()) return null;

        String ingredientName = mRecipeIngredients.get(position);
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.baking_time_widget_list_item);
        views.setTextViewText(R.id.tv_ingredient_name, ingredientName);

        return views;
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
