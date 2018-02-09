package com.android.example.guardiannews;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.android.example.guardiannews.models.NewsItem;
import com.android.example.guardiannews.models.NewsItems;

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


/**
 * Subclass of {@link AsyncTaskLoader} which implements all the logic of contacting
 * the Guardian API and then preparing the data to be displayed in the UI.
 *
 * Followed implementation example from: https://github.com/alexjlockwood/adp-applistloader
 */
class NewsRefreshAsyncTaskLoader extends AsyncTaskLoader<List<NewsItem>> {
    private final static String LOG_TAG = NewsRefreshAsyncTaskLoader.class.getSimpleName();

    private final URL mUrl;
    private List<NewsItem> mNews;

    NewsRefreshAsyncTaskLoader(Context context, String url) {
        super(context);
        mUrl = createURL(url);
    }

    @Override
    public List<NewsItem> loadInBackground() {
        List<NewsItem> result = new ArrayList<>();

        try {
            // get the raw (i.e. String) data from the remote server
            String data = fetchNewsItemsData(mUrl);
            // parse the raw data as JSON and transform it into a list of {@link NewsItem}
            result = NewsItems.fromJSON(data);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Failed to fetch news item data: " + e.getMessage());
        }

        return result;
    }

    @Override
    public void deliverResult(List<NewsItem> news) {
        if (isReset()) {
            Log.w(LOG_TAG, "An async query came in while the Loader was reset!");
            if (news != null) {
                releaseResources(news);
                return;
            }
        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        List<NewsItem> oldNews = mNews;
        mNews = news;

        if (isStarted()) {
            Log.d(LOG_TAG, "Delivering results to the LoaderManager for the ListFragment to display.");
            // If the Loader is in a started state, have the superclass deliver the
            // results to the client.
            super.deliverResult(news);
        }

        // Invalidate the old data as we don't need it any more.
        if (oldNews != null &&  oldNews != news) {
            Log.d(LOG_TAG, "Releasing any old data associated with this Loader.");
            releaseResources(oldNews);
        }
    }

    @Override
    protected void onReset() {
        Log.i(LOG_TAG, "onReset() called!");

        // Ensure the loader is stopped.
        onStopLoading();

        // At this point we can release the resources associated with 'news'.
        if (mNews != null) {
            releaseResources(mNews);
            mNews = null;
        }
    }

    @Override
    public void onCanceled(List<NewsItem> news) {
        Log.d(LOG_TAG, "onCanceled() called!");

        // Attempt to cancel the current asynchronous load.
        super.onCanceled(news);

        // The load has been canceled, so we should release the resources
        // associated with 'mNews'.
        releaseResources(news);
    }

    /**********************************************
     * Helper methods which handle network I/O
     *********************************************/
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

    private String fetchNewsItemsData(URL url) throws IOException {
        String response = "";
        HttpURLConnection connection = null;
        InputStream inStream = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10 * 1000);
            connection.setConnectTimeout(15 * 1000);
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inStream = connection.getInputStream();
                response = readFromStream(inStream);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Failed to fetch news item data: " + e.getMessage());
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
    private void releaseResources(List<NewsItem> news) {
        // For a simple List, there is nothing to do. For something like a Cursor,
        // we would close it in this method. All resources associated with the
        // Loader should be released here.
    }
}
