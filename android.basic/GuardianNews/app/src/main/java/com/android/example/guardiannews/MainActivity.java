package com.android.example.guardiannews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.android.example.guardiannews.models.NewsItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements BaseFragment.OnRefreshNewsListener,
                   NewsListFragment.OnNewsItemClickListener,
                   LoaderManager.LoaderCallbacks<List<NewsItem>> {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int LOADER_ID = 1;

    /* We fetch 50 items per page. We also want set the 'show-tags=contributor' query param to access the author's info. */
    private static final String NEWS_FEED_URL = "https://content.guardianapis.com/search?from-date=2018-01-01&api-key=test&page-size=50&show-tags=contributor&show-fields=wordcount";
    private static final String NEWS_FEED_BASE_URL = "https://content.guardianapis.com/search";

    /**
     * Preferences:
     *  - results max count
     *  - section name: football, environment, culture, business, all.
     *  - sort by: newest, oldest, relevance
     * */

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        // integration between the activity and the navigation drawer.
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close);
        // set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        // set the navigation view events/handlers
        mNavigationView = findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.nav_menu_settings:
                        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(intent);
                    default:
                        Log.w(LOG_TAG, "Invalid menu selection: " + id);
                }
                return false;
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        // setup the async-loader
        getSupportLoaderManager().initLoader(LOADER_ID, null, MainActivity.this);

        // initiate download
        downloadNews();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mDrawerLayout.closeDrawers();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**********************************************
     * Helper methods
     *********************************************/
    private void downloadNews() {
        if (isOnline()) {
            showFragment(new DownloadSpinnerFragment());
            Loader<NewsItem> loader = getSupportLoaderManager().getLoader(LOADER_ID);
            if (loader != null) {
                loader.forceLoad();
            }
        } else { // if we're not online, just show the 'no-news' fragment (with the appropriate message)
            showNoNewsFragment(getResources().getString(R.string.not_online));
        }
    }

    // Displays the "no-news" fragment with a message provided by the caller
    private void showNoNewsFragment(String reason) {
        // pass-on the message to be shown on the screen
        Bundle bundle = new Bundle();
        bundle.putString(NoNewsFragment.REASON_KEY, reason);

        Fragment fragment = new NoNewsFragment();
        fragment.setArguments(bundle);

        showFragment(fragment);
    }

    // Displays the "news-list" fragment with the list of news items provided by the caller
    private void showNewsListFragment(List<NewsItem> newsItems) {
        if (newsItems.isEmpty()) { // if the list of news items is empty, just show the "no-news" fragment
            showNoNewsFragment(getResources().getString(R.string.check_later));
        } else {
            ArrayList<NewsItem> news = new ArrayList<>();
            news.addAll(newsItems);

            // pass-on the list of news item to be shown on the screen
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(NewsListFragment.NEWS_ITEM_LIST_KEY, news);

            Fragment fragment = new NewsListFragment();
            fragment.setArguments(bundle);

            showFragment(fragment);
        }
    }

    // Replaces the "content_frame" placeholder with the fragment provided by the caller
    private void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commitAllowingStateLoss(); // see https://stackoverflow.com/questions/22788684/can-not-perform-this-action-inside-of-onloadfinished
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

    /**********************************************
     * Fragment callbacks
     *  - implements the standard communication pattern with child fragments
     *    (see http://www.vogella.com/tutorials/AndroidFragments/article.html#fragments_activitycommunication)
     *********************************************/
    @Override
    public void onRefreshNews() { // called when the user taps the "refresh button" on the bottom of the screen
        downloadNews();
    }

    @Override
    public void onNewsItemClicked(String url) { // called when the user taps one of the news items in the list
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(android.net.Uri.parse(url));
        startActivity(intent);
    }

    /**********************************************
     * LoaderManager callbacks
     *********************************************/
    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, Bundle args) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        // let's build the URI based on the values of our preferences.
        String maxItemCount = prefs.getString(
                getString(R.string.settings_max_item_count_key),
                getString(R.string.settings_max_item_count_default));

        String orderBy = prefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        String section = prefs.getString(
                getString(R.string.settings_section_key),
                getString(R.string.settings_section_default));

        Uri baseUri = Uri.parse(NEWS_FEED_BASE_URL);

        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("api-key", "test");
        uriBuilder.appendQueryParameter("from-date", "2018-01-01");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("page-size", maxItemCount);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        if (!section.equalsIgnoreCase("all")) {
            // if we want a specific section, include the "section" filter.
            uriBuilder.appendQueryParameter("section", section);
        }

        return new NewsRefreshAsyncTaskLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> newsItems) {
        showNewsListFragment(newsItems);
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
        showNewsListFragment(new ArrayList<NewsItem>());
    }
}
