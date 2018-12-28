package com.example.android.funtravel.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OfferList {
    @SerializedName("offers")
    @Expose
    private List<? extends ParcelableOffer> mOffers;

    public List<? extends ParcelableOffer> getOffers() { return mOffers; }
}
