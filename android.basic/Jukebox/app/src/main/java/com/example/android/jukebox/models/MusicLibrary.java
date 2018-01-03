package com.example.android.jukebox.models;

import android.util.Log;

import com.example.android.jukebox.R;

import java.util.ArrayList;
import java.util.List;


public class MusicLibrary {
    private static final String LOG_TAG = MusicLibrary.class.getSimpleName();

    private List<Album> mAlbums = new ArrayList<>();
    private static MusicLibrary mInstance;

    public static MusicLibrary getInstance() {
        if (mInstance == null) {
            mInstance = new MusicLibrary();
        }

        return mInstance;
    }

    private MusicLibrary() {
        buildLibrary();
    }

    public List<Album> getAlbums() { return mAlbums; }

    private void addAlbum(Album album) {
        mAlbums.add(album);
    }

    private void buildLibrary() {
        Album album;

        Log.i(LOG_TAG, "Building the music library.");

        // Depeche Mode - Violator
        album = new Album("Violator", "Depeche Mode", 1990, R.drawable.aa_dm_violator);
        album.addSong("World In My Eyes", 266, 4);
        album.addSong("Sweetest Perfection", 283, 2);
        album.addSong("Personal Jesus", 296, 5);
        album.addSong("Halo", 270, 3);
        album.addSong("World In My Eyes", 266, 4);
        album.addSong("Waiting For The Night", 367, 2);
        album.addSong("Enjoy The Silence", 373, 5);
        album.addSong("Policy Of Truth", 295, 3);
        album.addSong("Blue Dress", 342, 1);
        album.addSong("Clean", 328, 3);

        addAlbum(album);

        // Metallica - Ride The Lightning
        album = new Album("Ride The Lightning", "Metallica",2010, R.drawable.aa_metallica_rtl);
        album.addSong("Fight Fire With Fire", 285, 4);
        album.addSong("Ride The Lightning", 397, 2);
        album.addSong("For Whom The Bell Tolls", 311, 5);
        album.addSong("Fade To Black", 415, 3);
        album.addSong("Trapped Under Ice", 244, 4);
        album.addSong("Escape", 264, 2);
        album.addSong("Creeping Death", 397, 5);
        album.addSong("The Call Of Ktulu", 535, 3);

        addAlbum(album);

        // Queen - Absolute Greatest
        album = new Album("Absolute Greatest", "Queen", 2009, R.drawable.aa_queen_greatest);
        album.addSong("We Will Rock You", 266, 4);
        album.addSong("We Are The Champions", 283, 2);
        album.addSong("Radio Ga Ga", 296, 5);
        album.addSong("Another One Bites The Dust", 270, 3);
        album.addSong("I Want It All", 266, 4);
        album.addSong("A Kind Of Magic", 367, 2);
        album.addSong("Under Pressure", 373, 5);
        album.addSong("Somebody To Love", 295, 3);

        addAlbum(album);

        // The Beatles - Revolver
        album = new Album("Revolver", "The Beatles", 2014, R.drawable.aa_beatles_revolver);
        album.addSong("Taxman", 266, 4);
        album.addSong("I'm Only Sleeping", 283, 2);
        album.addSong("Love You To", 296, 5);
        album.addSong("Here, There and Everywhere", 270, 3);
        album.addSong("Yellow Submarine", 266, 4);
        album.addSong("She Said She Said", 367, 2);
        album.addSong("Good Day Sunshine", 373, 5);
        album.addSong("I Want To Tell You", 295, 3);

        addAlbum(album);

        // Guns N'Roses - Appetite For Destruction
        album = new Album("Appetite For Destruction", "Guns N'Roses", 1987, R.drawable.aa_guns_appetite);
        album.addSong("Welcome To The Jungle", 266, 4);
        album.addSong("It's So Easy", 283, 2);
        album.addSong("Paradise City", 296, 5);
        album.addSong("Think About You", 270, 3);
        album.addSong("Sweet Child O'Mine", 266, 4);
        album.addSong("You're Crazy", 367, 2);
        album.addSong("Anything Goes", 373, 5);
        album.addSong("Rocket Queen", 295, 3);

        addAlbum(album);

        // George Michael - Faith
        album = new Album("Faith", "George Michael", 1987, R.drawable.aa_gm_faith);
        album.addSong("Faith", 266, 4);
        album.addSong("Father Figure", 283, 2);
        album.addSong("One More Try", 296, 5);
        album.addSong("Hard Day", 270, 3);
        album.addSong("Hand To Mouth", 266, 4);
        album.addSong("Look At Your Hands", 367, 2);
        album.addSong("Monkey", 373, 5);
        album.addSong("Kissing A Fool", 295, 3);

        addAlbum(album);
    }
}
