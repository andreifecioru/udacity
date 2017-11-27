package com.android.example.geoquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.example.geoquiz.quiz.Quiz;
import com.android.example.geoquiz.quiz.ScreenCards;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {
    @BindString(R.string.start_again) String mStartAgainText;
    @BindString(R.string.next_question) String mNextQuestionText;

    @BindView(R.id.button_action) Button mActionButton;
    @BindView(R.id.screen_card_container) LinearLayout mQuestionContainer;
    @BindView(R.id.screen_card_title) TextView mTextViewQuestionText;

    private Quiz mQuiz;
    private boolean mIsQuizFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        startQuiz();
    }

    /**
     * Starts a new quiz.
     *
     * A new {@link Quiz} object is created. The 1st question is displayed and the
     * caption for the action button is updated appropriately.
     */
    private void startQuiz() {
        mQuiz = new Quiz(this);
        mQuiz.displayNextQuestion();
        mIsQuizFinished = false;
        mActionButton.setText(mNextQuestionText);
    }

    /**
     * Creates and displays the screen card with the final quiz results.
     */
    private void showQuizResults() {
        ScreenCards.createQuizReport(mQuestionContainer, mQuiz).display();
    }

    /**
     * Event handler method for the "action button".
     *
     * The action button behaves differently depending on the quiz state:
     *   - if the quiz is "finished", the button triggers the start of a new quiz
     *   - if the quiz is still "in progress", the button advances the quiz to
     *     the next question.
     */
    @OnClick(R.id.button_action)
    public void onButtonActionClick(View view) {
        if (mIsQuizFinished) {
            startQuiz();
        } else {
            mIsQuizFinished = !mQuiz.displayNextQuestion();

            if (mIsQuizFinished) {
                showQuizResults();
                mActionButton.setText(mStartAgainText);
            }
        }
    }
}
