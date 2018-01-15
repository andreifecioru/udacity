package com.example.android.tourguide;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.android.tourguide.models.Location;
import com.example.android.tourguide.models.Locations;


/**
 * Concrete implementation of the {@link BaseFragment} abstract class
 *
 * Implements the UI fragment for displaying the 'parks and gardens' locations.
 */
public class ParksAndGardensFragment extends BaseFragment {

    @Override
    protected ArrayAdapter<Location> getLocationAdapter(Context context) {
        // pass in the 'parks and gardens' locations.
        return new LocationAdapter(context, Locations.parksAndGardens(context));
    }
}
