package com.udacity.sandwichclub.utils;

import android.text.TextUtils;
import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    private static final String LOG_TAG = JsonUtils.class.getSimpleName();

    // Constants for various keys in the JSON document
    // to avoid typos and magic strings throughout the code.
    private static final String JSON_KEY_NAME = "name";
    private static final String JSON_KEY_MAIN_NAME = "mainName";
    private static final String JSON_KEY_AKA = "alsoKnownAs";
    private static final String JSON_KEY_PLACE_OF_ORIGIN = "placeOfOrigin";
    private static final String JSON_KEY_DESCRIPTION ="description";
    private static final String JSON_KEY_IMAGE = "image";
    private static final String JSON_KEY_INGREDIENTS = "ingredients";

    public static Sandwich parseSandwichJson(String json) {
        // Sanity check - fast exit
        if (TextUtils.isEmpty(json)) {
            Log.e(LOG_TAG, "Empty input JSON data.");
            return null;
        }

        Sandwich sandwich = new Sandwich();
        try {
            JSONObject root = new JSONObject(json);
            JSONObject name = root.getJSONObject(JSON_KEY_NAME);

            sandwich.setMainName(name.getString(JSON_KEY_MAIN_NAME));
            sandwich.setAlsoKnownAs(jsonArrayToList(name.getJSONArray(JSON_KEY_AKA)));
            sandwich.setPlaceOfOrigin(root.getString(JSON_KEY_PLACE_OF_ORIGIN));
            sandwich.setDescription(root.getString(JSON_KEY_DESCRIPTION));
            sandwich.setImage(root.getString(JSON_KEY_IMAGE));
            sandwich.setIngredients(jsonArrayToList(root.getJSONArray(JSON_KEY_INGREDIENTS)));
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing JSON data: " + e.getMessage());
            return null;
        }

        return sandwich;
    }

    /**
     * Transforms a {@link JSONArray} of strings into a {@link List<String>}.
     *
     * @param array The input {@link JSONArray} containing a set of strings.
     *
     * @return The input set of strings wrapped inside a {@link List<String>}.
     *
     * @throws JSONException when invalid JSON doc is passed in.
     */
    private static List<String> jsonArrayToList(JSONArray array) throws JSONException {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            result.add(array.getString(i));
        }

        return result;
    }
}
