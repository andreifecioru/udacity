package com.android.example.geoquiz;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        mQuestionContainer.setVisibility(View.VISIBLE);
    }

    /**
     * Creates and displays the screen card with the final quiz results.
     */
    private void showQuizResults() {
        // hide the question screen cards
        mQuestionContainer.setVisibility(View.GONE);

        // hide the action button
        // will be shown again later after the toast with
        mActionButton.setVisibility(View.GONE);

        Context context = this.getApplicationContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.quiz_results_toast, null);

        String results = getString(R.string.quiz_report, mQuiz.getCorrectAnswerCount(), mQuiz.getQuestionCount());
        // Set the results text
        TextView text = layout.findViewById(R.id.quiz_results_text_view);
        text.setText(results);

        // setup the toast and show it on the screen
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mActionButton.setVisibility(View.VISIBLE);
            }
        }, 6000);
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
