package com.example.android.funtravel.backend;

import java.util.List;

import com.example.android.funtravel.common.model.Offer;


/** The object model for the offer data we are sending through endpoints */
public class OffersBean {
    private final int mCount;

    OffersBean(int count) {
        mCount = count;
    }

    public List<Offer> getOffers() {
        return InMemoryDB.listOffers(mCount);
    }
}