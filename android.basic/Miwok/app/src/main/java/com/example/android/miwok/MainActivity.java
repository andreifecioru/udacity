/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.miwok;

import android.app.ActivityManager;
import static android.app.ActivityManager.MemoryInfo;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.Arrays;

import butterknife.BindArray;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {
    @BindArray(R.array.numbers_english) String[] mNumberEnglish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.family)
    public void onFamilyViewClick() {
        Intent intent = new Intent(this, FamilyActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.phrases)
    public void onPhrasesViewClick() {
        Intent intent = new Intent(this, PhrasesActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.numbers)
    public void onNumbersViewClick() {
        Intent intent = new Intent(this, NumbersActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.colors)
    public void onColorsViewClick() {
        Intent intent = new Intent(this, ColorsActivity.class);
        startActivity(intent);
    }
}
