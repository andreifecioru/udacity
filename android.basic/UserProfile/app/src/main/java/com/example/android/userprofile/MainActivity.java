package com.example.android.userprofile;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.profile_picture) ImageView profilePictureImageView;
    @BindView(R.id.name) TextView nameTextView;
    @BindView(R.id.birthday) TextView birthdayTextView;
    @BindView(R.id.country) TextView countryTextView;

    @BindDrawable(R.drawable.profile_picture) Drawable profilePictureDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        profilePictureImageView.setImageDrawable(profilePictureDrawable);

        nameTextView.setText("Andrei Fecioru");
        birthdayTextView.setText("24 May 1981");
        countryTextView.setText("Romania (RO)");
    }
}
