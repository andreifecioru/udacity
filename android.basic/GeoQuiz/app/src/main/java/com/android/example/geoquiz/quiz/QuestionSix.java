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
 * The correct answer is the 3rd option (index 2). Expected answer is: "2".
 */
final class QuestionSix extends ClosedMultipleChoiceQuestion {
    @BindString(R.string.question_6) String mQuestionText;
    @BindString(R.string.australia) String mAustraliaText;
    @BindString(R.string.africa) String mAfricaText;
    @BindString(R.string.antartica) String mAntarticaText;


    QuestionSix(ViewGroup questionContainer) {
        super(questionContainer);

        addChoice(mAustraliaText);
        addChoice(mAfricaText);
        addChoice(mAntarticaText);

        ButterKnife.bind(this, questionContainer);

        mTitle = mQuestionText;
        mCorrectAnswer = "2";
    }
}
