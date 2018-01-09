package com.example.android.miwok;

import android.os.Bundle;

public class ColorsActivity extends BaseSoundPlayingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mWordsAdapter = new WordAdapter(this,
                Words.createColors(this),
                R.color.category_colors);

        super.onCreate(savedInstanceState);
    }
}
