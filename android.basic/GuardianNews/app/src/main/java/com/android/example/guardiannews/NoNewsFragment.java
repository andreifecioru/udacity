package com.android.example.guardiannews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Concrete implementation of the {@link BaseFragment} abstract class
 *
 * Implements the UI fragment for displaying a "message-screen" when no news items are available.
 */
public class NoNewsFragment extends BaseFragment {
    private final static String LOG_TAG = NoNewsFragment.class.getSimpleName();

    static String REASON_KEY = "reason";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.no_news_fragment, container, false);

        // get the reason for which we have no news
        String reason = getArguments().getString(REASON_KEY);

        // fill-in the UI
        TextView reasonTextView = mRootView.findViewById(R.id.no_news_text_view);
        if (reasonTextView != null) {
            reasonTextView.setText(reason);
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
