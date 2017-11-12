package com.example.android.scorekeeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private int teamAScore = 0;
    private int teamBScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Show the initial score values on the screen (no hard-coding in XML).
        updateTeamAScoreTextView();
        updateTeamBScoreTextView();
    }

    /**
     * Helper methods
     */
    private void increaseTeamAScore(int amount) {
        teamAScore += amount;
        updateTeamAScoreTextView();
    }

    private void increaseTeamBScore(int amount) {
        teamBScore += amount;
        updateTeamBScoreTextView();
    }

    private void resetTeamAScore() {
        teamAScore = 0;
        updateTeamAScoreTextView();

    }

    private void resetTeamBScore() {
        teamBScore = 0;
        updateTeamBScoreTextView();
    }

    private void updateTeamAScoreTextView() {
        TextView teamAScoreTextView = findViewById(R.id.score_a_text_view);
        if (teamAScoreTextView != null) {
            teamAScoreTextView.setText(String.format(Locale.ENGLISH, "%d", teamAScore));
        }
    }

    private void updateTeamBScoreTextView() {
        TextView teamBScoreTextView = findViewById(R.id.score_b_text_view);
        if (teamBScoreTextView != null) {
            teamBScoreTextView.setText(String.format(Locale.ENGLISH, "%d", teamBScore));
        }
    }


    /**
     * Event handlers
     */
    public void onTeamATouchdownButtonClick(View view) {
        increaseTeamAScore(6);
    }

    public void onTeamAExtraPointButtonClick(View view) {
        increaseTeamAScore(1);
    }

    public void onTeamAFieldGoalButtonClick(View view) {
        increaseTeamAScore(3);
    }

    public void onTeamASafetyButtonClick(View view) {
        increaseTeamAScore(2);
    }

    public void onTeamBTouchdownButtonClick(View view) {
        increaseTeamBScore(6);
    }

    public void onTeamBExtraPointButtonClick(View view) {
        increaseTeamBScore(1);
    }

    public void onTeamBFieldGoalButtonClick(View view) {
        increaseTeamBScore(3);
    }

    public void onTeamBSafetyButtonClick(View view) {
        increaseTeamBScore(2);
    }

    public void onResetButtonClick(View view){
        resetTeamAScore();
        resetTeamBScore();
    }
}
