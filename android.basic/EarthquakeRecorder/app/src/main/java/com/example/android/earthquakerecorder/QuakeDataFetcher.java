package com.example.android.earthquakerecorder;

import com.example.android.earthquakerecorder.models.Earthquake;

import java.util.List;


public interface QuakeDataFetcher {
    String getURLString();
    void onQuakeListAvailable(List<Earthquake> quakeList);
}
