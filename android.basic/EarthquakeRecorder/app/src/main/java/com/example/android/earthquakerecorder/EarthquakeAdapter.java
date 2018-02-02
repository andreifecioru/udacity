package com.example.android.earthquakerecorder;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.earthquakerecorder.models.Earthquake;

import java.util.List;


public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {
    private static class ViewHolder {
        TextView magTextView;
        TextView locationTextView;
        TextView placeTextView;
        TextView dateTextView;
        TextView timeTextView;
    }

    public EarthquakeAdapter(Context context, List<Earthquake> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Earthquake quake = getItem(position);

        if (quake == null) return convertView;

        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.quake_list_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.magTextView = convertView.findViewById(R.id.mag_text_view);
            viewHolder.locationTextView = convertView.findViewById(R.id.location_text_view);
            viewHolder.placeTextView = convertView.findViewById(R.id.place_text_view);
            viewHolder.dateTextView = convertView.findViewById(R.id.date_text_view);
            viewHolder.timeTextView = convertView.findViewById(R.id.time_text_view);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.magTextView.setText(quake.getMagnitude());
        int magColor = getMagnitudeColor(quake.getmMagnitudeValue());
        ((GradientDrawable) viewHolder.magTextView.getBackground()).setColor(magColor);

        viewHolder.locationTextView.setText(quake.getLocation());
        viewHolder.placeTextView.setText(quake.getPlace());
        viewHolder.dateTextView.setText(quake.getDate());
        viewHolder.timeTextView.setText(quake.getTime());

        return convertView;
    }

    private int getMagnitudeColor(double magnitude) {
        int magColorResId;

        if (magnitude < 2) {
            magColorResId = R.color.magnitude1;
        } else if (magnitude < 3) {
            magColorResId = R.color.magnitude2;
        } else if (magnitude < 4) {
            magColorResId = R.color.magnitude3;
        } else if (magnitude < 5) {
            magColorResId = R.color.magnitude4;
        } else if (magnitude < 6) {
            magColorResId = R.color.magnitude5;
        } else if (magnitude < 7) {
            magColorResId = R.color.magnitude6;
        } else if (magnitude < 8) {
            magColorResId = R.color.magnitude7;
        } else if (magnitude < 9) {
            magColorResId = R.color.magnitude8;
        } else if (magnitude < 10) {
            magColorResId = R.color.magnitude9;
        } else {
            magColorResId = R.color.magnitude10plus;
        }

        return ContextCompat.getColor(getContext(), magColorResId);
    }
}
