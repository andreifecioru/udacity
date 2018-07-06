package com.example.android.background.sync;

import android.content.Context;
import android.util.Log;

import com.example.android.background.utilities.PreferenceUtilities;

public class ReminderTasks {
    private static final String LOG_TAG = ReminderTasks.class.getSimpleName();
    public static final String ACTION_INCREMENT_WATER_COUNT = "incr-water-cnt";

    private static void incrementWaterCount(Context context) {
        PreferenceUtilities.incrementWaterCount(context);
    }

    public static void  executeTask(Context context, String action) {
        switch (action) {
            case ACTION_INCREMENT_WATER_COUNT:
                incrementWaterCount(context);
                break;

            default:
                Log.w(LOG_TAG, "Unsupported action: " + action);
                break;
        }

    }
}


