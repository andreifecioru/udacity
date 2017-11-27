package com.android.example.geoquiz.quiz;

import android.support.v7.view.ContextThemeWrapper;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.android.example.geoquiz.R;

import butterknife.BindString;
import butterknife.ButterKnife;


/**
 * Custom {@link ScreenCard} implementation which displays the final results
 * of the quiz to the user.
 */
final class QuizResults extends ScreenCard {
    @BindString(R.string.quiz_results) String mReportTitle;

    private Quiz mQuiz;

    /**
     * Creates a Question instance inside a parent container.
     *
     * @param screenCardContainer The parent container where the question will
     *                            be rendered.
     * @param quiz The quiz for which the results are displayed.
     */
    QuizResults(ViewGroup screenCardContainer, Quiz quiz) {
        super(screenCardContainer);

        ButterKnife.bind(this, screenCardContainer);

        mQuiz = quiz;
        mTitle = mReportTitle;
    }

    /**
     * Displays the quiz results inside the {@link ScreenCard} content area via a
     * {@link TextView} UI control.
     */
    @Override
    protected void displayContent(ViewGroup screenCardContainer) {
        // generate the TextView control and apply various styling options
        TextView textView = new TextView(new ContextThemeWrapper(screenCardContainer.getContext(),
                R.style.AnswerSectionEditText), null, 0);
        textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        textView.setText(screenCardContainer.getContext().getString(
                R.string.quiz_report, mQuiz.getCorrectAnswerCount(), mQuiz.getQuestionCount()));

        // add the TextView control as a child of the screen card container
        // (becomes the content area beneath the title strip)
        screenCardContainer.addView(textView);
    }
}
