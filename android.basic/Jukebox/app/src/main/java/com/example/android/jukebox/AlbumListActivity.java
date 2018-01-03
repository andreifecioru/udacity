package com.example.android.jukebox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.example.android.jukebox.models.Album;
import com.example.android.jukebox.models.MusicLibrary;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AlbumListActivity extends AppCompatActivity {
    @BindView(R.id.album_grid_view) GridView mAlbumGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list);

        ButterKnife.bind(this);

        ArrayAdapter<Album> albumAdapter = new AlbumAdapter(this,
                MusicLibrary.getInstance().getAlbums());

        mAlbumGridView.setAdapter(albumAdapter);
    }
}
