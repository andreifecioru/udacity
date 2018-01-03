package com.example.android.jukebox.models;

import java.io.Serializable;
import java.util.Locale;

public class Song implements Serializable {
    private Album mAlbum;
    private String mTitle;
    private int mDuration;
    private int mRating;
    private int mTrackNumber;

    Song(Album album, String title, int duration, int rating, int trackNumber) {
        mAlbum = album;
        mTitle = title;
        mDuration = duration;
        mRating = rating;
        mTrackNumber = trackNumber;
    }

    public String getArtist() { return mAlbum.getArtist(); }

    public int getAlbumArtResId() { return mAlbum.getAlbumArtResId(); }

    public String getAlbumTitle() { return mAlbum.getTitle(); }

    public String getTitle() { return mTitle; }

    public String getDuration() {
        int minutes = mDuration / 60;
        int seconds = mDuration - minutes * 60;

        if (seconds < 10) {
            return String.format(Locale.ENGLISH, "%d:0%d", minutes, seconds);
        }
        return String.format(Locale.ENGLISH, "%d:%d", minutes, seconds);
    }

    public int getRating() { return mRating; }

    public String getTrackNumber() {
        if (mTrackNumber < 10) return "0" + mTrackNumber;
        return Integer.toString(mTrackNumber);
    }
}
