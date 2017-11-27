package com.android.example.geoquiz.quiz;

import android.content.Context;
import android.support.v7.view.ContextThemeWrapper;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.android.example.geoquiz.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;


/**
 * Concrete implementation of the {@link Question} class representing
 * a question that can be answered by choosing one or several of multiple
 * available choices.
 *
 * The user provides the answer via a set od {@link CheckBox} UI controls.
 *
 * The captured answer is the string representation of whatever the
 * user selects in the set of {@link CheckBox} UI controls.
 */
class OpenMultipleChoiceQuestion extends Question {
    @BindString(R.string.provide_answer) String mProvideAnswer;
    @BindString(R.string.internal_error) String mInternalError;

    private List<String> mChoices = new ArrayList<>();

    private final static int CHECK_BUTTON_ID_OFFSET = 1000;
    private final static String DELIMITER = "|";

    /**
     * Creates a {@link OpenMultipleChoiceQuestion} instance inside a parent container.
     *
     * @param screenCardContainer The parent container where the question will
     *                            be rendered.
     */
    OpenMultipleChoiceQuestion(ViewGroup screenCardContainer) {
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
     * For this particular implementation, selects one or several of the multiple possible answers
     * presented via a set of {@link CheckBox} UI controls.
     *
     * @param screenCardContainer The parent container where the question will
     *                            be rendered.
     */
    @Override
    protected void displayContent(ViewGroup screenCardContainer) {
        Context context = screenCardContainer.getContext();

        // generate the LinearLayout container and apply various styling options
        LinearLayout layout = new LinearLayout(new ContextThemeWrapper(context, R.style.AnswerSectionCheckGroup), null);
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.VERTICAL);

        // inject a CheckBox for each one of the available answers.
        // IDs are generated for each CheckBox (starting from and arbitrary offset value to avoid collisions).
        int index = 0;
        for (String choice : mChoices) {
            // generate the CheckBox control and apply various styling options
            CheckBox checkBox = new CheckBox(new ContextThemeWrapper(context, R.style.AnswerSectionCheckButton), null, 0);
            checkBox.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            checkBox.setText(choice);
            checkBox.setId(CHECK_BUTTON_ID_OFFSET + index);

            index ++;

            // add the CheckBox to the parent RadioGroup
            layout.addView(checkBox);
        }

        // add the LinearLayout container as a child of the screen card container
        // (becomes the content area beneath the title strip)
        screenCardContainer.addView(layout);
    }

    /**
     * Captures the user-provided answer in a <code>String</code> value.
     *
     * The return value from this method is compared with what we know to be
     * the correct answer (see {@link Question#checkAnswer()}.
     *
     * The captured answer is the string representation of whatever the
     * user selects in the set of {@link CheckBox} UI control.
     *
     * @param screenCardContainer The parent container where the question will
     *                            be rendered.
     *
     * @return a <code>String</code> representation of the user-provided answer.
     */
    @Override
    protected String getAnswer(ViewGroup screenCardContainer) {
        LinearLayout layout = (LinearLayout) screenCardContainer.getChildAt(1);

        if (layout != null) {
            StringBuilder sb = new StringBuilder(DELIMITER);
            int checkBoxCount = layout.getChildCount();
            for (int idx = 0; idx < checkBoxCount; idx++) {
                CheckBox checkBox = (CheckBox) layout.getChildAt(idx);
                if (checkBox.isChecked()) {
                    sb.append(idx).append(DELIMITER);
                }
            }
            return sb.toString();
        } else {
            throw new InvalidAnswerException("Internal error. Something went terribly wrong...");
        }
    }
}
