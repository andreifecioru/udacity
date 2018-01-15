package com.example.android.tourguide.models;

import android.content.Context;
import android.content.res.Resources;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.tourguide.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for the {@link Location} class. Provides a set of factory functions
 * for creating various types of locations (parks, museums, restaurants, etc.)
 */
public class Locations {
    public static final int MAX_RATING = 5;

    private static List<Location> parkAndGardenLocations = new ArrayList<>();
    private static List<Location> museumLocations = new ArrayList<>();
    private static List<Location> concertAndEventLocations = new ArrayList<>();
    private static List<Location> restaurantLocations = new ArrayList<>();

    /**
     * Static factory method which creates the 'parks and gardens' locations.
     *
     * @param context A {@link Context} instance (used for accessing resources).
     *
     * @return A list of locations representing the parks locations.
     */
    public static List<Location> parksAndGardens(Context context) {
        // create the locations only once (lazily)
        if (parkAndGardenLocations.isEmpty()) {
            Resources res = context.getResources();

            String[] names = res.getStringArray(R.array.park_and_gardens_names);
            String[] descriptions = res.getStringArray(R.array.park_and_gardens_descriptions);
            int[] ratings = res.getIntArray(R.array.park_and_gardens_ratings);
            int[] imgResIDs = {
                    R.drawable.herastrau_park,
                    R.drawable.tineretului_park,
                    R.drawable.cismigiu_park,
                    R.drawable.carol_park,
                    R.drawable.titan_park
            };

            // the location count is dictated by the shortest array
            // to avoid index-out-of-bound errors
            int locationCount = Math.min(names.length, descriptions.length);
            locationCount = Math.min(locationCount, ratings.length);
            locationCount = Math.min(locationCount, imgResIDs.length);

            List<Location> locations = new ArrayList<>();

            for (int i = 0; i < locationCount; i ++) {
                locations.add(new Location(
                        names[i],
                        descriptions[i],
                        Math.min(MAX_RATING, ratings[i]),
                        imgResIDs[i],
                        Location.NO_AUDIO_RES_ID));
            }

            parkAndGardenLocations = locations;
        }

        return parkAndGardenLocations;
    }

    /**
     * Static factory method which creates the 'museums' locations.
     *
     * @param context A {@link Context} instance (used for accessing resources).
     *
     * @return A list of locations representing the museums locations.
     */
    public static List<Location> museums(Context context) {
        // create the locations only once (lazily)
        if (museumLocations.isEmpty()) {
            Resources res = context.getResources();

            String[] names = res.getStringArray(R.array.museums_names);
            String[] descriptions = res.getStringArray(R.array.museums_descriptions);
            int[] ratings = res.getIntArray(R.array.museums_ratings);
            int[] imgResIDs = {
                    R.drawable.museum_grigore_antipa,
                    R.drawable.museum_national_art,
                    R.drawable.museum_romanian_peasant,
                    R.drawable.museum_national_history,
                    R.drawable.museum_george_enescu
            };

            // the location count is dictated by the shortest array
            // to avoid index-out-of-bound errors
            int locationCount = Math.min(names.length, descriptions.length);
            locationCount = Math.min(locationCount, ratings.length);
            locationCount = Math.min(locationCount, imgResIDs.length);

            List<Location> locations = new ArrayList<>();

            for (int i = 0; i < locationCount; i ++) {
                locations.add(new Location(
                        names[i],
                        descriptions[i],
                        Math.min(MAX_RATING, ratings[i]),
                        imgResIDs[i],
                        Location.NO_AUDIO_RES_ID));
            }

            museumLocations = locations;
        }

        return museumLocations;
    }

    /**
     * Static factory method which creates the 'concerts and events' locations.
     *
     * @param context A {@link Context} instance (used for accessing resources).
     *
     * @return A list of locations representing the concerts locations.
     */
    public static List<Location> concertsAndEvents(Context context) {
        // create the locations only once (lazily)
        if (concertAndEventLocations.isEmpty()) {
            Resources res = context.getResources();

            String[] names = res.getStringArray(R.array.concerts_and_events_names);
            String[] descriptions = res.getStringArray(R.array.concerts_and_events_descriptions);
            int[] ratings = res.getIntArray(R.array.concerts_and_events_ratings);
            int[] imgResIDs = {
                    R.drawable.banica,
                    R.drawable.antonia,
                    R.drawable.carlas_dreams,
                    R.drawable.delia,
                    R.drawable.loredana
            };

            // the 'concerts and events' location type is the only one with audio resources
            int[] audioResIDs = {
                    R.raw.banica_sample,
                    R.raw.antonia_sample,
                    R.raw.carlas_dreams_sample,
                    R.raw.delia_sample,
                    R.raw.loredana_sample
            };

            // the location count is dictated by the shortest array
            // to avoid index-out-of-bound errors
            int locationCount = Math.min(names.length, descriptions.length);
            locationCount = Math.min(locationCount, ratings.length);
            locationCount = Math.min(locationCount, imgResIDs.length);
            locationCount = Math.min(locationCount, audioResIDs.length);

            List<Location> locations = new ArrayList<>();

            for (int i = 0; i < locationCount; i ++) {
                locations.add(new Location(
                        names[i],
                        descriptions[i],
                        Math.min(MAX_RATING, ratings[i]),
                        imgResIDs[i],
                        audioResIDs[i]));
            }

            concertAndEventLocations = locations;
        }

        return concertAndEventLocations;
    }

    /**
     * Static factory method which creates the 'restaurants' locations.
     *
     * @param context A {@link Context} instance (used for accessing resources).
     *
     * @return A list of locations representing the restaurants locations.
     */
    public static List<Location> restaurants(Context context) {
        // create the locations only once (lazily)
        if (restaurantLocations.isEmpty()) {
            Resources res = context.getResources();

            String[] names = res.getStringArray(R.array.restaurants_names);
            String[] descriptions = res.getStringArray(R.array.restaurants_descriptions);
            int[] ratings = res.getIntArray(R.array.restaurants_ratings);
            int[] imgResIDs = {
                    R.drawable.restaurants_beer_wagon,
                    R.drawable.restaurants_manuc_inn,
                    R.drawable.restaurants_bocca_lupo,
                    R.drawable.restaurants_shift,
                    R.drawable.restaurants_chef_experience
            };

            // the location count is dictated by the shortest array
            // to avoid index-out-of-bound errors
            int locationCount = Math.min(names.length, descriptions.length);
            locationCount = Math.min(locationCount, ratings.length);
            locationCount = Math.min(locationCount, imgResIDs.length);

            List<Location> locations = new ArrayList<>();

            for (int i = 0; i < locationCount; i ++) {
                locations.add(new Location(
                        names[i],
                        descriptions[i],
                        Math.min(MAX_RATING, ratings[i]),
                        imgResIDs[i],
                        Location.NO_AUDIO_RES_ID));
            }

            restaurantLocations = locations;
        }

        return restaurantLocations;
    }

    static public void showRatingStars(ViewGroup ratingContainer, Location location) {
        int i;

        // show full stars up to the rating value
        for (i = 0; i < location.getRating(); i++) {
            ImageView imgView = (ImageView) ratingContainer.getChildAt(i);
            imgView.setImageResource(android.R.drawable.star_on);
        }

        // all the remaining stars are empty
        for (; i< Locations.MAX_RATING; i++) {
            ImageView imgView = (ImageView) ratingContainer.getChildAt(i);
            imgView.setImageResource(android.R.drawable.star_off);

        }
    }
}
