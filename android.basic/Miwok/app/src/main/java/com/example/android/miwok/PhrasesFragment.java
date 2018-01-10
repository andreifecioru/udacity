package com.example.android.miwok;


import android.content.Context;
import android.widget.ArrayAdapter;


public class PhrasesFragment extends BaseSoundPlayingFragment {

    @Override
    protected ArrayAdapter<Word> getWordsAdapter(Context context) {
        return new WordAdapter(context,
                Words.createPhrases(context),
                R.color.category_phrases);
    }
}
