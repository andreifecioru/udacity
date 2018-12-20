package com.android.example.feedreader.ui;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.example.feedreader.R;
import com.android.example.feedreader.models.Article;
import com.android.example.feedreader.repo.ArticleRepository;
import com.android.example.feedreader.utils.ui.DynamicHeightNetworkImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RecyclerView adapter for the {@link Article} class. Fuels the grid-view
 * which displays the list of articles.
 */
public class ArticleAdapter
        extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {
    private static final String LOG_TAG = ArticleAdapter.class.getSimpleName();

    private final OnArticleClick mOnArticleClick;
    private final ArticlesViewModel mViewModel;
    private final LifecycleOwner mOwner;

    private long mArticleCount = 0;

    /** Produces an instance of the {@link ArticleAdapter} class (constructor)
     *
     *
     * @param owner An instance of {@link LifecycleOwner} (allows us to observe {@link LiveData} objects.
     * @param viewModel An instance of {@link ArticlesViewModel} (provides access to {@link ArticleRepository}).
     * @param onArticleClick Implementation of the {@link OnArticleClick} interface
     *                      where you can specify custom logic for clicking a article item.
     */
    ArticleAdapter(LifecycleOwner owner,
                   ArticlesViewModel viewModel,
                   OnArticleClick onArticleClick) {
        mOwner = owner;
        mViewModel = viewModel;
        mOnArticleClick = onArticleClick;
    }

    /**
     * Forces an update of the adapter view.
     *
     * @param articleCount The number of articles to be displayed by this adapter.
     */
    public void notifyChanged(long articleCount) {
        mArticleCount = articleCount;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for the article item.
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.article_grid_item, parent, false);

        // Return the view holder
        return new ArticleViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull final ArticleViewHolder holder, int position) {
        final int pos = position;
        // Get the article at position
        mViewModel.getArticleAtPosition(position).observe(mOwner, new Observer<Article>() {
            @Override
            public void onChanged(@Nullable Article article) {
                // Bind data to the view-holder
                holder.bindArticle(article, pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (int) mArticleCount;
    }

    // Implements the view-holder pattern.
    class ArticleViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {


        @BindView(R.id.iv_thumbnail) DynamicHeightNetworkImageView mThumbImageView;
        @BindView(R.id.tv_article_title) TextView mArticleTitleTextView;
        @BindView(R.id.tv_article_subtitle) TextView mArticleSubtitleTextView;

        private Article mArticle;
        private int mPosition;

        ArticleViewHolder(@NonNull View root) {
            super(root);

            ButterKnife.bind(this, root);

            // Install on-click handler
            root.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnArticleClick.onArticleClick(mArticle, mPosition);
        }

        // Binding method.
        void bindArticle(Article article, int position) {
            // Sanity check: fast exit
            if (article == null) return;

            mArticle = article;
            mPosition = position;

            // Load the thumbnail image
            mThumbImageView.loadImage(mArticle.getThumbUrl());
            mThumbImageView.setAspectRatio(mArticle.getAspectRatio());

            // Display the article title
            mArticleTitleTextView.setText(mArticle.getTitle());

            // Set the sub-title
            mArticleSubtitleTextView.setText(mArticle.getSubtitle("<br/>"));
        }
    }

    /**
     * Protocol for providing custom logic for handling user taps on article items.
     */
    interface OnArticleClick {
        void onArticleClick(Article article, int position);
    }
}

