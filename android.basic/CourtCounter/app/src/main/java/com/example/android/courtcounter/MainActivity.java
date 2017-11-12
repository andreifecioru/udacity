package com.example.android.courtcounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private int teamAScore = 0;
    private int teamBScore = 0;

    private TextView teamAScoreTextView;
    private TextView teamBScoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateTeamAScoreTextView();
        updateTeamBScoreTextView();
    }

    private void increaseTeamAScore(int amount) {
        teamAScore += amount;
        updateTeamAScoreTextView();
    }

    private void increaseTeamBScore(int amount) {
        teamBScore += amount;
        updateTeamBScoreTextView();
    }

    private void updateTeamAScoreTextView() {
        teamAScoreTextView = findViewById(R.id.score_a_text_view);
        if (teamAScoreTextView != null) {
            teamAScoreTextView.setText(String.format(Locale.ENGLISH, "%d", teamAScore));
        }
    }

    private void updateTeamBScoreTextView() {
        teamBScoreTextView = findViewById(R.id.score_b_text_view);
        if (teamBScoreTextView != null) {
            teamBScoreTextView.setText(String.format(Locale.ENGLISH, "%d", teamBScore));
        }
    }

    public void onTeamAPlus3ButtonClick(View view) {
       increaseTeamAScore(3);
    }

    public void onTeamAPlus2ButtonClick(View view) {
        increaseTeamAScore(2);
    }

    public void onTeamAFreeThrowButtonClick(View view) {
        increaseTeamAScore(1);
    }

    public void onTeamBPlus3ButtonClick(View view) {
        increaseTeamBScore(3);
    }

    public void onTeamBPlus2ButtonClick(View view) {
        increaseTeamBScore(2);
    }

    public void onTeamBFreeThrowButtonClick(View view) {
        increaseTeamBScore(1);
    }

    public void onResetButtonClick(View view) {
        teamAScore = 0;
        updateTeamAScoreTextView();
        teamBScore = 0;
        updateTeamBScoreTextView();
    }
}
