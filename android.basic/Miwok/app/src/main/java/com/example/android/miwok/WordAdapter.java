package com.example.android.miwok;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class WordAdapter extends ArrayAdapter<Word> {
    private static final String LOG_TAG = WordAdapter.class.getSimpleName();

    private final int mColorResId;

    private static class ViewHolder {
        ImageView iconImageView;
        TextView miWordTextView;
        TextView enWordTextView;
        ImageView mediaIconImageView;
    }

    WordAdapter(Context context, List<Word> words, int colorResId) {
        super(context, 0, words);
        mColorResId = colorResId;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        final Word word = getItem(position);

        if (word == null) return convertView;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.word_layout, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.iconImageView = (ImageView) convertView.findViewById(R.id.icon_img_view);
            viewHolder.miWordTextView = (TextView) convertView.findViewById(R.id.mi_word_text_view);
            viewHolder.enWordTextView = (TextView) convertView.findViewById(R.id.en_word_text_view);
            viewHolder.mediaIconImageView = (ImageView) convertView.findViewById(R.id.icon_media_img_view);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        convertView.setBackgroundResource(mColorResId);

        if (word.hasImage()) {
            viewHolder.iconImageView.setImageResource(word.getIconDrawableResId());
        } else {
            viewHolder.iconImageView.setVisibility(View.GONE);
        }

        viewHolder.miWordTextView.setText(word.getMiwokTranslation());
        viewHolder.enWordTextView.setText(word.getDefaultTranslation());

        return convertView;
    }
}
