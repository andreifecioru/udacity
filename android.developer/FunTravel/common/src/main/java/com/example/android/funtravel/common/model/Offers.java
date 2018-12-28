package com.example.android.funtravel.common.model;

import com.example.android.funtravel.common.fixtures.Fixtures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


public class Offers {
    private static final AtomicLong mOfferId = new AtomicLong(0L);

    public static List<Offer> generateOffers(int count) {
        if (count <= 0) return Collections.emptyList();

        List<Offer> result = new ArrayList<>();
        for (int i = 0; i < count; i ++) {
            result.add(generateOffer());
        }

        return result;
    }

    private static Offer generateOffer() {
        Offer offer = new Offer();
        offer.setId(mOfferId.incrementAndGet());
        offer.setTitle(Fixtures.getRandomOfferTitle());
        offer.setDescription(Fixtures.getRandomOfferDescription());
        offer.setType(Fixtures.getRandomOfferType());
        offer.setPrice(Fixtures.getRandomPrice());
        offer.setPhotoUrl(Fixtures.getRandomOfferPhotoUrl());
        offer.setAspectRatio(1.5f);
        offer.setVideoUrl(Fixtures.getRandomOfferVideoUrl());

        return offer;
    }

    private Offers() {
        throw new IllegalStateException("Utility class");
    }
}
