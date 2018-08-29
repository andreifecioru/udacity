package com.example.android.popularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.android.popularmovies.R;

/**
 * Utility class for working with the preference store.
 */
public final class PreferencesUtils {

    private PreferencesUtils() {
        throw new IllegalStateException("Utility class");
        // Utility class.
    }

    /**
     * Retrieves the current setting for the movie sorting criteria from the
     * shared preference store.
     *
     * @return the current value of the sort-by criteria preference setting
     */
    public static String getMoviesSortByPreferenceSetting(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(
                context.getString(R.string.settings_order_by_key),
                context.getString(R.string.settings_order_by_default));
    }
}
