package com.example.android.jukebox;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.android.jukebox.models.Album;
import com.example.android.jukebox.models.Song;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumDetailsActivity extends AppCompatActivity {
    private static final String LOG_TAG = AlbumDetailsActivity.class.getSimpleName();

    @BindView(R.id.album_art_img_view) ImageView mAlbumArtImageView;
    @BindView(R.id.song_list_view) ListView mSongListView;
    @BindView(R.id.navigation_back_image_view) ImageView mBackImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        Album album = (Album) intent.getSerializableExtra("album");

        ArrayAdapter<Song> songAdapter = new SongAdapter(this, album.getSongs());

        mSongListView.setAdapter(songAdapter);

        mAlbumArtImageView.setImageResource(album.getAlbumArtResId());

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
}
