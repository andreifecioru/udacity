package com.example.android.earthquakerecorder;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.earthquakerecorder.models.Earthquake;
import com.example.android.earthquakerecorder.models.Earthquakes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class QuakeAsyncTask extends AsyncTask<QuakeDataFetcher, Void, List<Earthquake>> {
    private final static String LOG_TAG = QuakeAsyncTask.class.getSimpleName();

    private QuakeDataFetcher mQuakeFetcher;
    private OkHttpClient mHttpClient = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();

    @Override
    protected List<Earthquake> doInBackground(QuakeDataFetcher... fetchers) {
        List<Earthquake> result = new ArrayList<>();
        if (fetchers.length != 1) {
            Log.e(LOG_TAG, "Expected exactly one quake data fetcher (got " + fetchers.length + ").");
            return result;
        }

        mQuakeFetcher = fetchers[0];
        try {
            String data = fetchQuakeData(mQuakeFetcher.getURLString());
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

    private String fetchQuakeData(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = mHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body != null) {
                    return body.string();
                }
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Failed to fetch quake data: " + e.getMessage());
        }

        return "";
    }
}
