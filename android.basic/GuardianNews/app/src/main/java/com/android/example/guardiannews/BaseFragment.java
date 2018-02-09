package com.android.example.guardiannews;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Abstract class representing the starting point for all the fragments
 * used by {@link MainActivity}.
 *
 * Encapsulates the shared functionality between concrete implementations:
 *   - a button on the bottom of the fragment which triggers a call to {@link OnRefreshNewsListener#onRefreshNews()} callback
 */
abstract public class BaseFragment extends Fragment {
    private static final String LOG_TAG = NewsListFragment.class.getSimpleName();

    protected View mRootView;

    protected OnRefreshNewsListener mOnRefreshNewsListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Button refreshNewsButton = mRootView.findViewById(R.id.refresh_news_button);
        if (refreshNewsButton != null) {
            refreshNewsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LOG_TAG, "Refreshing the news list...");
                    if (mOnRefreshNewsListener != null) {
                        mOnRefreshNewsListener.onRefreshNews();
                    }
                }
            });
        }

        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // make sure that the parent activity adheres to the communication protocol imposed by this fragment
        if (context instanceof OnRefreshNewsListener) {
            mOnRefreshNewsListener = (OnRefreshNewsListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement BaseFragment.OnRefreshNewsListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnRefreshNewsListener = null;
    }

    // defines the communication protocol with the parent activity
    interface OnRefreshNewsListener {
        void onRefreshNews();
    }
}
