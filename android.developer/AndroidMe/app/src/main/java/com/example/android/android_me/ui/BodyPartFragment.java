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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.android_me.R;


public class BodyPartFragment extends Fragment {
    private static final String LOG_TAG = BodyPartFragment.class.getSimpleName();

    public static final String IMG_RES_ID_KEY = "img.res.id";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public BodyPartFragment() { }

    /**
     * Inflates the fragment layout file and sets the correct resource for the image to display
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Get the image resource id
        Bundle args = getArguments();

        if (args == null) {
            String errMsg = "Fragment params must be set for BodyPartFragment class.";
            Log.e(LOG_TAG, errMsg);
            throw new IllegalStateException(errMsg);
        }

        int imgResId = args.getInt(IMG_RES_ID_KEY);

        // Inflate the Android-Me fragment layout
        View rootView = inflater.inflate(R.layout.fragment_body_part, container, false);

        // Get a reference to the ImageView in the fragment layout
        ImageView imageView = (ImageView) rootView.findViewById(R.id.body_part_image_view);

        // Set the image to the first in our list of head images
        imageView.setImageResource(imgResId);

        // Return the rootView
        return rootView;
    }
}
