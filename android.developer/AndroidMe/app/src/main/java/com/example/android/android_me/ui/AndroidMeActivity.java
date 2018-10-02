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

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.android_me.R;
import com.example.android.android_me.data.AndroidImageAssets;
import static com.example.android.android_me.ui.BodyPartFragment.IMG_RES_ID_KEY;

// This activity will display a custom Android image composed of three body parts: head, body, and legs
public class AndroidMeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_me);

        Bundle args = new Bundle();

        // Create a new head BodyPartFragment
        BodyPartFragment headFragment = new BodyPartFragment();
        args.putInt(IMG_RES_ID_KEY, AndroidImageAssets.getHeads().get(0));
        headFragment.setArguments(args);

        // Create a new body BodyPartFragment
        BodyPartFragment bodyFragment = new BodyPartFragment();
        args = new Bundle();
        args.putInt(IMG_RES_ID_KEY, AndroidImageAssets.getBodies().get(0));
        bodyFragment.setArguments(args);

        // Create a new body BodyPartFragment
        BodyPartFragment legsFragment = new BodyPartFragment();
        args = new Bundle();
        args.putInt(IMG_RES_ID_KEY, AndroidImageAssets.getLegs().get(0));
        legsFragment.setArguments(args);

        // Add the fragment to its container using a FragmentManager and a Transaction
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.head_container, headFragment)
                .add(R.id.body_container, bodyFragment)
                .add(R.id.legs_container, legsFragment)
                .commit();
    }
}
