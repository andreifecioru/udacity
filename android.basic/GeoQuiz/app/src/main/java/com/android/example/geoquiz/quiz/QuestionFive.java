package com.android.example.geoquiz.quiz;

import android.view.ViewGroup;

import com.android.example.geoquiz.R;

import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Concrete final implementation of the {@link Question} abstract class representing
 * one of the questions in the quiz.
 *
 * It is a {@link ClosedMultipleChoiceQuestion} where the user must to select exactly
 * one option out of 3 possible answers.
 *
 * The correct answer is the 1st option (index 0). Expected answer is: "0".
 */
final class QuestionFive extends ClosedMultipleChoiceQuestion {
    @BindString(R.string.question_5) String mQuestionText;
    @BindString(R.string.east_coast) String mEastCoast;
    @BindString(R.string.west_coast) String mWestCoast;
    @BindString(R.string.none_of_above) String mNoneOfTheAbove;

    QuestionFive(ViewGroup questionContainer) {
        super(questionContainer);

        addChoice(mEastCoast);
        addChoice(mWestCoast);
        addChoice(mNoneOfTheAbove);

        ButterKnife.bind(this, questionContainer);

        mTitle = mQuestionText;
        mCorrectAnswer = "0";
    }
}
