package com.android.example.geoquiz.quiz;

import android.content.Context;
import android.support.v7.view.ContextThemeWrapper;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;

import com.android.example.geoquiz.R;

import butterknife.BindString;


/**
 * Concrete implementation of the {@link Question} class representing
 * a question that can be answered in a free-text form.
 *
 * The user provides the answer via a {@link EditText} UI control.
 *
 * The captured answer is the string representation of whatever the
 * user types in the {@link EditText} form.
 */
class FreeTextQuestion extends Question {
    @BindString(R.string.your_answer_here) String mYourAnswerHere;
    @BindString(R.string.provide_answer) String mProvideAnswer;
    @BindString(R.string.internal_error) String mInternalError;

    /**
     * Creates a {@link FreeTextQuestion} instance inside a parent container.
     *
     * @param screenCardContainer The parent container where the question will
     *                            be rendered.
     */
    FreeTextQuestion(ViewGroup screenCardContainer) { super(screenCardContainer); }

    /**
     * Renders the content of the {@link ScreenCard} where the user is required to
     * provide the answer to the question.
     *
     * For this particular implementation, the user inputs the answer in a free-text from
     * via a {@link EditText} UI control.
     *
     * @param screenCardContainer The parent container where the question will
     *                            be rendered.
     */
    @Override
    protected void displayContent(ViewGroup screenCardContainer) {
        final Context context = screenCardContainer.getContext();

        // generate the EditText control and apply various styling options
        final EditText editText = new EditText(
                new ContextThemeWrapper(context, R.style.AnswerSectionEditText),
                null, 0);
        editText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        editText.setHint(mYourAnswerHere);
        editText.setFocusableInTouchMode(true);

        // add the EditText control as a child of the screen card container
        // (becomes the content area beneath the title strip)
        screenCardContainer.addView(editText);
    }

    /**
     * Captures the user-provided answer in a <code>String</code> value.
     *
     * The return value from this method is compared with what we know to be
     * the correct answer (see {@link Question#checkAnswer()}.
     *
     * The captured answer is the string representation of whatever the
     * user types in the {@link EditText} UI control.
     *
     * @param screenCardContainer The parent container where the question will
     *                            be rendered.
     *
     * @return a <code>String</code> representation of the user-provided answer.
     */
    @Override
    protected String getAnswer(ViewGroup screenCardContainer) {
        EditText editText = (EditText) screenCardContainer.getChildAt(1);

        if (editText != null) {
            String answer = editText.getText().toString().trim();

            // the user must type something
            if ("".equals(answer)) {
                throw new InvalidAnswerException(mProvideAnswer);
            }

            return answer;
        } else {
            // we should never get to this place
            throw new InvalidAnswerException(mInternalError);
        }
    }
}
