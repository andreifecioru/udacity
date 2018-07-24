package com.example.android.sunshine.sync;

import java.net.URL;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;
import com.example.android.sunshine.data.WeatherContract.WeatherEntry;


class SunshineSyncTask {
    synchronized public static void syncWeather(Context context) {
        URL weatherRequestUrl = NetworkUtils.getUrl(context);

        try {
            String jsonWeatherResponse = NetworkUtils
                    .getResponseFromHttpUrl(weatherRequestUrl);

            ContentValues[] weatherData = OpenWeatherJsonUtils
                    .getWeatherContentValuesFromJson(context, jsonWeatherResponse);

            if (weatherData != null && weatherData.length != 0) {
                ContentResolver resolver = context.getContentResolver();

                // delete everything
                resolver.delete(WeatherEntry.CONTENT_URI, null, null);

                // bulk-insert the new data
                resolver.bulkInsert(WeatherEntry.CONTENT_URI, weatherData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}