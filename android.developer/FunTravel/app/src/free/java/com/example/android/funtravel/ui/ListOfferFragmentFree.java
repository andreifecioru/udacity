package com.example.android.funtravel.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.android.funtravel.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


/**
 * Fragment implementation for pretty much all the screen content presented
 * in the {@link ListOffersActivity}.
 *
 * It comes in flavors (paid and free). This is the implementation for the "free"
 * flavor. This setup allows us to have the same parent activity with fragments
 * that are linked dynamically based on the product flavor.
 */
public class ListOfferFragmentFree extends Fragment {
    private static final String AD_MOB_SAMPLE_APP_ID = "ca-app-pub-3940256099942544~3347511713";

    public ListOfferFragmentFree() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_offers, container, false);

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
