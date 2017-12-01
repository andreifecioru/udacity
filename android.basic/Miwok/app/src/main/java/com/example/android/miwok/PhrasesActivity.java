package com.example.android.miwok;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhrasesActivity extends AppCompatActivity {
    @BindView(R.id.list)
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_words);

        ButterKnife.bind(this);

        ArrayAdapter<Word> wordsAdapter = new WordAdapter(
                this,
                Words.createPhrases(this));

        mListView.setAdapter(wordsAdapter);
    }
}
