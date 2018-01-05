package com.example.android.musicplayer;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mPlayer = MediaPlayer.create(this, R.raw.beat);

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(MainActivity.this, "Playback done.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.button_play)
    public void onPlayButtonClick(View v) {
        Log.i(LOG_TAG, "Play button pressed");
        mPlayer.start();
    }

    @OnClick(R.id.button_pause)
    public void onPauseButtonClick(View v) {
        Log.i(LOG_TAG, "Pause button pressed");
        mPlayer.pause();
    }
}
