package com.android.example.bakingapp.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.example.bakingapp.R;
import com.android.example.bakingapp.service.BakingTimeWidgetService;


import static com.android.example.bakingapp.ui.RecipeDetailsActivity.INTENT_EXTRA_RECIPE_ID_KEY;

/**
 * Implementation of App Widget functionality.
 *
 * The widget does the following:
 *    - displays a summary of the *last* recipe the user has accessed.
 *    - when tapped, the {@link RecipeDetailsActivity} is launched, displaying
 *      the recipe shown in the widget (i.e. the recipe that was last accessed).
 */
public class BakingTimeWidget extends AppWidgetProvider {
    private static final String LOG_TAG  = BakingTimeWidget.class.getSimpleName();

    private static final String EMPTY_STRING = "";

    public static final String RECIPE_ID_KEY = "recipe.id.key";
    public static final String RECIPE_NAME_KEY = "recipe.name.key";
    public static final String INGREDIENT_SET_KEY = "ingredient.set.key";

    public static void updateAppWidgets(Context context,
                                        AppWidgetManager appWidgetManager,
                                        int[] appWidgetIds) {

        Log.d(LOG_TAG, "Updating app widget.");

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_time_widget);

        // Try to restore info about the last accessed recipe from the
        // app's preference store.
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String recipeName = sharedPreferences.getString(RECIPE_NAME_KEY, null);
        views.setTextViewText(R.id.tv_recipe_name, recipeName);

        Intent intent = new Intent(context, BakingTimeWidgetService.class);
        views.setRemoteAdapter(R.id.lv_ingredient_list, intent);

        // Create an intent to launch the RecipeDetailsActivity when the widget is tapped
        long recipeId = sharedPreferences.getLong(RECIPE_ID_KEY, 0);
        Intent activityIntent = new Intent(context, RecipeDetailsActivity.class);
        // Add the recipe ID as additional info to the intent.
        // This will let the RecipeDetailsActivity to show the recipe that is displayed in the widget
        activityIntent.putExtra(INTENT_EXTRA_RECIPE_ID_KEY, recipeId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);

        // Hook-up the pending intent to our widget.
        views.setOnClickPendingIntent(R.id.tv_recipe_name, pendingIntent);

        // Instruct the widget manager to update the widget.

        // Notify the widget to update the ingredient list
        // There may be multiple widgets active, so update all of them.
        for (int appWidgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

        // Notify the widget to update the ingredient list
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_ingredient_list);
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        updateAppWidgets(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void refresh(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context, BakingTimeWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);

        updateAppWidgets(context, appWidgetManager, appWidgetIds);
    }
}

