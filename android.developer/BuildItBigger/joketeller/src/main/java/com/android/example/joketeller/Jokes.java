package com.android.example.joketeller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


class Jokes {
    private static final List<String> sJokes = new ArrayList<>();

    private static final String sQuestion = "Why did the chicken cross the road?\n";

    static {
        sJokes.add("She wanted to stretch her legs.");
        sJokes.add("She was afraid someone would Caesar!");
        sJokes.add("To get to the other slide.");
        sJokes.add("There was a car coming.");
        sJokes.add("To prove to the possum it could actually be done!");
        sJokes.add("The chicken cross the road in search of food.");
        sJokes.add("Because she realized that there is a rooster on the other side…");
        sJokes.add("To avoid lame and outdated jokes.");
        sJokes.add("I don’t know, let the chicken mind its own bussiness.");
        sJokes.add("To go to the idiot’s house.");
        sJokes.add("Because he could.");
        sJokes.add("So you could later make a website out of it !!");
        sJokes.add("To get a life…Now its time for all of you to get one.");
        sJokes.add("Because she saw what you did to her eggs");
        sJokes.add("No-one knows, but the road sure was pissed.");
        sJokes.add("Because it wanted to find out what those jokes were about.");
    }

    static String nextJoke() {
        int jokeIndex = ThreadLocalRandom.current().nextInt(sJokes.size());
        return sQuestion + sJokes.get(jokeIndex);
    }

    private Jokes() {
        throw new IllegalStateException("Utility class");
    }
}
