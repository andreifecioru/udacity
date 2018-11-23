package com.example.android.mygarden.service;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.example.android.mygarden.R;
import com.example.android.mygarden.widget.PlantWidget;
import com.example.android.mygarden.utils.PlantUtils;

import static com.example.android.mygarden.provider.PlantContract.BASE_CONTENT_URI;
import static com.example.android.mygarden.provider.PlantContract.INVALID_PLANT_ID;
import static com.example.android.mygarden.provider.PlantContract.PATH_PLANTS;
import static com.example.android.mygarden.provider.PlantContract.PlantEntry.COLUMN_CREATION_TIME;
import static com.example.android.mygarden.provider.PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME;
import static com.example.android.mygarden.provider.PlantContract.PlantEntry.COLUMN_PLANT_TYPE;
import static com.example.android.mygarden.provider.PlantContract.PlantEntry._ID;

public class PlantWateringService extends JobIntentService {
    private static final String LOG_TAG = PlantWateringService.class.getSimpleName();

    private static final int JOB_ID = 1;

    public static final String ACTION_WATER_PLANT = "com.example.android.mygarden.action.water_plant";
    public static final String ACTION_UPDATE_PLANT_WIDGETS = "com.example.android.mygarden.action.update_widgets";

    private final static Uri PLANTS_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLANTS).build();

    public final static String PLANT_ID_KEY = "plant.id.key";

    private static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, PlantWateringService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@Nullable Intent intent) {
        Log.d(LOG_TAG, "Handling some work...");
        if (intent == null) return;

        final String action = intent.getAction();

        if (action == null) return;

        switch (action) {
            case ACTION_WATER_PLANT:
                handleActionWaterPlant(intent.getLongExtra(PLANT_ID_KEY, INVALID_PLANT_ID));
                break;

            case ACTION_UPDATE_PLANT_WIDGETS:
                handleActionUpdatePlantWidgets();
                break;

            default:
                Log.d(LOG_TAG, "Don't know how to handle action: " + action);
        }
    }

    private void handleActionUpdatePlantWidgets() {
        Log.d(LOG_TAG, "Updating plants widget...");

        Cursor cursor = getContentResolver().query(PLANTS_URI,
                null,
                null,
                null,
                COLUMN_LAST_WATERED_TIME);

        int imgRes = R.drawable.grass;
        boolean canWater = false;
        long plantId = INVALID_PLANT_ID;

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int plantIdIndex = cursor.getColumnIndex(_ID);
            int createTimeIndex = cursor.getColumnIndex(COLUMN_CREATION_TIME);
            int waterTimeIndex = cursor.getColumnIndex(COLUMN_LAST_WATERED_TIME);
            int plantTypeIndex = cursor.getColumnIndex(COLUMN_PLANT_TYPE);

            long now = System.currentTimeMillis();

            plantId = cursor.getLong(plantIdIndex);
            long createdAt = cursor.getLong(createTimeIndex);
            long wateredAt = cursor.getLong(waterTimeIndex);
            int plantType = cursor.getInt(plantTypeIndex);

            long timeSinceLastWatered = now - wateredAt;
            long timeSinceCreation = now - createdAt;

            canWater = timeSinceLastWatered > PlantUtils.MIN_AGE_BETWEEN_WATER &&
                    timeSinceLastWatered < PlantUtils.MAX_AGE_WITHOUT_WATER;

            imgRes = PlantUtils.getPlantImageRes(this, timeSinceCreation, timeSinceCreation, plantType);
        }

        if (cursor != null) cursor.close();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, PlantWidget.class));

        PlantWidget.updateAppWidgets(this, appWidgetManager, appWidgetIds, imgRes, plantId, canWater);
    }

    private void handleActionWaterPlant(long plantId) {
        Log.d(LOG_TAG, "Watering plant: " + plantId);

        if (plantId == INVALID_PLANT_ID) {
            Log.d(LOG_TAG, "No plant to water.");
            return;
        }

        Uri SINGLE_PLANT_URI = ContentUris.withAppendedId(PLANTS_URI, plantId);

        long now = System.currentTimeMillis();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_LAST_WATERED_TIME, now);

        getContentResolver().update(SINGLE_PLANT_URI, contentValues,
                COLUMN_LAST_WATERED_TIME + ">?",
                new String[] { String.valueOf(now - PlantUtils.MAX_AGE_WITHOUT_WATER) });

        startActionUpdatePlantWidgets(this);
    }

    public static void startActionUpdatePlantWidgets(Context context) {
        Intent intent = new Intent(context, PlantWateringService.class);
        intent.setAction(ACTION_UPDATE_PLANT_WIDGETS);
        enqueueWork(context, intent);
    }
}
