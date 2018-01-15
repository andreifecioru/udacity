package com.example.android.tourguide;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.tourguide.models.Location;
import com.example.android.tourguide.models.Locations;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Activity which displays the details of a particular location.
 *
 * It's a simple mostly static UI, whose purpose is to demonstrate
 * the master-detail navigation pattern between activities.
 */
public class LocationDetailsActivity extends AppCompatActivity {
    public final static String LOCATION_KEY = "location";

    private final static String LOG_TAG = LocationDetailsActivity.class.getSimpleName();

    @BindView(R.id.location_img_view) ImageView mImageView;
    @BindView(R.id.media_play_img_view) ImageView mMediaPlayImageView;
    @BindView(R.id.location_name_text_view) TextView mNameTextView;
    @BindView(R.id.location_description_text_view) TextView mDescriptionTextView;
    @BindView(R.id.rating_container) ViewGroup mRatingContainer;

    private Location mLocation;

    private MediaPlayer mMediaPlayer;

    private AudioManager mAudioManager;

    private boolean mPlaybackInProgress = false;

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
                    startMediaPlayback();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    releaseMediaPlayer();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                    pauseMediaPlayback();
                    break;
                default:
                    Log.w(LOG_TAG, "Unknown audio focus change state: " + focusChange);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_details);

        if (savedInstanceState == null) {
            // obtain location info from the intent starting this activity.
            mLocation = (Location) getIntent().getSerializableExtra(LOCATION_KEY);
        } else {
            // restore location info from saved state.
            mLocation = (Location) savedInstanceState.getSerializable(LOCATION_KEY);
        }

        ButterKnife.bind(this);

        // setup the "up" navigation pattern
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // set the content for the various UI widgets
        mImageView.setImageResource(mLocation.getImageResId());
        mNameTextView.setText(mLocation.getName());
        mDescriptionTextView.setText(mLocation.getDescription());
        Locations.showRatingStars(mRatingContainer, mLocation);

        // not all locations have audio resources associated with them
        // (only 'concerts and events' locations do).
        if (mLocation.hasAudioResource()) {
            mMediaPlayImageView.setVisibility(View.VISIBLE);
        } else {
            mMediaPlayImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save the location info so we can recreate the activity content
        // on onCreate
        outState.putSerializable(LOCATION_KEY, mLocation);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // fix for navigation issue described here:
        // https://stackoverflow.com/questions/6554317/savedinstancestate-is-always-null
        if (item.getItemId()== android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Event handler method dealing with media playback.
     */
    @OnClick(R.id.media_play_img_view)
    public void onMediaPlayClick(View v) {
        // if location has no audio resource, there's nothing to be done.
        if (!mLocation.hasAudioResource()) return;

        mAudioManager = (AudioManager) this.getSystemService(AUDIO_SERVICE);
        if (mAudioManager != null) {
            int result = mAudioManager.requestAudioFocus(
                    mOnAudioFocusChanged,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                if (mMediaPlayer != null) {
                    // toggle between play/paused states
                    if (mPlaybackInProgress) {
                        pauseMediaPlayback();
                    } else {
                        startMediaPlayback();
                    }
                } else {
                    // create a new media player instance and start playback
                    mMediaPlayer = MediaPlayer.create(this, mLocation.getAudioResId());
                    mMediaPlayer.setOnCompletionListener(mOnPlaybackComplete);
                    startMediaPlayback();
                }
            }
        }
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            pauseMediaPlayback();
            mMediaPlayer.release();
            mAudioManager.abandonAudioFocus(mOnAudioFocusChanged);
            mMediaPlayer = null;
        }
    }

    private void startMediaPlayback() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            mPlaybackInProgress = true;
            mMediaPlayImageView.setImageResource(android.R.drawable.ic_media_pause);
        }
    }

    private void pauseMediaPlayback() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
            mPlaybackInProgress = false;
            mMediaPlayImageView.setImageResource(android.R.drawable.ic_media_play);
        }
    }
}
