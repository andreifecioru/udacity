package com.example.android.funtravel.utils;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.funtravel.R;
import com.example.android.funtravel.common.model.Offer;
import com.example.android.funtravel.common.model.OfferType;

public class OfferUtils {
    public static void showRatingStars(ViewGroup ratingContainerLayout, float starCount) {
        int i;

        int ratingStarCount = (int) starCount;

        // show full stars up to the rating value
        for (i = 0; i < ratingStarCount; i++) {
            ImageView imgView = (ImageView) ratingContainerLayout.getChildAt(i);
            imgView.setImageResource(android.R.drawable.star_on);
        }

        // all the remaining stars are empty
        for (; i< Offer.MAX_RATING; i++) {
            ImageView imgView = (ImageView) ratingContainerLayout.getChildAt(i);
            imgView.setImageResource(android.R.drawable.star_off);
        }
    }

    public static int getOfferTypeImageRes(Offer offer) {
        OfferType offerType = offer.getTypeAsEnum();

        switch (offerType) {
            case CITYSCAPE: return R.drawable.city_logo;
            case TREKKING: return R.drawable.trekking_logo;
            case RESORT: return R.drawable.resort_logo;
            default:
                throw new IllegalStateException("Invalid offer type:" + offerType);
        }
    }

    private OfferUtils() {
        throw new IllegalStateException("Utility class");
    }
}
