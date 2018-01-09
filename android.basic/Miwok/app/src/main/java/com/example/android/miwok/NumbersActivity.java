package com.example.android.miwok;

import android.os.Bundle;


public class NumbersActivity extends BaseSoundPlayingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mWordsAdapter = new WordAdapter(this,
                Words.createNumbers(this),
                R.color.category_numbers);

        super.onCreate(savedInstanceState);
    }
}
