package com.android.example.joketeller;

public class JokeTeller {
    public static String tellJoke() {
        return Jokes.nextJoke();
    }
}
