package com.example.android.earthquakerecorder;

import android.os.AsyncTask;
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

public class QuakeAsyncTask extends AsyncTask<QuakeDataFetcher, Void, List<Earthquake>> {
    private final static String LOG_TAG = QuakeAsyncTask.class.getSimpleName();

    private QuakeDataFetcher mQuakeFetcher;

    @Override
    protected List<Earthquake> doInBackground(QuakeDataFetcher... fetchers) {
        List<Earthquake> result = new ArrayList<>();
        if (fetchers.length != 1) {
            Log.e(LOG_TAG, "Expected exactly one quake data fetcher (got " + fetchers.length + ").");
            return result;
        }

        mQuakeFetcher = fetchers[0];
        URL url = createURL(mQuakeFetcher.getURLString());
        try {
            String data = fetchQuakeData(url);
            result = Earthquakes.fromJSON(data);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Failed to fetch quake data: " + e.getMessage());
        }

        return result;
    }

    protected void onPostExecute(List<Earthquake> result) {
        Log.i(LOG_TAG, "Fetched " + result.size() + " quake events.");
        if (mQuakeFetcher != null) {
            mQuakeFetcher.onQuakeListAvailable(result);
        }
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
        String response = "";
        HttpURLConnection connection = null;
        InputStream inStream = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10 * 1000);
            connection.setConnectTimeout(15 * 1000);
            connection.connect();
            inStream = connection.getInputStream();
            response = readFromStream(inStream);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Failed to fetch quake data: " + e.getMessage());
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
}
