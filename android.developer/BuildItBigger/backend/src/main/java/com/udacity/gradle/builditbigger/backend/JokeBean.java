package com.udacity.gradle.builditbigger.backend;

import com.android.example.joketeller.JokeTeller;

/** The object model for the data we are sending through endpoints */
public class JokeBean {

    public String getData() {
        return JokeTeller.tellJoke();
    }
}