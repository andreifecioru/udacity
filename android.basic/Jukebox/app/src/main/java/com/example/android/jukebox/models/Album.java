package com.example.android.jukebox.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Album implements Serializable {
    private List<Song> mSongs = new ArrayList<>();
    private String mTitle;
    private String mArtist;
    private int mYear;
    private int mAlbumArtResId;

    Album(String title, String artist, int year, int albumArtResId) {
        mTitle = title;
        mArtist = artist;
        mYear = year;
        mAlbumArtResId = albumArtResId;
    }

    public List<Song> getSongs() { return mSongs; }

    public String getTitle() { return mTitle; }

    public String getArtist() { return mArtist; }

    private int getYear() { return mYear; }

    public int getAlbumArtResId() { return mAlbumArtResId; }

    void addSong(String title, int duration, int rating) {
        int trackNumber = mSongs.size() + 1;
        mSongs.add(new Song(this, title, duration, rating, trackNumber));
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%s by %s (%d)", getTitle(), getArtist(), getYear());
    }
}
