package com.example.android.miwok;

class Word {
    private static final int NO_IMAGE_PROVIDED = -1;

    final private String mMiwokTranslation;
    final private String mDefaultTranslation;
    final private int mIconDrawableResId;
    final private int mMediaResourceId;

    static Word createWord(String miwokTranslation, String defaultTranslation, int iconDrawableResId, int mediaResourceId) {
        return new Word(miwokTranslation, defaultTranslation, iconDrawableResId, mediaResourceId);
    }

    static Word createWordWithoutImage(String miwokTranslation, String defaultTranslation, int mediaResourceId) {
        return new Word(miwokTranslation, defaultTranslation, NO_IMAGE_PROVIDED, mediaResourceId);
    }

    private Word(String miwokTranslation, String defaultTranslation, int iconDrawableResId, int mediaResourceId) {
        mMiwokTranslation = miwokTranslation;
        mDefaultTranslation = defaultTranslation;
        mIconDrawableResId = iconDrawableResId;
        mMediaResourceId = mediaResourceId;
    }

    String getMiwokTranslation() { return mMiwokTranslation; }

    String getDefaultTranslation() { return mDefaultTranslation; }

    int getIconDrawableResId() { return mIconDrawableResId; }

    int getMediaResourceId() { return mMediaResourceId; }

    boolean hasImage() { return mIconDrawableResId != NO_IMAGE_PROVIDED; }

    @Override
    public String toString() { return getMiwokTranslation() + " / " + getDefaultTranslation(); }
}
