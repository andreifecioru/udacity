package com.example.andrei.jokefactory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

import com.example.jokedisplay.JokeDisplayActivity;
import com.example.joketeller.JokeTeller;

import static com.example.jokedisplay.JokeDisplayActivity.JOKE_TEXT_KEY;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_tell_joke)
    public void tellJoke(View view) {
        Intent intent = new Intent(this, JokeDisplayActivity.class);
        intent.putExtra(JOKE_TEXT_KEY, JokeTeller.tellJoke());
        startActivity(intent);
    }

}
