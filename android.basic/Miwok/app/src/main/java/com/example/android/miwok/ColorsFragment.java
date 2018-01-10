package com.example.android.miwok;


import android.content.Context;
import android.widget.ArrayAdapter;


public class ColorsFragment extends BaseSoundPlayingFragment {

    @Override
    protected ArrayAdapter<Word> getWordsAdapter(Context context) {
        return new WordAdapter(context,
                Words.createColors(context),
                R.color.category_colors);
    }
}
