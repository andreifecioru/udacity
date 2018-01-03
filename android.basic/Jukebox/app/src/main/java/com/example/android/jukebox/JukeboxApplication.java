package com.example.android.jukebox;

import android.app.Application;

import com.example.android.jukebox.models.MusicLibrary;


public class JukeboxApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MusicLibrary.getInstance();
    }
}
