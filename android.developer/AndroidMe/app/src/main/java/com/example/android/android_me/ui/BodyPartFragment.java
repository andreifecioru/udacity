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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.android_me.R;


public class BodyPartFragment extends Fragment {
    private static final String TAG = BodyPartFragment.class.getSimpleName();

    public static final String IMG_RES_ID_KEY = "img.res.id.key";

    private ImageView mImageView;
    private int mImgResId;
    private OnClickListener mOnClickListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public BodyPartFragment() { }

    /**
     * Inflates the fragment layout file and sets the correct resource for the image to display
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(savedInstanceState != null) {
            mImgResId = savedInstanceState.getInt(IMG_RES_ID_KEY);
        }

        // Inflate the Android-Me fragment layout
        View rootView = inflater.inflate(R.layout.fragment_body_part, container, false);

        // Get a reference to the ImageView in the fragment layout
        mImageView = rootView.findViewById(R.id.body_part_image_view);

        final int id = getId();

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(mOnClickListener != null) {
                   mOnClickListener.onClick(id);
               }
            }
        });

        // Return the rootView
        return rootView;
    }

    public void setImageResourceId(int imgResId) {
        mImgResId = imgResId;
        mImageView.setImageResource(mImgResId);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putInt(IMG_RES_ID_KEY, mImgResId);
    }

    public interface OnClickListener {
        void onClick(int id);
    }
}
