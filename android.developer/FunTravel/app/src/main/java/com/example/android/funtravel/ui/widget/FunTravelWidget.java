package com.example.android.funtravel.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.android.funtravel.R;


/**
 * Implementation of App's Widget functionality.
 */
public class FunTravelWidget extends AppWidgetProvider {
    private static final String LOG_TAG = FunTravelWidget.class.getSimpleName();

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_fun_travel);

        Intent intent = new Intent(context, FunTravelRemoteViewsService.class);
        views.setRemoteAdapter(R.id.lv_offer_list, intent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action != null) {
            switch (action) {
                case AppWidgetManager.ACTION_APPWIDGET_UPDATE:
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    ComponentName componentName = new ComponentName(context, FunTravelWidget.class);
                    int[] ids = appWidgetManager.getAppWidgetIds(componentName);

                    Log.d(LOG_TAG, "Updating dataset");
                    appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.lv_offer_list);
                    break;
            }
        }

        super.onReceive(context, intent);
    }

    public static void updateFunTravelWidget(Context context) {
        Intent intent = new Intent(context, FunTravelWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, FunTravelWidget.class));

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context, FunTravelWidget.class);
        int[] ids = appWidgetManager.getAppWidgetIds(componentName);

        if(ids != null && ids.length > 0) {
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            context.sendBroadcast(intent);
        }
    }
}

