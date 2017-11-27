package com.android.example.geoquiz.quiz;

import android.view.ViewGroup;

import com.android.example.geoquiz.R;

import butterknife.BindString;
import butterknife.ButterKnife;


/**
 * Concrete final implementation of the {@link Question} abstract class representing
 * one of the questions in the quiz.
 *
 * It is a {@link FreeTextQuestion} where the user must type the answer in an
 * {@link android.widget.EditText} UI control.
 *
 * The correct answer is 5. Expected answer is: "5".
 */
final class QuestionThree extends FreeTextQuestion {
    @BindString(R.string.question_3) String mQuestionText;

    QuestionThree(ViewGroup questionContainer) {
        super(questionContainer);

        ButterKnife.bind(this, questionContainer);

        mTitle = mQuestionText;
        mCorrectAnswer = "5";
    }
}
