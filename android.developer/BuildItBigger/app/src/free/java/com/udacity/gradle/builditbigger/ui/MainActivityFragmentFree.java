package com.udacity.gradle.builditbigger.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.udacity.gradle.builditbigger.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragmentFree extends Fragment {
    private static final String AD_MOB_SAMPLE_APP_ID = "ca-app-pub-3940256099942544~3347511713";

    public MainActivityFragmentFree() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        Context context = getContext();
        if (context != null) {
            // Sample AdMob app ID:
            MobileAds.initialize(context, AD_MOB_SAMPLE_APP_ID);

            AdView mAdView = root.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
            mAdView.loadAd(adRequest);
        }

        return root;
    }
}
