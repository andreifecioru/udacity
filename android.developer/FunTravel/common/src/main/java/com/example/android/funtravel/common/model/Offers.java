package com.example.android.funtravel.common.model;

public class Offers {

    public static Offer create(String title, String description, OfferType type,
                               float price, String photoUrl, String videoUrl) {
        Offer offer = new Offer();
        offer.setId(0L);
        offer.setTitle(title);
        offer.setDescription(description);
        offer.setType(type.asString());
        offer.setPrice(price);
        offer.setPhotoUrl(photoUrl);
        offer.setAspectRatio(1.5f);
        offer.setVideoUrl(videoUrl);

        return offer;
    }

    private Offers() {
        throw new IllegalStateException("Utility class");
    }
}
