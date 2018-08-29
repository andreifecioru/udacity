package com.example.android.popularmovies.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;


/**
 * Utility class for accessing with YouTube videos.
 *
 * Inspired by: https://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent
 */
public final class YouTubeUtils {
    private static final String YOUTUBE_APP_SCHEMA = "vnd.youtube:";
    private static final String YOUTUBE_BASE_URL="http://www.youtube.com/watch?v=";

    private YouTubeUtils() {
        throw new IllegalStateException("Utility class");
        // Utility class.
    }

    /**
     * Access a YouTube video identified by it's unique key.
     *
     * It tries to open the YouTube app (if present) and falls-back to opening
     * the web-browser.
     *
     * @param context application context
     * @param id YouTube resource id
     */
    public static void openYouTubeVideo(Context context, String id){
        try { // try to use the YouTube app if available.
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(YOUTUBE_APP_SCHEMA + id));
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) { // fallback on the web-browser
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(YOUTUBE_BASE_URL + id));
            context.startActivity(intent);
        }
    }
}
