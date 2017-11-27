package com.android.example.geoquiz.quiz;

import android.view.ViewGroup;

import com.android.example.geoquiz.R;

import butterknife.BindString;
import butterknife.ButterKnife;


/**
 * Concrete final implementation of the {@link Question} abstract class representing
 * one of the questions in the quiz.
 *
 * It is a {@link OpenMultipleChoiceQuestion} where the user must select at least
 * one (but possibly more than one) option out of 3 possible answers.
 *
 * The correct answer is the 1st and 3rd options (index 0 and 2). Expected answer is: "|0|2|".
 */
final class QuestionFour extends OpenMultipleChoiceQuestion {
    @BindString(R.string.question_4) String mQuestionText;
    @BindString(R.string.europe) String mEuropeText;
    @BindString(R.string.africa) String mAfricaText;
    @BindString(R.string.asia) String mAsiaText;

    QuestionFour(ViewGroup screenCardContainer) {
        super(screenCardContainer);

        addChoice(mEuropeText);
        addChoice(mAfricaText);
        addChoice(mAsiaText);

        ButterKnife.bind(this, screenCardContainer);

        mTitle = mQuestionText;
        mCorrectAnswer = "|0|2|";
    }
}
