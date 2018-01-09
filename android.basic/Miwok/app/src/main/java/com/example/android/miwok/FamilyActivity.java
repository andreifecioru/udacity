package com.example.android.miwok;

import android.os.Bundle;


public class FamilyActivity extends BaseSoundPlayingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mWordsAdapter = new WordAdapter(this,
                Words.createFamilyMembers(this),
                R.color.category_family);

        super.onCreate(savedInstanceState);
    }
}
