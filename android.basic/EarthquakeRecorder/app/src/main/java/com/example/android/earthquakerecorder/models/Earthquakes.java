package com.example.android.earthquakerecorder.models;


import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Earthquakes {
    private static final String LOG_TAG = Earthquake.class.getSimpleName();
    public static final String QUAKE_DATA_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";

    public static List<Earthquake> createEarthquakes() {
        List<Earthquake> quakes = new ArrayList<>();

        quakes.add(new Earthquake("94km SSE of Taron, Papua New Guinea", 6.1, 1453777820750L, "www.example.com"));
        quakes.add(new Earthquake("50km NNE of Al Hoceima, Morocco", 6.3, 1453695722730L, "www.example.com"));
        quakes.add(new Earthquake("Old Iliamna, Alaska", 7.123, 1453631430230L, "www.example.com"));
        quakes.add(new Earthquake("94km SSE of Taron, Papua New Guinea", 6.1, 1453777820750L, "www.example.com"));
        quakes.add(new Earthquake("50km NNE of Al Hoceima, Morocco", 6.3, 1453695722730L, "www.example.com"));
        quakes.add(new Earthquake("Old Iliamna, Alaska", 7.123, 1453631430230L, "www.example.com"));
        quakes.add(new Earthquake("94km SSE of Taron, Papua New Guinea", 6.1, 1453777820750L, "www.example.com"));
        quakes.add(new Earthquake("50km NNE of Al Hoceima, Morocco", 6.3, 1453695722730L, "www.example.com"));
        quakes.add(new Earthquake("Old Iliamna, Alaska", 7.123, 1453631430230L, "www.example.com"));

        return quakes;
    }

    public static List<Earthquake> fromJSON(String jsonData) {
        List<Earthquake> result = new ArrayList<>();

        if (TextUtils.isEmpty(jsonData)) return result;

        try {
            JSONObject root = new JSONObject(jsonData);
            JSONArray features = root.getJSONArray("features");

            for (int i = 0; i < features.length(); i++) {
                JSONObject props = features.getJSONObject(i).getJSONObject("properties");
                result.add(new Earthquake(
                    props.getString("place"),
                    props.getDouble("mag"),
                    props.getLong("time"),
                    props.getString("url")
                ));
            }
        } catch (JSONException e) {
            Log.w(LOG_TAG, e.getMessage());
        }

        return result;
    }


    public interface OnQuakeListAvailable {
        List<Earthquake> getQuakeList();
    }
}
