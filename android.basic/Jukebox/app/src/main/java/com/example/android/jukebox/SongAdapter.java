package com.example.android.jukebox;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.jukebox.models.Album;
import com.example.android.jukebox.models.Song;

import java.util.List;

class SongAdapter extends ArrayAdapter<Song> {
    private static final String LOG_TAG = SongAdapter.class.getSimpleName();

    private static class ViewHolder {
        TextView songNumberTextView;
        TextView songTitleTextView;
        TextView songArtistTextView;
        TextView songDurationTextView;
    }

    SongAdapter(Context context, List<Song> songs) {
        super(context, 0, songs);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        final Context context = getContext();
        final Song song = getItem(position);

        if (song == null) return convertView;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.song_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.songNumberTextView = convertView.findViewById(R.id.song_number_text_view);
            viewHolder.songTitleTextView = convertView.findViewById(R.id.song_title_text_view);
            viewHolder.songArtistTextView = convertView.findViewById(R.id.song_artist_text_view);
            viewHolder.songDurationTextView = convertView.findViewById(R.id.song_duration_text_view);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.songNumberTextView.setText(song.getTrackNumber());
        viewHolder.songTitleTextView.setText(song.getTitle());
        viewHolder.songArtistTextView.setText(song.getArtist());
        viewHolder.songDurationTextView.setText(song.getDuration());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG, "Selected: " + song);

                Intent intent = new Intent(context, SongDetailsActivity.class);
                intent.putExtra("song", song);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
