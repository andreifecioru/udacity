package com.android.example.geoquiz.quiz;

import android.support.v7.view.ContextThemeWrapper;

import android.content.Context;

import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.example.geoquiz.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;


/**
 * Concrete implementation of the {@link Question} class representing
 * a question that can be answered by choosing one of multiple available
 * choices.
 *
 * The user provides the answer via a {@link RadioGroup} UI control.
 *
 * The captured answer is the string representation of whatever the
 * user selects in the {@link RadioGroup} form.
 */
class ClosedMultipleChoiceQuestion extends Question {
    @BindString(R.string.provide_answer) String mProvideAnswer;
    @BindString(R.string.internal_error) String mInternalError;

    private List<String> mChoices = new ArrayList<>();

    private final static int RADIO_BUTTON_ID_OFFSET = 1000;

    /**
     * Creates a {@link ClosedMultipleChoiceQuestion} instance inside a parent container.
     *
     * @param screenCardContainer The parent container where the question will
     *                            be rendered.
     */
    ClosedMultipleChoiceQuestion(ViewGroup screenCardContainer) {
        super(screenCardContainer);
    }

    /**
     * Adds a new choice to the list of possible choices that are presented as possible
     * answers to the user.
     *
     * @param choiceText The text that appears as one of the possible answers to the question.
     */
    void addChoice(String choiceText) {
        mChoices.add(choiceText);
    }

    /**
     * Renders the content of the {@link ScreenCard} where the user is required to
     * provide the answer to the question.
     *
     * For this particular implementation, selects one of the multiple possible answers
     * presented via a {@link RadioGroup} UI control.
     *
     * @param screenCardContainer The parent container where the question will
     *                            be rendered.
     */
    @Override
    protected void displayContent(ViewGroup screenCardContainer) {
        Context context = screenCardContainer.getContext();

        // generate the RadioGroup control and apply various styling options
        RadioGroup radioGroup = new RadioGroup(new ContextThemeWrapper(context, R.style.AnswerSectionRadioGroup), null);
        radioGroup.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        // inject a RadioButton for each one of the available answers.
        // IDs are generated for each RadioButton (starting from and arbitrary offset value to avoid collisions).
        int index = 0;
        for (String choice : mChoices) {
            // generate the RadioButton control and apply various styling options
            RadioButton radioButton = new RadioButton(new ContextThemeWrapper(context, R.style.AnswerSectionRadioButton), null, 0);
            radioButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            radioButton.setText(choice);
            radioButton.setId(RADIO_BUTTON_ID_OFFSET + index);

            index ++;

            // add the RadioButton to the parent RadioGroup
            radioGroup.addView(radioButton);
        }

        // add the RadioGroup control as a child of the screen card container
        // (becomes the content area beneath the title strip)
        screenCardContainer.addView(radioGroup);
    }

    /**
     * Captures the user-provided answer in a <code>String</code> value.
     *
     * The return value from this method is compared with what we know to be
     * the correct answer (see {@link Question#checkAnswer()}.
     *
     * The captured answer is the string representation of whatever the
     * user selects from the set of available checkboxes. The result is the
     * set of indexes of checked boxes separated by the <code>|</code> character.
     *
     * @param screenCardContainer The parent container where the question will
     *                            be rendered.
     *
     * @return a <code>String</code> representation of the user-provided answer.
     */
    @Override
    protected String getAnswer(ViewGroup screenCardContainer) {
        RadioGroup rg = (RadioGroup) screenCardContainer.getChildAt(1);

        if (rg != null) {
            int selectedId = rg.getCheckedRadioButtonId();

            // the user must select something
            if (selectedId == -1) {
                throw new InvalidAnswerException(mProvideAnswer);
            }

            // we return the (0-based) index of the selected option as as String
            return "" + (selectedId - RADIO_BUTTON_ID_OFFSET);
        } else {
            // we should never get to this place
            throw new InvalidAnswerException(mInternalError);
        }
    }
}
