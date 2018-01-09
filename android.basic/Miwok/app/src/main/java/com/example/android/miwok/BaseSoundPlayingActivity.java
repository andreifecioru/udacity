package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;


abstract class BaseSoundPlayingActivity extends AppCompatActivity {
    private static final String LOG_TAG = BaseSoundPlayingActivity.class.getSimpleName();
    @BindView(R.id.list) protected ListView mListView;

    protected ArrayAdapter<Word> mWordsAdapter;

    private MediaPlayer mMediaPlayer;

    private AudioManager mAudioManager;

    private OnCompletionListener mOnPlaybackComplete = new OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    private OnAudioFocusChangeListener mOnAudioFocusChanged = new OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    mMediaPlayer.start();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    releaseMediaPlayer();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                    mMediaPlayer.pause();
                    mMediaPlayer.seekTo(0);
                    break;
                default:
                    Log.w(LOG_TAG, "Unknown audio focus change state: " + focusChange);
            }
        }
    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_words);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mListView.setAdapter(mWordsAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = BaseSoundPlayingActivity.this;

                Word word = mWordsAdapter.getItem(position);
                if (word != null) {
                    releaseMediaPlayer();

                    mAudioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
                    if (mAudioManager != null) {
                        int result = mAudioManager.requestAudioFocus(
                                mOnAudioFocusChanged,
                                AudioManager.STREAM_MUSIC,
                                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                            mMediaPlayer = MediaPlayer.create(context, word.getMediaResourceId());
                            mMediaPlayer.setOnCompletionListener(mOnPlaybackComplete);
                            mMediaPlayer.start();
                        }
                    }

                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        releaseMediaPlayer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mAudioManager.abandonAudioFocus(mOnAudioFocusChanged);
            mMediaPlayer = null;
        }
    }
}
