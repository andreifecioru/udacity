package com.example.android.mygarden.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.example.android.mygarden.R;
import com.example.android.mygarden.service.PlantWateringService;

import static com.example.android.mygarden.service.PlantWateringService.PLANT_ID_KEY;
import static com.example.android.mygarden.ui.PlantDetailActivity.EXTRA_PLANT_ID;

/**
 * Implementation of App Widget functionality.
 */
public class PlantWidget extends AppWidgetProvider {
    private static final String LOG_TAG = PlantWidget.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, int plantImgResId, long plantId, boolean canWater) {

        RemoteViews views;
        views = getSinglePlantRemoteViews(context, plantImgResId, plantId, canWater);

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
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, plantImgResId, plantId, canWater);
        }
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
}

