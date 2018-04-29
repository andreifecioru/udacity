package com.example.android.popularmovies;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.Movies;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * Subclass of {@link AsyncTaskLoader} which implements all the logic of contacting
 * the movie DB API and then preparing the data to be displayed in the UI.
 *
 * Followed implementation example from: https://github.com/alexjlockwood/adp-applistloader
 *
 * Employed the HttpOK lib to download the actual data.
 */
public class MovieAsyncTaskLoader extends AsyncTaskLoader<List<Movie>> {
    private static final String LOG_TAG = MovieAsyncTaskLoader.class.getSimpleName();

    private final OkHttpClient mHttpClient = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();

    private URL mUrl;
    private List<Movie> mMovies;

    MovieAsyncTaskLoader(Context context, Uri uri) {
        super(context);

        Log.i(LOG_TAG, "Downloading movie posters from: " + uri.toString());
        String uriString = uri.toString();
        try {
            mUrl = new URL(uriString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Cannot parse URL string: " + uriString);
        }
    }

    @Nullable
    @Override
    public List<Movie> loadInBackground() {
        List<Movie> result = new ArrayList<>();

        try {
            // Get the raw (String data) from the remote server.
            String data = fetchData();

            // Parse the raw data as JSON and transform it into a list of @{link MoviePoster}
            result = Movies.fromJSON(data);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Failed to fetch movie posters: " + e.getMessage());
        }

        return result;
    }

    @Override
    public void deliverResult(@Nullable List<Movie> movies) {
        if (isReset()) {
            Log.w(LOG_TAG, "An async query came in while the Loader was reset!");
            if (movies != null) {
                releaseResources(movies);
                return;
            }
        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        List<Movie> oldPosters = mMovies;
        mMovies = movies;

        if (isStarted()) {
            Log.d(LOG_TAG, "Delivering results to the LoaderManager to be displayed in the UI");
            // If the Loader is in a started state, have the superclass deliver the
            // results to the client.
            super.deliverResult(movies);
        }

        // Invalidate the old data as we don't need it any more.
        if (oldPosters != null &&  oldPosters != movies) {
            Log.d(LOG_TAG, "Releasing any old data associated with this Loader.");
            releaseResources(oldPosters);
        }
    }

    @Override
    protected void onReset() {
        Log.d(LOG_TAG, "onReset() called!");

        // Ensure the loader is stopped.
        onStopLoading();

        // At this point we can release the resources associated with our data.
        if (mMovies != null) {
            releaseResources(mMovies);
            mMovies = null;
        }
    }

    @Override
    public void onCanceled(List<Movie> movies) {
        Log.d(LOG_TAG, "onCanceled() called!");

        // Attempt to cancel the current asynchronous load.
        super.onCanceled(movies);

        // The load has been canceled, so we should release the resources
        // associated with our data
        releaseResources(movies);
    }

    /**********************************************
     * Helper methods which handle network I/O
     *********************************************/
    private String fetchData() throws IOException {
        Request request = new Request.Builder()
                .url(mUrl)
                .build();

        Response response = mHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            ResponseBody body = response.body();
            if (body != null) {
                return body.string();
            }
        } else {
            Log.w(LOG_TAG, "Failed to download movie list. Server error code: " + response.code());
        }

        return "";
    }

    /**
     * Helper method to take care of releasing resources associated with an
     * actively loaded data set.
     */
    private void releaseResources(List<Movie> movies) {
        // For a simple List, there is nothing to do. For something like a Cursor,
        // we would close it in this method. All resources associated with the
        // Loader should be released here.
    }
}
