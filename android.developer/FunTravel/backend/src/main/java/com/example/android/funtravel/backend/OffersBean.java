package com.example.android.funtravel.backend;

import java.util.List;

import com.example.android.funtravel.common.model.Offer;


/** The object model for the offer data we are sending through endpoints */
public class OffersBean {
    private final int mCount;
    private final int mFlags;

    OffersBean(int count, int flags) {
        mCount = count;
        mFlags = flags;
    }

    public List<Offer> getOffers() {
        return InMemoryDB.listOffers(mCount, mFlags);
    }
}