package com.android.example.bakingapp.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.example.bakingapp.R;
import com.android.example.bakingapp.models.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A custom {@link Fragment} implementation displaying the UI for the detailed info about a
 * preparation step.
 *
 * Displays the preparation-step instructions in video-form (if available) and text-form (below
 * the video.
 *
 * NOTE: the screen ratios between video and text instructions differ slightly when running
 * in tablet vs. mobile mode.
 */
public class StepDetailsFragment
        extends Fragment
        implements ExoPlayer.EventListener {
    private static final String LOG_TAG = StepDetailsFragment.class.getSimpleName();

    public static final String STEP_ARGS_KEY = "step.args.key";
    public static final String STEP_ARGS_TABLET_MODE_KEY = "step.args.tablet.mode.key";

    private static final String PLAYER_POSITION_KEY = "player.position.key";
    private static final String PLAYER_PLAY_WHEN_READY_KEY = "player.play.when.ready.key";

    @BindView(R.id.tv_step_description) TextView mDescriptionTextView;
    @BindView(R.id.exo_player_view) SimpleExoPlayerView mPlayerView;
    @BindView(R.id.iv_no_video) ImageView mNoVideoImageView;

    private Step mStep;
    private SimpleExoPlayer mPlayer;
    private boolean mTabletModeOn;
    private boolean mPlayWhenReady = true;
    private long mPlayerPosition = 0L;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            mStep = args.getParcelable(STEP_ARGS_KEY);
            if (mStep != null) {
                Log.d(LOG_TAG, mStep.toString());
            }

            mTabletModeOn = args.getBoolean(STEP_ARGS_TABLET_MODE_KEY, false);
        }

        if (mStep == null) {
            throw new IllegalStateException("Step cannot be null");
        }

        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);

        ButterKnife.bind(this, rootView);

        mDescriptionTextView.setText(mStep.getDescription());

        Bitmap defaultArtWork = BitmapFactory.decodeResource(getResources(), R.drawable.pie);
        mPlayerView.setDefaultArtwork(defaultArtWork);

        // adjust the view proportions differently;
        // when in tablet vs. mobile mode
        adjustViewWights();

        String videoUrl = mStep.getVideoUrl();
        if (TextUtils.isEmpty(videoUrl)) {
            // we  don't have a valid video URL;
            // show a place-holder image and hide the player.
            mPlayerView.setVisibility(View.GONE);
            mNoVideoImageView.setVisibility(View.VISIBLE);
        } else {
            // restore player state (if needed)
            if (savedInstanceState != null) {
                mPlayWhenReady = savedInstanceState.getBoolean(PLAYER_PLAY_WHEN_READY_KEY, true);
                mPlayerPosition = savedInstanceState.getLong(PLAYER_POSITION_KEY, 0L);
            }

            // initialize the player
            initializePlayer(Uri.parse(videoUrl));
        }

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        // take a snapshot of the player state
        // we might not get the chance to do that later since
        // we may be releasing the player now
        if (mPlayer != null) {
            mPlayWhenReady = mPlayer.getPlayWhenReady();
            mPlayerPosition = mPlayer.getCurrentPosition();
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(PLAYER_PLAY_WHEN_READY_KEY, mPlayWhenReady);
        outState.putLong(PLAYER_POSITION_KEY, mPlayerPosition);
    }

    // Helper method used to initialize the ExoPlayer and its view.
    private void initializePlayer(Uri stepVideoUri) {
        if (mPlayer == null) {
            Context context = getContext();
            if (context == null) return;

            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();

            // create the player instance and connect it to its view
            mPlayer = ExoPlayerFactory.newSimpleInstance(getContext(),
                    trackSelector, loadControl);
            mPlayerView.setPlayer(mPlayer);


            // prepare a media source and load it into the player
            String userAgent = Util.getUserAgent(context, "PlayerApp");
            MediaSource mediaSource = new ExtractorMediaSource(
                    stepVideoUri,
                    new DefaultDataSourceFactory(context, userAgent),
                    new DefaultExtractorsFactory(),
                    null, null);

            mPlayer.prepare(mediaSource);
            // listen to the player callbacks
            mPlayer.addListener(this);

            mPlayer.setPlayWhenReady(mPlayWhenReady);
            mPlayer.seekTo(mPlayerPosition);
        }
    }

    // Make sure we release the player resource.
    private void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    // We adjust the ratios between the video and text instructions based
    // on the layout type (tablet vs. mobile).
    private void adjustViewWights() {
        LinearLayout.LayoutParams params;
        if (mTabletModeOn) { // tablet mode
            params = (LinearLayout.LayoutParams) mPlayerView.getLayoutParams();
            params.weight = 2.5f;
            mPlayerView.setLayoutParams(params);

            params = (LinearLayout.LayoutParams) mNoVideoImageView.getLayoutParams();
            params.weight = 2.5f;
            mNoVideoImageView.setLayoutParams(params);

            params = (LinearLayout.LayoutParams) mDescriptionTextView.getLayoutParams();
            params.weight = 1.0f;
            mDescriptionTextView.setLayoutParams(params);

        } else { // mobile mode
            params = (LinearLayout.LayoutParams) mPlayerView.getLayoutParams();
            params.weight = 2.0f;
            mPlayerView.setLayoutParams(params);

            params = (LinearLayout.LayoutParams) mNoVideoImageView.getLayoutParams();
            params.weight = 2.0f;
            mNoVideoImageView.setLayoutParams(params);

            params = (LinearLayout.LayoutParams) mDescriptionTextView.getLayoutParams();
            params.weight = 2.5f;
            mDescriptionTextView.setLayoutParams(params);
        }
    }

    // ExoPlayer event listener. Mostly interested in the error callback (for debugging purposes).
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) { }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups,
                                TrackSelectionArray trackSelections) { }

    @Override
    public void onLoadingChanged(boolean isLoading) { }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) { }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.e(LOG_TAG, "Error loading media: " + error.getMessage());
    }

    @Override
    public void onPositionDiscontinuity() { }
}
