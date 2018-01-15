package com.example.android.tourguide;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Application's main (and only) activity. Implements the 'navigation drawer'
 * navigation pattern.
 *
 * Content is changed by swapping-in dynamically various fragments based
 * on the current selection in the navigation menu.
 * */
public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String SELECTED_MENU_ITEM_KEY = "selected_menu_item";

    private ActionBarDrawerToggle mDrawerToggle;

    private int mSelectedMenuId;

    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view) NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // restore the correct fragment based on the saved state
        if (savedInstanceState != null) {
            mSelectedMenuId = savedInstanceState.getInt(SELECTED_MENU_ITEM_KEY);
        } else {
            // if no saved state exists, default to the 'park and gardens' fragment.
            mSelectedMenuId = R.id.nav_menu_parks_and_gardens;
        }

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);

                // select the fragment corresponding to the selected item
                // in the navigation menu.
                selectFragment(item.getItemId());

                return false;
            }
        });

        // integration between the activity and the navigation drawer.
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close);

        // set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // sync the current fragment with the current navigation menu
        // selection every time the activity comes to the foreground.
        selectFragment(mSelectedMenuId);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // sync the navigation menu with the changes in config
        // (like changing device orientation)
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // save the nav menu item selection to reconstruct
        // the state of the activity during onCreate.
        outState.putInt(SELECTED_MENU_ITEM_KEY, mSelectedMenuId);

        super.onSaveInstanceState(outState);
    }

    /**
     * Dynamically load the appropriate fragment based on the nav-menu
     * item selection.
     *
     * @param menuItemId the ID of the selection nav-menu item.
     */
    private void selectFragment(int menuItemId) {
        Fragment fragment;
        String title;

        switch (menuItemId) {
            case R.id.nav_menu_parks_and_gardens:
                fragment = new ParksAndGardensFragment();
                title = getString(R.string.parks_and_gardens);
                break;
            case R.id.nav_menu_museums:
                fragment = new MuseumsFragment();
                title = getString(R.string.museums);
                break;
            case R.id.nav_menu_concerts_and_events:
                fragment = new ConcertsAndEventsFragment();
                title = getString(R.string.concerts_and_events);
                break;
            case R.id.nav_menu_restaurants:
                fragment = new RestaurantsFragment();
                title = getString(R.string.restaurants);
                break;
            default:
                Log.w(LOG_TAG, "Unsupported menu id for fragment selection: " + menuItemId);
                fragment = null;
                title = null;
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();

            setTitle(title);
            mDrawerLayout.closeDrawer(Gravity.START);
            mSelectedMenuId = menuItemId;
        }
    }
}
