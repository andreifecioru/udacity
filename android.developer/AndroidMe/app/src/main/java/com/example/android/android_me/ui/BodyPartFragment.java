package com.example.android.android_me.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.android_me.R;
import com.example.android.android_me.data.AndroidImageAssets;

public class BodyPartFragment extends Fragment {
    public BodyPartFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout into a "root" view
        View rootView = inflater.inflate(R.layout.body_part_fragment, container,false);

        // Find the image UI element
        ImageView bodyPartImage = (ImageView) rootView.findViewById(R.id.iv_body_part);
        // ... and set the image resource
        bodyPartImage.setImageResource(AndroidImageAssets.getHeads().get(0));

        // Return the "root" view
        return rootView;
    }
}
