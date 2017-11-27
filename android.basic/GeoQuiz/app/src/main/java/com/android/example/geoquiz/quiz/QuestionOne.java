package com.android.example.geoquiz.quiz;

import android.view.ViewGroup;

import com.android.example.geoquiz.R;

import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Concrete final implementation of the {@link Question} abstract class representing
 * one of the questions in the quiz.
 *
 * It is a {@link ClosedMultipleChoiceQuestion} where the user must select exactly
 * one option out of 3 possible answers.
 *
 * The correct answer is the 2nd option (index 1). Expected answer is: "1".
 */
final class QuestionOne extends ClosedMultipleChoiceQuestion {
    @BindString(R.string.question_1) String mQuestionText;

    QuestionOne(ViewGroup questionContainer) {
        super(questionContainer);

        addChoice("10921");
        addChoice("40075");
        addChoice("50025");

        ButterKnife.bind(this, questionContainer);

        mTitle = mQuestionText;
        mCorrectAnswer = "1";
    }
}
