package com.example.android.funtravel.ui;

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

import com.example.android.funtravel.common.model.Offer;
import com.example.android.funtravel.utils.OfferUtils;
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

import com.example.android.funtravel.R;


public class OfferOverviewFragment
    extends Fragment
    implements ExoPlayer.EventListener {

    private static final String LOG_TAG = OfferOverviewFragment.class.getSimpleName();

    public static final String OFFER_ARGS_KEY = "offer.args.key";

    private static final String PLAYER_POSITION_KEY = "player.position.key";
    private static final String PLAYER_PLAY_WHEN_READY_KEY = "player.play.when.ready.key";

    @BindView(R.id.tv_step_description) TextView mDescriptionTextView;
    @BindView(R.id.exo_player_view) SimpleExoPlayerView mPlayerView;
    @BindView(R.id.iv_no_video) ImageView mNoVideoImageView;
    @BindView(R.id.tv_offer_title) TextView mOfferTitleTextView;
    @BindView(R.id.iv_offer_type) ImageView mOfferTypeImageView;
    @BindView(R.id.tv_offer_price) TextView mOfferPriceTextView;
    @BindView(R.id.ll_rating_container) LinearLayout mRatingContainerLinearLayout;


    private Offer mOffer;
    private SimpleExoPlayer mPlayer;
    private boolean mPlayWhenReady = false;
    private long mPlayerPosition = 0L;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            mOffer = args.getParcelable(OFFER_ARGS_KEY);
            if (mOffer != null) {
                Log.d(LOG_TAG, mOffer.toString());
            }
        }

        if (mOffer == null) {
            throw new IllegalStateException("Offer cannot be null");
        }

        View rootView = inflater.inflate(R.layout.fragment_offer_overview, container, false);

        ButterKnife.bind(this, rootView);

        mDescriptionTextView.setText(mOffer.getDescription());

        Bitmap defaultArtWork = BitmapFactory.decodeResource(getResources(), R.drawable.funtravel);
        mPlayerView.setDefaultArtwork(defaultArtWork);

        String videoUrl = mOffer.getVideoUrl();
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

        // Display the offer-type logo image
        mOfferTypeImageView.setImageResource(OfferUtils.getOfferTypeImageRes(mOffer));

        // Display the offer title
        mOfferTitleTextView.setText(mOffer.getTitle());

        // Display the avg. rating
        OfferUtils.showRatingStars(mRatingContainerLinearLayout, mOffer.getAvgRating());

        // Display the offer price
        mOfferPriceTextView.setText(getString(R.string.offer_price, String.valueOf(mOffer.getPrice())));

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

    // ExoPlayer event listener. Mostly interested in the error callback (for debugging purposes).
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) { }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) { }

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
