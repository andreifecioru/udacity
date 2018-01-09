package com.example.android.miwok;

import android.os.Bundle;


public class PhrasesActivity extends BaseSoundPlayingActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mWordsAdapter = new WordAdapter(this,
                Words.createPhrases(this),
                R.color.category_phrases);

        super.onCreate(savedInstanceState);
    }
}
