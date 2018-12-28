package com.example.android.funtravel.common.model;

public enum OfferType {
    CITYSCAPE("cityscape"),
    TREKKING("trekking"),
    RESORT("resort");

    private final String mType;

    OfferType(String type) {
        mType = type;
    }

    public String asString() {
        return mType;
    }

    public static OfferType fromString(String text) {
        for (OfferType offerType: OfferType.values()) {
            if (offerType.mType.equalsIgnoreCase(text)) {
                return offerType;
            }
        }
        return null;
    }
}

