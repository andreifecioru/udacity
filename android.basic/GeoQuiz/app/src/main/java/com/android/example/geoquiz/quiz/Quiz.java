package com.android.example.geoquiz.quiz;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.example.geoquiz.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Implements the quiz abstraction: a thin management layer over a set of
 * {@link Question} objects. Questions are added to the quiz, presented
 * one-by-one to the user, answers are checked for correctness and score
 * is being kept.
 */
public class Quiz {
    @BindView(R.id.screen_card_container) LinearLayout mScreenCardContainer;

    @BindString(R.string.answer_ok) String mAnswerOk;
    @BindString(R.string.answer_bad) String mAnswerBad;
    
    @BindDrawable(R.drawable.ic_thumb_up_white_24dp) Drawable mThumbsUpIcon;
    @BindDrawable(R.drawable.ic_thumb_down_white_24dp) Drawable mThumbsDownIcon;
    @BindDrawable(android.R.drawable.stat_notify_error) Drawable mErrorIcon;

    private List<Question> mQuestions = new ArrayList<>();
    private Question mCurrentQuestion;
    private int mQuestionIdx = 0;
    private int mQuestionCount = 0;
    private int mCorrectAnswerCount = 0;

    /**
     * Creates a {@link Quiz} instance by creating the corresponding
     * {@link Question} screen cards inside an activity.
     *
     * @param activity The activity where the quiz questions will be
     *                 displayed for the user.
     */
    public Quiz(AppCompatActivity activity) {
        ButterKnife.bind(this, activity);

        // Populate the quiz with our questions
        mQuestions.add(ScreenCards.createQuestionOne(mScreenCardContainer));
        mQuestions.add(ScreenCards.createQuestionTwo(mScreenCardContainer));
        mQuestions.add(ScreenCards.createQuestionThree(mScreenCardContainer));
        mQuestions.add(ScreenCards.createQuestionFour(mScreenCardContainer));
        mQuestions.add(ScreenCards.createQuestionFive(mScreenCardContainer));
        mQuestions.add(ScreenCards.createQuestionSix(mScreenCardContainer));

        // shuffle the questions, to get some variety
        Collections.shuffle(mQuestions, new Random(System.nanoTime()));

        mQuestionCount = mQuestions.size();
    }

    /**
     * Computes the number of questions that make up the quiz.
     *
     * @return The number of questions in the quiz.
     */
    int getQuestionCount() {
        return mQuestionCount;
    }

    /**
     * Computes the number of correct answers provided by the user.
     *
     * @return The number of correct answers.
     */
    int getCorrectAnswerCount() {
        return mCorrectAnswerCount;
    }

    /**
     * Keeps track of the question currently displayed on the screen
     * and presents the next one. Once the last question is reached,
     * the quiz is marked as "finished".
     *
     * @return <code>true</code> if there are more questions to be
     *         displayed; <code>false</code> if we reached the end
     *         of the quiz.
     */
    public boolean displayNextQuestion() {
        try {
            // if this is the first question, we don't check
            // if the answer for the previous question is correct
            // (i.e. there is no previous question)
            if (mCurrentQuestion != null) {
                if (mCurrentQuestion.checkAnswer()) {
                    showToast(mAnswerOk, mThumbsUpIcon);
                    // update the "correct answers" counter
                    mCorrectAnswerCount++;
                } else {
                    showToast(mAnswerBad, mThumbsDownIcon);
                }
            }

            // if we have not yet reached the last question in the quiz,
            // display the next question.
            if (mQuestionIdx < mQuestionCount) {
                mCurrentQuestion = mQuestions.get(mQuestionIdx);
                mCurrentQuestion.display();
                mQuestionIdx ++;
                return true;
            }

            // we reached the end of the quiz (no more questions to be displayed)
            return false;
        } catch (Question.InvalidAnswerException e) {
            // the user provided an invalid answer:
            // we do not advance to the next question
            // and display an "error toast" with the appropriate error message.
            showToast(e.getMessage(), mErrorIcon);
            return true;
        }
    }

    /**
     * Displays a {@link Toast} message with a custom layout (an icon and a text beside it).
     *
     * @param message The text that appears in the {@link Toast} message.
     * @param iconDrawable The {@link Drawable} resource that is used as the icon in the
     *                     custom layout of the {@link Toast} message.
     */
    private void showToast(String message, Drawable iconDrawable) {
        Context context = mScreenCardContainer.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.message_toast, null);

        // set the icon drawable
        ImageView icon = layout.findViewById(R.id.toast_image);
        icon.setImageDrawable(iconDrawable);

        // the the toast text
        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(message);

        // setup the toast and show it on the screen
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 250);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
