package com.example.android.jukebox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.jukebox.models.Album;

import java.util.List;

class AlbumAdapter extends ArrayAdapter<Album> {
    private static final String LOG_TAG = AlbumAdapter.class.getSimpleName();

    private static class ViewHolder {
        ImageView albumArtImageView;
        TextView titleTextView;
        TextView artistTextView;
    }

    AlbumAdapter(Context context, List<Album> albums) {
        super(context, 0, albums);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        final Context context = getContext();
        final Album album = getItem(position);

        if (album == null) return convertView;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.album_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.albumArtImageView = convertView.findViewById(R.id.album_art_img_view);
            viewHolder.titleTextView = convertView.findViewById(R.id.album_title_text_view);
            viewHolder.artistTextView = convertView.findViewById(R.id.album_artist_text_view);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.albumArtImageView.setImageResource(album.getAlbumArtResId());
        viewHolder.titleTextView.setText(album.getTitle());
        viewHolder.artistTextView.setText(album.getArtist());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG, "Selected: " + album);
                Intent intent = new Intent(context, AlbumDetailsActivity.class);
                intent.putExtra("album", album);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
