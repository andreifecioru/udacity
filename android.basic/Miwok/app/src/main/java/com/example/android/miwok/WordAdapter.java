package com.example.android.miwok;

import android.content.Context;
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

    private static class ViewHolder {
        ImageView iconImageView;
        TextView miWordTextView;
        TextView enWordTextView;
    }

    WordAdapter(Context context, List<Word> words) {
        super(context, 0, words);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        final Word word = getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.word_layout, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.iconImageView = (ImageView) convertView.findViewById(R.id.icon_img_view);
            viewHolder.miWordTextView = (TextView) convertView.findViewById(R.id.mi_word_text_view);
            viewHolder.enWordTextView = (TextView) convertView.findViewById(R.id.en_word_text_view);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.miWordTextView.setText(word.getMiwokTranslation());
        viewHolder.enWordTextView.setText(word.getDefaultTranslation());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG, "Click on position: " + position);
                Log.i(LOG_TAG, "Click on word: " +  word);
            }
        });

        return convertView;
    }
}
