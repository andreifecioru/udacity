package com.example.andrei.jokefactory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.jokesmith.JokeSmith;
import com.example.jokewizard.JokeWizard;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_joke_smith_text) TextView mJokeSmithTextView;
    @BindView(R.id.tv_joke_wizard_text) TextView mJokeWizardTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mJokeSmithTextView.setText(JokeSmith.getJoke());
        mJokeWizardTextView.setText(JokeWizard.getJoke());
    }
}
