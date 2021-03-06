package com.example.android.mygarden.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.example.android.mygarden.R;
import com.example.android.mygarden.service.PlantWateringService;
import com.example.android.mygarden.ui.PlantDetailActivity;

import static com.example.android.mygarden.service.PlantWateringService.PLANT_ID_KEY;
import static com.example.android.mygarden.ui.PlantDetailActivity.EXTRA_PLANT_ID;

/**
 * Implementation of App Widget functionality.
 */
public class PlantWidget extends AppWidgetProvider {
    private static final String LOG_TAG = PlantWidget.class.getSimpleName();

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, int plantImgResId, long plantId, boolean canWater) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);

        RemoteViews views;
        if (width < 300) {
            views = getSinglePlantRemoteViews(context, plantImgResId, plantId, canWater);
        } else {
            views = getGridRemoteViews(context);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

        Log.d(LOG_TAG, "Widget updated");
    }

    public static void updateAppWidgets(Context context,
                                        AppWidgetManager appWidgetManager,
                                        int[] appWidgetIds,
                                        int plantImgResId,
                                        long plantId,
                                        boolean canWater) {
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, plantImgResId, plantId, canWater);
        }
    }

    private static RemoteViews getGridRemoteViews(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.plant_widget_grid);

        Intent intent = new Intent(context, GridWidgetService.class);
        views.setRemoteAdapter(R.id.widget_grid_view, intent);

        Intent appIntent = new Intent(context, PlantDetailActivity.class);
        PendingIntent pendingAppIntent = PendingIntent.getActivity(
                context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setPendingIntentTemplate(R.id.widget_grid_view, pendingAppIntent);

        views.setEmptyView(R.id.widget_grid_view, R.id.empty_view);

        return views;
    }

    private static RemoteViews getSinglePlantRemoteViews(Context context, int plantImgResId, long plantId, boolean canWater) {
        // The "views" here points to the whole hierarchy of views
        // in the widget's layout
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.plant_widget);

        // Create an intent to launch the MainActivity when the grass image view is clicked
        Intent appIntent = new Intent(context, PlantDetailActivity.class);
        appIntent.putExtra(EXTRA_PLANT_ID, plantId);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_plant_image, appPendingIntent);

        Intent wateringIntent = new Intent(context, PlantWateringService.class);
        wateringIntent.setAction(PlantWateringService.ACTION_WATER_PLANT);
        wateringIntent.putExtra(PLANT_ID_KEY, plantId);
        PendingIntent wateringPendingIntent = PendingIntent.getService(context, 0, wateringIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_water_image, wateringPendingIntent);

        Log.d(LOG_TAG, "Grass image resource: " + R.drawable.grass + ". Image resource: " + plantImgResId);

        views.setImageViewResource(R.id.widget_plant_image, plantImgResId);
        views.setTextViewText(R.id.widget_plant_id, String.valueOf(plantId));

        if (canWater) {
            views.setViewVisibility(R.id.widget_water_image, View.VISIBLE);
        } else {
            views.setViewVisibility(R.id.widget_water_image, View.VISIBLE);
        }

        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        PlantWateringService.startActionUpdatePlantWidgets(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        Log.d(LOG_TAG, "App widget options changed...");
        PlantWateringService.startActionUpdatePlantWidgets(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }
}

