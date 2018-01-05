package com.example.android.miwok;

class Word {
    private static final int NO_IMAGE_PROVIDED = -1;

    final private String mMiwokTranslation;
    final private String mDefaultTranslation;
    final private int mIconDrawableResId;
    final private String mMediaResource;

    Word(String miwokTranslation, String defaultTranslation) {
        this(miwokTranslation, defaultTranslation, NO_IMAGE_PROVIDED, null);
    }

    Word(String miwokTranslation, String defaultTranslation, String mediaResource) {
        this(miwokTranslation, defaultTranslation, NO_IMAGE_PROVIDED, mediaResource);
    }

    Word(String miwokTranslation, String defaultTranslation, int iconDrawableResId) {
        this(miwokTranslation, defaultTranslation, iconDrawableResId, null);
    }

    Word(String miwokTranslation, String defaultTranslation, int iconDrawableResId, String mediaResource) {
        mMiwokTranslation = miwokTranslation;
        mDefaultTranslation = defaultTranslation;
        mIconDrawableResId = iconDrawableResId;
        mMediaResource = mediaResource;
    }

    String getMiwokTranslation() { return mMiwokTranslation; }

    String getDefaultTranslation() { return mDefaultTranslation; }

    int getIconDrawableResId() { return mIconDrawableResId; }

    String getMediaResource() { return mMediaResource; }

    boolean hasImage() { return mIconDrawableResId != NO_IMAGE_PROVIDED; }

    @Override
    public String toString() { return getMiwokTranslation() + " / " + getDefaultTranslation(); }
}
