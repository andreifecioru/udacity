package com.android.example.geoquiz.quiz;

import android.view.ViewGroup;

/**
 * Utility class for the {@link ScreenCard} class.
 *
 * Provides static factory methods for the various concrete
 * implementations of screen cards.
 */
final public class ScreenCards {
    static Question createQuestionOne(ViewGroup screenCardContainer) {
        return new QuestionOne(screenCardContainer);
    }

    static Question createQuestionTwo(ViewGroup screenCardContainer) {
        return new QuestionTwo(screenCardContainer);
    }

    static Question createQuestionThree(ViewGroup screenCardContainer) {
        return new QuestionThree(screenCardContainer);
    }

    static Question createQuestionFour(ViewGroup screenCardContainer) {
        return new QuestionFour(screenCardContainer);
    }

    static Question createQuestionFive(ViewGroup screenCardContainer) {
        return new QuestionFive(screenCardContainer);
    }

    static Question createQuestionSix(ViewGroup screenCardContainer) {
        return new QuestionSix(screenCardContainer);
    }
}
