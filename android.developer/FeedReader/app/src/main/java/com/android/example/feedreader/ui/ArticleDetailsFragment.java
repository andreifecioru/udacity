package com.android.example.feedreader.ui;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.example.feedreader.R;
import com.android.example.feedreader.models.Article;
import com.android.example.feedreader.utils.ui.DynamicHeightNetworkImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ArticleDetailsFragment
        extends Fragment
        implements DynamicHeightNetworkImageView.Protocol {
    private static final String LOG_TAG = ArticleDetailsFragment.class.getSimpleName();

    private static final String ARTICLE_POS_ARGS_KEY = "article.pos.args.key";

    private static final long INVALID_ARTICLE_POS = -1;

    @BindView(R.id.tv_article_subtitle) TextView mSubtitleTextView;
    @BindView(R.id.tv_article_body) TextView mBodyTextView;
    @BindView(R.id.iv_article_photo) DynamicHeightNetworkImageView mPhotoImageView;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.fab_share) FloatingActionButton mShareFab;

    private Protocol mProtocol;

    private long mArticlePos = INVALID_ARTICLE_POS;

    public ArticleDetailsFragment() {}

    public static ArticleDetailsFragment newInstance(long position) {
        Bundle arguments = new Bundle();
        arguments.putLong(ARTICLE_POS_ARGS_KEY, position);
        ArticleDetailsFragment fragment = new ArticleDetailsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            mArticlePos = args.getLong(ARTICLE_POS_ARGS_KEY, INVALID_ARTICLE_POS);
        }

        if (mArticlePos == INVALID_ARTICLE_POS) {
            throw new IllegalStateException("Must provide an article position.");
        }

        Log.d(LOG_TAG, "Article pos: " + mArticlePos);

        View rootView = inflater.inflate(R.layout.fragment_article_details, container, false);
        ButterKnife.bind(this, rootView);

        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        if (parentActivity != null) {
            parentActivity.setSupportActionBar(mToolbar);
        }

        mPhotoImageView.setProtocol(this);

        refreshContent();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Make sure that the context we attach to conforms to the protocol.
        try {
            mProtocol = (Protocol) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement ArticleDetailsFragment.Protocol");
        }
    }

    private void refreshContent() {
        Activity parentActivity = getActivity();
        if (parentActivity != null) {
            mProtocol.getViewModel().getArticleAtPosition(mArticlePos)
                .observe(getActivity(), new Observer<Article>() {
                    @Override
                    public void onChanged(@Nullable Article article) {
                        bindUIControls(article);
                    }
                });
        }
    }

    private void bindUIControls(Article article) {
        if (article == null) return;

        final Context context = getContext();
        if (context == null) return;

        // Set the title
        mToolbarLayout.setTitle(article.getTitle());

        // Set the sub-title
        mSubtitleTextView.setText(article.getSubtitle(" - "));

        // Load the photo and adjust the aspect ratio
        mPhotoImageView.loadImage(article.getPhotoUrl());
        mPhotoImageView.setAspectRatio(article.getAspectRatio());

        // Set the body
        mBodyTextView.setText(article.getBody());
    }

    @Override
    public void onImageLoaded() {
        mProtocol.setContainerBackground();
    }

    public interface Protocol {
        ArticlesViewModel getViewModel();
        void setContainerBackground();
    }

    @OnClick(R.id.fab_share)
    public void onShareFabClick(View view) {
        startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                .setType("text/plain")
                .setText("Some sample text")
                .getIntent(), getString(R.string.action_share)));
    }
}
