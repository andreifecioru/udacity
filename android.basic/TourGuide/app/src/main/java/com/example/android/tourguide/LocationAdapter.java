package com.example.android.tourguide;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.tourguide.models.Location;
import com.example.android.tourguide.models.Locations;

import java.util.List;


/**
 * Array adapter for the {@link Location} class. Fuels the grid-view
 * for the various location grid-views.
 */
public class LocationAdapter extends ArrayAdapter<Location> {
    private static class ViewHolder {
        ImageView locationImageView;
        TextView nameTextView;
        TextView descriptionTextView;
        ViewGroup ratingContainer;
    }

    /** Produces an instance of the {@link LocationAdapter} class (constructor)
     *
     * @param context Application context (used for accessing app resources)
     * @param locations A list of location objects (the raw data source)
     */
    LocationAdapter(Context context, List<Location> locations) {
        super(context, 0, locations);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // obtain the the location for the current item in the grid-view
        final Location location = getItem(position);

        if (location == null) return convertView;

        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.location_grid_view_item, parent, false);

            // cache the relevant views (so we don't have to search for them every time)
            viewHolder = new ViewHolder();
            viewHolder.locationImageView = convertView.findViewById(R.id.location_img_view);
            viewHolder.nameTextView = convertView.findViewById(R.id.location_name_text_view);
            viewHolder.descriptionTextView = convertView.findViewById(R.id.location_description_text_view);
            viewHolder.ratingContainer = convertView.findViewById(R.id.rating_container);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // populate the UI with the relevant data.
        viewHolder.locationImageView.setImageResource(location.getImageResId());
        viewHolder.nameTextView.setText(location.getName());
        viewHolder.descriptionTextView.setText(location.getDescription());
        Locations.showRatingStars(viewHolder.ratingContainer, location);

        return convertView;
    }
}
