package com.example.android.earthquakerecorder.models;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Earthquake {
    private final static String LOCATION_NEAR = "NEAR THE";

    private final String mPlace;
    private final String mLocation;
    private final double mMagnitude;
    private final String mDate;
    private final String mTime;
    private final String mUrl;

    Earthquake(String place, double magnitude, long time, String url) {
        mMagnitude = magnitude;
        mUrl = url;


        // parse place and location
        if (place.contains(" of ")) {
            String[] items = place.split(" of ");
            mLocation = items[0] + " OF ";
            mPlace = items[1];
        } else {
            mLocation = LOCATION_NEAR;
            mPlace = place;
        }

        // parse date and time
        Date date = new Date(time);
        SimpleDateFormat fmt = new SimpleDateFormat("MMM d, yyyy", Locale.US);
        mDate = fmt.format(date);

        fmt = new SimpleDateFormat("h:mm a", Locale.US);
        mTime = fmt.format(date);
    }

    public String getPlace() { return mPlace; }

    public String getUrl() { return mUrl; }

    public String getLocation() { return mLocation.toUpperCase(); }

    public String getMagnitude() {
        DecimalFormat fmt = new DecimalFormat("0.0");
        return fmt.format(mMagnitude);
    }

    public double getmMagnitudeValue() { return mMagnitude; }

    public String getTime() { return mTime; }

    public String getDate() { return mDate; }

    @Override
    public String toString(){
        return String.format(Locale.ENGLISH, "%s (%s)", mPlace, String.valueOf(mMagnitude));
    }
}
