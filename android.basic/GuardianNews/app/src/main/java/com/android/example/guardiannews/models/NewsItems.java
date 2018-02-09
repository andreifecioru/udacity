package com.android.example.guardiannews.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Utility class for the {@link NewsItem} class. Provides a set of factory functions
 * for creating collections of {@link NewsItem} from various sources.
 */
public class NewsItems {
    private static final String LOG_TAG = NewsItem.class.getSimpleName();

    /**
     * Static factory method which creates a list of {@link NewsItem} from a JSON string source
     * as provided by the Guardian API.
     *
     * @param jsonData A {@link String} containing the JSON data as provided by the Guardian API
     *
     * @return A list of locations representing the parks locations.
     */
    public static List<NewsItem> fromJSON(String jsonData) {
        // start with an empty list
        List<NewsItem> newsItems = new ArrayList<>();

        try {
            // start digging for the data
            JSONObject root = new JSONObject(jsonData);
            JSONObject response = root.getJSONObject("response");
            String status = response.getString("status");

            // we only dig further if the API response says it's ok
            if ("ok".equals(status)) {
                JSONArray results = response.getJSONArray("results");

                Log.d(LOG_TAG, "Received " + results.length() + " items.");

                // sift through each post
                for (int i = 0; i < results.length(); i++) {
                    JSONObject result = results.getJSONObject(i);
                    String title = result.getString("webTitle");
                    String publishTimestamp = result.getString("webPublicationDate");
                    String pillar = result.getString("pillarName");
                    String url = result.getString("webUrl");
                    JSONArray tags = result.getJSONArray("tags");

                    // start digging for the author (i.e. the "contributor" tag - if it exists)
                    // initially we assume that the author is "unknown"
                    String authorName = "Unknown";
                    for (int j = 0; j < tags.length(); j++) {
                        JSONObject tag = tags.getJSONObject(j);
                        String tagType = tag.getString("type");
                        if ("contributor".equals(tagType)) {
                            authorName = tag.getString("webTitle");

                            // we found our contributor tag. Let's break out.
                            break;
                        }
                    }

                    // construct a new {@link NewsItem} and add it to the final result.
                    newsItems.add(new NewsItem(title, authorName, publishTimestamp, pillar, url));
                }
            }
        } catch (JSONException e) {
            Log.w(LOG_TAG, e.getMessage());
        }

        // return the list of {@link NewsItem} we've accumulated so far.
        return newsItems;
    }
}
