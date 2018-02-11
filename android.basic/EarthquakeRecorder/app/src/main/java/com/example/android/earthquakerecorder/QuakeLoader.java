package com.example.android.earthquakerecorder;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.android.earthquakerecorder.models.Earthquake;
import com.example.android.earthquakerecorder.models.Earthquakes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QuakeLoader extends AsyncTaskLoader<List<Earthquake>> {
    private final static String LOG_TAG = QuakeLoader.class.getSimpleName();

    private String mUrl;
    private List<Earthquake> mQuakes;

    QuakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        List<Earthquake> result = new ArrayList<>();

        URL url = createURL(mUrl);
        try {
            String data = fetchQuakeData(url);
            result = Earthquakes.fromJSON(data);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Failed to fetch quake data: " + e.getMessage());
        }

        return result;
    }

    @Override
    public void deliverResult(List<Earthquake> quakes) {
        if (isReset()) {
            Log.w(LOG_TAG, "An async query came in while the Loader was reset!");
            if (quakes != null) {
                releaseResources(quakes);
                return;
            }
        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        List<Earthquake> oldQuakes = mQuakes;
        mQuakes = quakes;

        if (isStarted()) {
            Log.d(LOG_TAG, "Delivering results to the LoaderManager for the ListFragment to display.");
            // If the Loader is in a started state, have the superclass deliver the
            // results to the client.
            super.deliverResult(quakes);
        }

        // Invalidate the old data as we don't need it any more.
        if (oldQuakes != null &&  oldQuakes != quakes) {
            Log.d(LOG_TAG, "Releasing any old data associated with this Loader.");
            releaseResources(oldQuakes);
        }
    }

    @Override
    protected void onReset() {
        Log.i(LOG_TAG, "onReset() called!");

        // Ensure the loader is stopped.
        onStopLoading();

        // At this point we can release the resources associated with 'news'.
        if (mQuakes != null) {
            releaseResources(mQuakes);
            mQuakes = null;
        }
    }

    @Override
    public void onCanceled(List<Earthquake> quakes) {
        Log.d(LOG_TAG, "onCanceled() called!");

        // Attempt to cancel the current asynchronous load.
        super.onCanceled(quakes);

        // The load has been canceled, so we should release the resources
        // associated with 'mNews'.
        releaseResources(quakes);
    }

    private URL createURL(String url) {
        URL _url;

        try {
            _url = new URL(url);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Invalid URL string: " + url);
            _url = null;
        }

        return _url;
    }

    private String fetchQuakeData(URL url) throws IOException {
        if (url == null) return "";

        String response = "";
        HttpURLConnection connection = null;
        InputStream inStream = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10 * 1000);
            connection.setConnectTimeout(15 * 1000);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = connection.getInputStream();
                response = readFromStream(inStream);
            } else {
                Log.w(LOG_TAG, "Server error status code: " + responseCode);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Failed to fetch quake data.", e);
        } finally {
            if (connection != null) connection.disconnect();
            if (inStream != null) inStream.close();
        }

        return response;
    }

    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Helper method to take care of releasing resources associated with an
     * actively loaded data set.
     */
    private void releaseResources(List<Earthquake> quakes) {
        // For a simple List, there is nothing to do. For something like a Cursor,
        // we would close it in this method. All resources associated with the
        // Loader should be released here.
    }
}
