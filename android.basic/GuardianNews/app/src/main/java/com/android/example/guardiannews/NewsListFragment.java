package com.android.example.guardiannews;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.example.guardiannews.models.NewsItem;

import java.util.List;


/**
 * Concrete implementation of the {@link BaseFragment} abstract class
 *
 * Implements the UI fragment for displaying the list of news items when available.
 */
public class NewsListFragment extends BaseFragment {
    private static final String LOG_TAG = NewsListFragment.class.getSimpleName();

    static final String NEWS_ITEM_LIST_KEY = "news_item_list";

    private OnNewsItemClickListener mOnNewsItemClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.news_list_fragment, container, false);

        // let's get the news list
        List<NewsItem> newsItemsList = getArguments().getParcelableArrayList(NEWS_ITEM_LIST_KEY);


        // fill-in the UI
        ListView newsListView = mRootView.findViewById(R.id.news_list_view);
        if (newsListView != null) {
            ArrayAdapter<NewsItem> newsItemAdapter =
                    new NewsItemAdapter(getActivity().getApplicationContext(), newsItemsList);

            newsListView.setAdapter(newsItemAdapter);

            // set the click listener on list items
            newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    NewsItem newsItem = (NewsItem) parent.getAdapter().getItem(position);

                    if (newsItem != null) {
                        String url = newsItem.getUrl();
                        Log.d(LOG_TAG, "Opening URL: " + url);

                        // defer to the parent activity to handle the actual navigation
                        mOnNewsItemClickListener.onNewsItemClicked(url);
                    }
                }
            });
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // make sure that the parent activity adheres to the communication protocol imposed by this fragment
        if (context instanceof OnNewsItemClickListener) {
            mOnNewsItemClickListener = (OnNewsItemClickListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement NewsListFragment.OnNewsItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnNewsItemClickListener = null;
    }

    // defines the communication protocol with the parent activity
    interface OnNewsItemClickListener {
        void onNewsItemClicked(String url);
    }
}
