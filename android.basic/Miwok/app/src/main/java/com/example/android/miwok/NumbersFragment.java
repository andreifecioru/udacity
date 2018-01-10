package com.example.android.miwok;


import android.content.Context;
import android.widget.ArrayAdapter;


public class NumbersFragment extends BaseSoundPlayingFragment {

    @Override
    protected ArrayAdapter<Word> getWordsAdapter(Context context) {
        return new WordAdapter(context,
                Words.createNumbers(context),
                R.color.category_numbers);
    }
}
