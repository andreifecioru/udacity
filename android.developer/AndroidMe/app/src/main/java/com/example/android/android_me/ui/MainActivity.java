/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.android.android_me.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.android_me.R;
import static com.example.android.android_me.ui.AndroidMeActivity.HEAD_IDX_KEYS;
import static com.example.android.android_me.ui.AndroidMeActivity.BODY_IDX_KEY;
import static com.example.android.android_me.ui.AndroidMeActivity.LEGS_IDX_KEY;

// This activity is responsible for displaying the master list of all images
// Implement the MasterListFragment callback, OnImageClickListener
public class MainActivity extends AppCompatActivity implements MasterListFragment.OnImageClickListener{

    private static final int MAX_BODY_PART_COUNT = 12;

    private Button mNextButton;
    private int mHeadIdx = 0;
    private int mBodyIdx = 0;
    private int mLegsIdx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNextButton = findViewById(R.id.button_next);
    }

    // Define the behavior for onImageSelected
    public void onImageSelected(int position) {
        // Create a Toast that displays the position that was clicked
        Toast.makeText(this, "Position clicked = " + position, Toast.LENGTH_SHORT).show();

        int bodyPartNumber = position / MAX_BODY_PART_COUNT;
        int bodyPartIdx = position - bodyPartNumber * MAX_BODY_PART_COUNT;

        switch (bodyPartNumber) {
            case 0: mHeadIdx = bodyPartIdx; break;
            case 1: mBodyIdx = bodyPartIdx; break;
            case 2: mLegsIdx = bodyPartIdx; break;
        }

        Bundle extra = new Bundle();
        extra.putInt(HEAD_IDX_KEYS, mHeadIdx);
        extra.putInt(BODY_IDX_KEY, mBodyIdx);
        extra.putInt(LEGS_IDX_KEY, mLegsIdx);

        final Intent intent = new Intent(MainActivity.this, AndroidMeActivity.class);
        intent.putExtras(extra);

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }

}
