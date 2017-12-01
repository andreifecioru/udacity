package com.example.android.miwok;

import android.support.annotation.NonNull;

class Word {
    private String mMiwokTranslation;
    private String mDefaultTranslation;

    Word(String miwokTranslation, String defaultTranslation) {
        mMiwokTranslation = miwokTranslation;
        mDefaultTranslation = defaultTranslation;
    }

    @NonNull
    String getMiwokTranslation() { return mMiwokTranslation; }

    String getDefaultTranslation() { return mDefaultTranslation; }

    @Override
    public String toString() { return getMiwokTranslation() + " / " + getDefaultTranslation(); }
}
