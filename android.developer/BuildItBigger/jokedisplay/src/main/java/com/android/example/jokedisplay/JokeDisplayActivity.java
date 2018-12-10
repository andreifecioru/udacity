package com.android.example.jokedisplay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.example.jokedisplay.models.Joke;

public class JokeDisplayActivity extends AppCompatActivity {
    private static final String LOG_TAG = JokeDisplayActivity.class.getSimpleName();

    public static final String JOKE_EXTRA_KEY = "joke.extra.key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke_display);

        Intent intent = getIntent();
        Joke mJoke = intent.getParcelableExtra(JOKE_EXTRA_KEY);

        if (mJoke == null) {
            // No joke was passed to us: just exit
            finish();
            return;
        }

        Log.d(LOG_TAG, "Displaying joke: " + mJoke);

        // Butterknife not working in library modules (can't resolve R).
        TextView mJokeTextView = findViewById(R.id.tv_joke_text);
        mJokeTextView.setText(mJoke.getText());
    }
}
