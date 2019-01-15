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

    public boolean checkFlags(int flags) {
        switch (this) {
            case CITYSCAPE: return (flags & Flags.CITYSCAPE) == Flags.CITYSCAPE;
            case TREKKING: return (flags & Flags.TREKKING) == Flags.TREKKING;
            case RESORT: return (flags & Flags.RESORT) == Flags.RESORT;

            default: throw new IllegalStateException("Unknown offer type: "+ mType);
        }
    }

    public static class Flags {
        public static final int CITYSCAPE = 1;
        public static final int TREKKING = 1 << 1;
        public static final int RESORT = 1 << 2;

        public static final int ALL = CITYSCAPE | TREKKING | RESORT;
        public static final int NONE = 0;

        private Flags() {
            throw new IllegalStateException("Cannot be instantiated.");
        }
    }
}

