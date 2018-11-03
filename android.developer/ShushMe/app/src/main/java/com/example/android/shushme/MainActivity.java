package com.example.android.shushme;

/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.android.shushme.provider.PlaceContract.PlaceEntry;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

public class MainActivity
        extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
                   GoogleApiClient.OnConnectionFailedListener {

    // Constants
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private static final int PLACE_PICKER_REQUEST = 100;

    // Member variables
    private PlaceListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private CheckBox mLocationPermissionsCheckBox;
    private LinearLayout mLocationPermissionsContainer;
    private Button mAddPlaceButton;
    private GoogleApiClient mGoogleApiClient;

    /**
     * Called when the activity is starting
     *
     * @param savedInstanceState The Bundle that contains the data supplied in onSaveInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the recycler view
        mRecyclerView = findViewById(R.id.places_list_recycler_view);
        mLocationPermissionsCheckBox = findViewById(R.id.location_permissions);
        mLocationPermissionsContainer = findViewById(R.id.location_permissions_container);
        mAddPlaceButton = findViewById(R.id.add_place_button);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PlaceListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mLocationPermissionsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(LOG_TAG, "Location permissions changed: " + isChecked);
                if (isChecked) {
                    askForLocationPermissions();
                }
            }
        });

        mAddPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPlace();
            }
        });

        mLocationPermissionsCheckBox.setChecked(false);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, this)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();

        int visibilityStatus = isLocationPermissionGranted() ? View.GONE : View.VISIBLE;
        mLocationPermissionsContainer.setVisibility(visibilityStatus);
    }

    private boolean isLocationPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void askForLocationPermissions() {
        if (!isLocationPermissionGranted()) {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    REQUEST_LOCATION_PERMISSION);
        } else {
            mLocationPermissionsContainer.setVisibility(View.GONE);
            // TODO: get location
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Location permissions granted", Toast.LENGTH_SHORT).show();
                    mLocationPermissionsContainer.setVisibility(View.GONE);
                    // TODO: get location
                } else {
                    Toast.makeText(this, "Location permissions NOT granted", Toast.LENGTH_SHORT).show();
                    mLocationPermissionsContainer.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(LOG_TAG, "API Connection Successful!");
        refreshPlacesData();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG_TAG, "API Connection Suspended!");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOG_TAG, "API Connection Failed!");
    }

    private void pickPlace() {
        try {
            Intent intent = new PlacePicker.IntentBuilder().build(this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            Log.e(LOG_TAG, "GooglePlaceServices not available: " + e.getMessage());
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(LOG_TAG, "GooglePlaceServices not available: " + e.getMessage());
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to pick a place: " + e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(this, data);
                    if (place == null) {
                        Log.i(LOG_TAG, "No place selected");
                    } else {
                        Log.i(LOG_TAG, "A place was selected");

                        // Extract the place info (only the ID)
                        String placeId = place.getId();

                        // Insert the place info in the DB
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(PlaceEntry.COLUMN_PLACE_ID, placeId);
                        getContentResolver().insert(PlaceEntry.CONTENT_URI, contentValues);

                        refreshPlacesData();
                    }
                }
                break;
        }
    }

    private void refreshPlacesData() {
        // Retrieve all the place IDs we have in our local DB
        Cursor cursor = getContentResolver().query(
            PlaceEntry.CONTENT_URI, null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            String[] placeIds = new String[cursor.getCount()];
            int idx = 0;
            while (cursor.moveToNext()) {
                String placeId = cursor.getString(cursor.getColumnIndex(PlaceEntry.COLUMN_PLACE_ID));
                placeIds[idx ++] = placeId;
            }

            cursor.close();

            PendingResult<PlaceBuffer> pendingResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeIds);
            pendingResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                @Override
                public void onResult(@NonNull PlaceBuffer places) {
                    mAdapter.swapPlaces(places);
                }
            });
        }
    }
}
