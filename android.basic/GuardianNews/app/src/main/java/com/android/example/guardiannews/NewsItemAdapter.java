package com.android.example.guardiannews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.example.guardiannews.models.NewsItem;

import java.util.List;


/**
 * Array adapter for the {@link NewsItem} class. Fuels the list-view
 * which displays the news feed.
 */
class NewsItemAdapter extends ArrayAdapter<NewsItem> {
    private static class ViewHolder {
        TextView titleTextView;
        TextView sectionTextView;
        TextView authorTextView;
        TextView dateTextView;
        TextView timeTextView;
    }

    /** Produces an instance of the {@link NewsItemAdapter} class (constructor)
     *
     * @param context Application context (used for accessing app resources)
     * @param items A list of {@link NewsItem} objects (the raw data source)
     */
    NewsItemAdapter(Context context, List<NewsItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // obtain the the news item for the current position in the list-view
        final NewsItem newsItem = getItem(position);

        if (newsItem == null) return convertView;

        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.news_list_item, parent, false);

            // cache the relevant views (so we don't have to search for them every time)
            viewHolder = new ViewHolder();
            viewHolder.titleTextView = convertView.findViewById(R.id.title_text_view);
            viewHolder.sectionTextView = convertView.findViewById(R.id.section_text_view);
            viewHolder.authorTextView = convertView.findViewById(R.id.author_text_view);
            viewHolder.dateTextView = convertView.findViewById(R.id.date_text_view);
            viewHolder.timeTextView = convertView.findViewById(R.id.time_text_view);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // populate the UI with the relevant data.
        viewHolder.titleTextView.setText(newsItem.getTitle());
        viewHolder.sectionTextView.setText(newsItem.getSection());
        viewHolder.authorTextView.setText(newsItem.getAuthorName());
        viewHolder.dateTextView.setText(newsItem.getPublishDate());
        viewHolder.timeTextView.setText(newsItem.getPublishTime());

        return convertView;
    }
}
