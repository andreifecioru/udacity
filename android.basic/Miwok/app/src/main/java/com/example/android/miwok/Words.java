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
        int[] icons = {
            R.drawable.number_one,
            R.drawable.number_two,
            R.drawable.number_three,
            R.drawable.number_four,
            R.drawable.number_five,
            R.drawable.number_six,
            R.drawable.number_seven,
            R.drawable.number_eight,
            R.drawable.number_nine,
            R.drawable.number_ten
        };

        int wordCount = Math.min(Math.min(miWords.length, enWords.length), icons.length);
        List<Word> words = new ArrayList<>();

        for (int i = 0; i < wordCount; i++) {
            words.add(new Word(miWords[i], enWords[i], icons[i]));
        }

        return words;
    }

    static List<Word> createColors(Context context) {
        Resources res = context.getResources();

        String[] miWords = res.getStringArray(R.array.colors_miwok);
        String[] enWords = res.getStringArray(R.array.colors_english);
        int[] icons = {
            R.drawable.color_red,
            R.drawable.color_green,
            R.drawable.color_brown,
            R.drawable.color_gray,
            R.drawable.color_black,
            R.drawable.color_white,
            R.drawable.color_dusty_yellow,
            R.drawable.color_mustard_yellow
        };

        int wordCount = Math.min(Math.min(miWords.length, enWords.length), icons.length);
        List<Word> words = new ArrayList<>();

        for (int i = 0; i < wordCount; i++) {
            words.add(new Word(miWords[i], enWords[i], icons[i]));
        }

        return words;
    }

    static List<Word> createFamilyMembers(Context context) {
        Resources res = context.getResources();

        String[] miWords = res.getStringArray(R.array.family_miwok);
        String[] enWords = res.getStringArray(R.array.family_english);
        int[] icons = {
                R.drawable.family_father,
                R.drawable.family_mother,
                R.drawable.family_son,
                R.drawable.family_daughter,
                R.drawable.family_older_brother,
                R.drawable.family_younger_brother,
                R.drawable.family_older_sister,
                R.drawable.family_younger_sister,
                R.drawable.family_grandmother,
                R.drawable.family_grandfather
        };

        int wordCount = Math.min(Math.min(miWords.length, enWords.length), icons.length);
        List<Word> words = new ArrayList<>();

        for (int i = 0; i < wordCount; i++) {
            words.add(new Word(miWords[i], enWords[i], icons[i]));
        }

        return words;
    }

    static List<Word> createPhrases(Context context) {
        Resources res = context.getResources();

        String[] miWords = res.getStringArray(R.array.phrases_miwok);
        String[] enWords = res.getStringArray(R.array.phrases_english);

        int wordCount = Math.min(miWords.length, enWords.length);
        List<Word> words = new ArrayList<>();

        for (int i = 0; i < wordCount; i++) {
            words.add(new Word(miWords[i], enWords[i], "none"));
        }

        return words;
    }

}
