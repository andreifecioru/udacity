package com.example.jokedisplay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class JokeDisplayActivity extends AppCompatActivity {

    public static String JOKE_TEXT_KEY = "joke.text.key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke_display);

        Intent intent = getIntent();
        String jokeText = intent.getStringExtra(JOKE_TEXT_KEY);

        TextView jokeTextView = findViewById(R.id.tv_joke_text);
        jokeTextView.setText(jokeText);
    }
}
