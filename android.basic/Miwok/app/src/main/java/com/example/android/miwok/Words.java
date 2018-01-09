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

        int[] audio = {
            R.raw.number_one,
            R.raw.number_two,
            R.raw.number_three,
            R.raw.number_four,
            R.raw.number_five,
            R.raw.number_six,
            R.raw.number_seven,
            R.raw.number_eight,
            R.raw.number_nine,
            R.raw.number_ten
        };

        int wordCount = Math.min(Math.min(Math.min(miWords.length, enWords.length), icons.length), audio.length);
        List<Word> words = new ArrayList<>();

        for (int i = 0; i < wordCount; i++) {
            words.add(Word.createWord(miWords[i], enWords[i], icons[i], audio[i]));
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

        int[] audio = {
            R.raw.color_red,
            R.raw.color_green,
            R.raw.color_brown,
            R.raw.color_gray,
            R.raw.color_black,
            R.raw.color_white,
            R.raw.color_dusty_yellow,
            R.raw.color_mustard_yellow
        };

        int wordCount = Math.min(Math.min(Math.min(miWords.length, enWords.length), icons.length), audio.length);
        List<Word> words = new ArrayList<>();

        for (int i = 0; i < wordCount; i++) {
            words.add(Word.createWord(miWords[i], enWords[i], icons[i], audio[i]));
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

        int[] audio = {
            R.raw.family_father,
            R.raw.family_mother,
            R.raw.family_son,
            R.raw.family_daughter,
            R.raw.family_older_brother,
            R.raw.family_younger_brother,
            R.raw.family_older_sister,
            R.raw.family_younger_sister,
            R.raw.family_grandmother,
            R.raw.family_grandfather
        };

        int wordCount = Math.min(Math.min(Math.min(miWords.length, enWords.length), icons.length), audio.length);
        List<Word> words = new ArrayList<>();

        for (int i = 0; i < wordCount; i++) {
            words.add(Word.createWord(miWords[i], enWords[i], icons[i], audio[i]));
        }

        return words;
    }

    static List<Word> createPhrases(Context context) {
        Resources res = context.getResources();

        String[] miWords = res.getStringArray(R.array.phrases_miwok);
        String[] enWords = res.getStringArray(R.array.phrases_english);

        int[] audio = {
                R.raw.phrase_where_are_you_going,
                R.raw.phrase_what_is_your_name,
                R.raw.phrase_my_name_is,
                R.raw.phrase_how_are_you_feeling,
                R.raw.phrase_im_feeling_good,
                R.raw.phrase_are_you_coming,
                R.raw.phrase_yes_im_coming,
                R.raw.phrase_im_coming,
                R.raw.phrase_lets_go,
                R.raw.phrase_come_here
        };

        int wordCount = Math.min(Math.min(miWords.length, enWords.length), audio.length);
        List<Word> words = new ArrayList<>();

        for (int i = 0; i < wordCount; i++) {
            words.add(Word.createWordWithoutImage(miWords[i], enWords[i], audio[i]));
        }

        return words;
    }

}
