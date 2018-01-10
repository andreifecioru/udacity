package com.example.android.miwok;


import android.content.Context;
import android.widget.ArrayAdapter;


public class FamilyFragment extends BaseSoundPlayingFragment {

    @Override
    protected ArrayAdapter<Word> getWordsAdapter(Context context) {
        return new WordAdapter(context,
                Words.createFamilyMembers(context),
                R.color.category_family);
    }
}
