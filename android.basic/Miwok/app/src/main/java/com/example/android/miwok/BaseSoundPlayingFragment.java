package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static android.content.Context.AUDIO_SERVICE;


abstract public class BaseSoundPlayingFragment extends ListFragment {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private MediaPlayer mMediaPlayer;

    private AudioManager mAudioManager;

    private Context mContext;

    private MediaPlayer.OnCompletionListener mOnPlaybackComplete = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChanged = new AudioManager.OnAudioFocusChangeListener() {
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();
        setListAdapter(getWordsAdapter(mContext));
    }

    @Override
    public void onStop() {
        super.onStop();

        releaseMediaPlayer();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Word word = (Word) l.getAdapter().getItem(position);
        if (word != null) {
            releaseMediaPlayer();

            mAudioManager = (AudioManager) mContext.getSystemService(AUDIO_SERVICE);
            if (mAudioManager != null) {
                int result = mAudioManager.requestAudioFocus(
                        mOnAudioFocusChanged,
                        AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mMediaPlayer = MediaPlayer.create(mContext, word.getMediaResourceId());
                    mMediaPlayer.setOnCompletionListener(mOnPlaybackComplete);
                    mMediaPlayer.start();
                }
            }
        }
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mAudioManager.abandonAudioFocus(mOnAudioFocusChanged);
            mMediaPlayer = null;
        }
    }

    abstract protected ArrayAdapter<Word> getWordsAdapter(Context context);
}
