package com.example.android.shushme;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

class GeofencingUtils {
    private static final String LOG_TAG = GeofencingUtils.class.getSimpleName();

    private final Context mContext;
    private final GeofencingClient mClient;

    private PendingIntent mPendingIntent;
    private final List<Geofence> mGeofenceList = new ArrayList<>();

    private static final long EXPIRATION_24_HOURS = 24 * 3600 * 1000;
    private static final float GEOFENCE_RADIUS = 50.0f;

    GeofencingUtils(Context context) {
        mContext = context;
        mClient = LocationServices.getGeofencingClient(mContext);
    }

    void updateGeofenceList(PlaceBuffer places) {
        // reset the geofence list
        mGeofenceList.clear();

        if (places == null || places.getCount() == 0) return;

        for (Place place : places) {
            Geofence geofence = new Geofence.Builder()
                // use the place ID as the unique ID for the geofence
                .setRequestId(place.getId())
                // set an expiration data for this geofence
                // NOTE:
                //   - setup a job scheduler to re-register this geofence
                //   - be careful when using NEVER_EXPIRE
                .setExpirationDuration(EXPIRATION_24_HOURS)
                // set the geofence location and radius
                .setCircularRegion(
                        place.getLatLng().latitude,
                        place.getLatLng().longitude,
                        GEOFENCE_RADIUS)
                // choose what type of transitions to watch for
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                // finally build the object
                .build();

            mGeofenceList.add(geofence);
        }
    }

    void registerAllGeofences() {
        // sanity checks
        if (mGeofenceList.size() == 0) return;

        try {
            mClient
                .addGeofences(buildGeofencingRequest(),
                              buildGeofencePendingIntent())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(LOG_TAG, "Geofences registered successfully.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(LOG_TAG, "Failed to register geofences: " + e.getMessage());
                    }
                });
        } catch (SecurityException e) {
            // this is triggered when the app does not have ACCESS_FINE_LOCATION permission
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    void unregisterAllGeofences() {
        try {
            mClient
                .removeGeofences(buildGeofencePendingIntent())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(LOG_TAG, "Geofences unregistered successfully.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(LOG_TAG, "Failed to unregister geofences: " + e.getMessage());
                    }
                });
        } catch (SecurityException e) {
            // this is triggered when the app does not have ACCESS_FINE_LOCATION permission
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    private GeofencingRequest buildGeofencingRequest() {
        return new GeofencingRequest.Builder()
            // if the device is already inside the geofencing area, trigger an ENTRY event
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofences(mGeofenceList)
            .build();
    }

    private PendingIntent buildGeofencePendingIntent() {
        // reuse the pending intent if possible
        if (mPendingIntent != null) return mPendingIntent;

        Intent intent = new Intent(mContext, GeofenceBroadcastReceiver.class);
        mPendingIntent = PendingIntent.getBroadcast(mContext, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return mPendingIntent;
    }
}
