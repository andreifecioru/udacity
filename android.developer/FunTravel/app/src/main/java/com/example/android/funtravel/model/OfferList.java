package com.example.android.funtravel.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.example.android.funtravel.common.model.Offer;


/**
 * Wrapper container over the {@link Offer} abstraction needed to help retrofit
 * parse the backend response which has the form:
 *
 * { offers: [...] }
 */
public class OfferList {
    @SerializedName("offers")
    @Expose
    private List<? extends ParcelableOffer> mOffers;

    public List<? extends ParcelableOffer> getOffers() { return mOffers; }
}
