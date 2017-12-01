package com.example.android.miwok;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

class Words {
    static List<Word> createNumbers(Context context) {
        Resources res = context.getResources();

        String[] miWords = res.getStringArray(R.array.numbers_miwok);
        String[] enWords = res.getStringArray(R.array.numbers_english);

        int wordCount = Math.max(miWords.length, enWords.length);
        List<Word> words = new ArrayList<>();

        for (int i = 0; i < wordCount; i++) {
            words.add(new Word(miWords[i], enWords[i]));
        }

        return words;
    }

    static List<Word> createColors(Context context) {
        Resources res = context.getResources();

        String[] miWords = res.getStringArray(R.array.colors_miwok);
        String[] enWords = res.getStringArray(R.array.colors_english);

        int wordCount = Math.max(miWords.length, enWords.length);
        List<Word> words = new ArrayList<>();

        for (int i = 0; i < wordCount; i++) {
            words.add(new Word(miWords[i], enWords[i]));
        }

        return words;
    }

    static List<Word> createFamilyMembers(Context context) {
        Resources res = context.getResources();

        String[] miWords = res.getStringArray(R.array.family_miwok);
        String[] enWords = res.getStringArray(R.array.family_english);

        int wordCount = Math.max(miWords.length, enWords.length);
        List<Word> words = new ArrayList<>();

        for (int i = 0; i < wordCount; i++) {
            words.add(new Word(miWords[i], enWords[i]));
        }

        return words;
    }

    static List<Word> createPhrases(Context context) {
        Resources res = context.getResources();

        String[] miWords = res.getStringArray(R.array.phrases_miwok);
        String[] enWords = res.getStringArray(R.array.phrases_english);

        int wordCount = Math.max(miWords.length, enWords.length);
        List<Word> words = new ArrayList<>();

        for (int i = 0; i < wordCount; i++) {
            words.add(new Word(miWords[i], enWords[i]));
        }

        return words;
    }

}
