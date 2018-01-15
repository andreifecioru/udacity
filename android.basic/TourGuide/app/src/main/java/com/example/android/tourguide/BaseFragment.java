package com.example.android.tourguide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.example.android.tourguide.models.Location;


/**
 * Abstract class representing the starting point for all the fragments
 * used by {@link MainActivity}.
 *
 * Encapsulates a {@link GridView} UI component which are rendered on the
 * screen using a set of view-cards.
 *
 * Clicking on any view card switches to the {@link LocationDetailsActivity}
 * (the appropriate {@link Location} instance is passed to it.
 *
 * Concrete child classed need to implement {@link BaseFragment#getLocationAdapter(Context)}
 * to provide the actual data (i.e. adapter for locations) for the fragment.
 */
abstract public class BaseFragment extends Fragment {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private Context mContext;

    private AdapterView.OnItemClickListener mOnGridViewItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Location location = (Location) parent.getAdapter().getItem(position);

            // pass the corresponding location instance to the location details activity
            Intent intent = new Intent(mContext, LocationDetailsActivity.class);
            intent.putExtra(LocationDetailsActivity.LOCATION_KEY, location);

            Log.i(LOG_TAG, "Switching to the location details activity.");
            startActivity(intent);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // save a reference to the app context.
        mContext = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.grid_view_one_col_fragment, container, false);

        // setup the grid-view
        GridView mLocationsGridView = rootView.findViewById(R.id.locations_grid_view);
        mLocationsGridView.setAdapter(getLocationAdapter(mContext));
        mLocationsGridView.setOnItemClickListener(mOnGridViewItemClick);

        return rootView;
    }

    /**
     *  Concrete child classes must implement this method to provide an array adapter
     *  for the list of locations that are displayed by the fragment.
     *
     *  @param context An instance of {@link Context} (useful for accessing app resources).
     *
     *  @return An {@link ArrayAdapter<Location>} instance providing the actual data to
     *  be displayed by the fragment.
     */
    abstract protected ArrayAdapter<Location> getLocationAdapter(Context context);
}
