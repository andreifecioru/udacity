package com.example.android.funtravel.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.android.funtravel.R;
import com.example.android.funtravel.common.model.OfferType;

public class PreferenceUtils {
    private static final String LOG_TAG = PreferenceUtils.class.getSimpleName();

    private static final int MAX_OFFER_COUNT_DEFAULT = 20;
    private static final int WIDGET_MAX_OFFER_COUNT_DEFAULT = 5;

    public static int getMaxOfferCountSetting(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String prefKey = context.getString(R.string.settings_max_offer_count_key);
        String prefDefaultValue = context.getString(R.string.settings_max_offer_count_default);

        String prefValue = sharedPreferences.getString(prefKey,prefDefaultValue);

        int retVal = MAX_OFFER_COUNT_DEFAULT;
        try {
            retVal = Integer.parseInt(prefValue);
        } catch (NumberFormatException e) {
            Log.e(LOG_TAG, "Unable to parse offers per page setting. Defaulting to " + MAX_OFFER_COUNT_DEFAULT);
        }

        return retVal;
    }

    public static int getWidgetMaxOfferCountSetting(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String prefKey = context.getString(R.string.settings_widget_max_offer_count_key);
        String prefDefaultValue = context.getString(R.string.settings_widget_max_offer_count_default);

        String prefValue = sharedPreferences.getString(prefKey, prefDefaultValue);

        int retVal = WIDGET_MAX_OFFER_COUNT_DEFAULT;
        try {
            retVal = Integer.parseInt(prefValue);
        } catch (NumberFormatException e) {
            Log.e(LOG_TAG, "Unable to parse offers per page setting. Defaulting to " + WIDGET_MAX_OFFER_COUNT_DEFAULT);
        }

        return retVal;
    }

    public static int getOfferCategoryFlags(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        int result = OfferType.Flags.NONE;

        if (sharedPreferences.getBoolean(context.getString(R.string.settings_cityscape_check_key), true)) {
            result |= OfferType.Flags.CITYSCAPE;
        }

        if (sharedPreferences.getBoolean(context.getString(R.string.settings_trekking_check_key), true)) {
            result |= OfferType.Flags.TREKKING;
        }

        if (sharedPreferences.getBoolean(context.getString(R.string.settings_resort_check_key), true)) {
            result |= OfferType.Flags.RESORT;
        }

        return result;
    }

    private PreferenceUtils() {
        throw new IllegalStateException("Utility class");
    }
}
