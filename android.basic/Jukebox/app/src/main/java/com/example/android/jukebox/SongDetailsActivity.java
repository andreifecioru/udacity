package com.example.android.jukebox;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.jukebox.models.Song;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SongDetailsActivity extends AppCompatActivity {
    @BindView(R.id.album_art_img_view) ImageView mAlbumArtImageView;
    @BindView(R.id.song_title_text_view) TextView mSongTitleTextView;
    @BindView(R.id.song_album_title_text_view) TextView mSongAlbumTitleTextView;
    @BindView(R.id.song_artist_text_view) TextView mSongArtistTextView;
    @BindView(R.id.play_pause_img_view) ImageView mPlayPauseImageView;
    @BindView(R.id.navigation_back_image_view) ImageView mBackImageView;

    private boolean mIsPlaybackInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_details);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        Song song = (Song) intent.getSerializableExtra("song");

        mAlbumArtImageView.setImageResource(song.getAlbumArtResId());
        mSongTitleTextView.setText(song.getTitle());
        mSongAlbumTitleTextView.setText(song.getAlbumTitle());
        mSongArtistTextView.setText(song.getArtist());

        mPlayPauseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePlayback();

            }
        });

        final Context context = this;
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AlbumListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });
    }

    private void togglePlayback() {
        mIsPlaybackInProgress = (!mIsPlaybackInProgress);
        displayPlaybackButton();
    }

    private void displayPlaybackButton() {
        if (mIsPlaybackInProgress) {
            mPlayPauseImageView.setImageResource(android.R.drawable.ic_media_pause);

            Toast toast  = Toast.makeText(this, "Playback started.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 50);
            toast.show();
        } else {
            mPlayPauseImageView.setImageResource(android.R.drawable.ic_media_play);

            Toast toast  = Toast.makeText(this, "Playback stopped.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 50);
            toast.show();
        }
    }
}
