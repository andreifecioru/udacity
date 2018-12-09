package com.example.jokewizard;

import com.example.jokesmith.JokeSmith;

public class JokeWizard {
    public static String getJoke() {
        return JokeSmith.getAnotherJoke() + " (via JokeWizard)";
    }
}
