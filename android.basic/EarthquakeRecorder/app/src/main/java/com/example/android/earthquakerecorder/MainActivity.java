package com.example.android.earthquakerecorder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.earthquakerecorder.models.Earthquake;
import com.example.android.earthquakerecorder.models.Earthquakes;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int LOADER_ID = 1;

    private List<Earthquake> mQuakeList = new ArrayList<>();
    private ArrayAdapter<Earthquake> mAdapter;

    @BindView(R.id.quake_list_view) ListView mQuakeListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mAdapter = new EarthquakeAdapter( this, mQuakeList);

        mQuakeListView.setAdapter(mAdapter);
        mQuakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Earthquake quake = (Earthquake) parent.getAdapter().getItem(position);

                if (quake != null) {
                    String url = quake.getUrl();

                    Log.d(LOG_TAG, "Opening URL: " + url);

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(android.net.Uri.parse(url));
                    parent.getContext().startActivity(intent);
                }
            }
        });

        getSupportLoaderManager().initLoader(LOADER_ID, null, MainActivity.this);
        downloadQuakes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void downloadQuakes() {
        if (isOnline()) {
            View loadingProgressView = getLayoutInflater().inflate(R.layout.progress_spinner, null, false);
            mQuakeListView.setEmptyView(loadingProgressView);
            addContentView(
                    loadingProgressView,
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT));

            Loader<Earthquake> loader = getSupportLoaderManager().getLoader(LOADER_ID);
            if (loader != null) {
                loader.forceLoad();
            }
        } else {
            showNoQuakesContainer(R.string.no_connectivity);
        }
    }

    // Checks if we have connectivity or not.
    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null) {
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }

    private void updateUI(List<Earthquake> quakes) {
        View progressBarView = findViewById(R.id.loading_progress_bar);
        if (progressBarView !=  null) {
            ((ViewGroup) progressBarView.getParent()).removeView(progressBarView);
        }

        if (quakes.isEmpty()) {
            showNoQuakesContainer(R.string.no_quakes);
        } else {
            mAdapter.clear();
            mAdapter.addAll(quakes);
        }
    }

    private void showNoQuakesContainer(int messageResId) {
        mAdapter.clear();
        View noQuakesContainer = getLayoutInflater().inflate(R.layout.no_quakes, null, false);
        TextView noQuakesTextView = noQuakesContainer.findViewById(R.id.no_quakes_text_view);
        noQuakesTextView.setText(messageResId);
        addContentView(
                noQuakesContainer,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));

        mQuakeListView.setEmptyView(noQuakesContainer);
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        String minMagnitude = prefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = prefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));



        Uri baseUri = Uri.parse(Earthquakes.QUAKE_DATA_URL);

        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "20");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);


        return new QuakeLoader(MainActivity.this, uriBuilder.toString());
    }


    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> quakes) {
        updateUI(quakes);
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        updateUI(new ArrayList<Earthquake>());
    }
}
