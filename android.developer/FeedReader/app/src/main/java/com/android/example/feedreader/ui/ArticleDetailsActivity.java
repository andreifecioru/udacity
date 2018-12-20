package com.android.example.feedreader.ui;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.Window;

import com.android.example.feedreader.FeedReaderApp;
import com.android.example.feedreader.R;
import com.android.example.feedreader.utils.Resource;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ArticleDetailsActivity
        extends AppCompatActivity
        implements ArticleDetailsFragment.Protocol {
    private static final String LOG_TAG = ArticleDetailsActivity.class.getSimpleName();

    public static final String ARTICLE_POS_EXTRA_KEY = "article.pos.extra.key";

    @BindView(R.id.pv_article_detail) ViewPager mArticleViewPager;
    @BindView(R.id.container_cl) ConstraintLayout mContainerLayout;

    private ArticlesViewModel mViewModel;
    private ArticlePagerAdapter mPagerAdapter;
    private int mInitialArticlePos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);

        // Get a handle on the view model
        mViewModel = ArticlesViewModel.create(this);
        // ... and inject it into our DI setup
        FeedReaderApp.getInstance().getAppComponent().inject(mViewModel);

        Intent intent = getIntent();
        mInitialArticlePos = intent.getIntExtra(ARTICLE_POS_EXTRA_KEY, 0);

        ButterKnife.bind(this);

        mPagerAdapter = new ArticlePagerAdapter(getSupportFragmentManager());
        mArticleViewPager.setAdapter(mPagerAdapter);


        // Setup the action-bar and the home button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            // make it transparent
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            actionBar.setElevation(0);
        }

        // Query the DB for the article list
        fetchArticles();
    }

    private void fetchArticles() {
        Log.d(LOG_TAG, "Fetching articles...");

        // Watch the view model for any changes in the underlying live data
        mViewModel.fetchArticles().observe(this, new Observer<Resource<Long>>() {
            @Override
            public void onChanged(@Nullable Resource<Long> resource) {
                if (resource == null) return;

                switch (resource.status) {
                    case LOADING:
                        if (resource.data == null) {
                            Log.d(LOG_TAG, "No data available in local cache. Loading...");
                        } else {
                            Log.d(LOG_TAG, "Data available in local cache. Loading...");
                            // Data loading is still in progress
                            // but we have some cache data to display
                            // until data is ready.

                            mPagerAdapter.notifyChanged(resource.data);
                            mArticleViewPager.setCurrentItem(mInitialArticlePos);
                        }
                        break;

                    case SUCCESS:
                        Log.d(LOG_TAG, "Fetched " + resource.data + " articles.");
                        if (resource.data != null) {
                            mPagerAdapter.notifyChanged(resource.data);
                            mArticleViewPager.setCurrentItem(mInitialArticlePos);
                        }
                        break;

                    case ERROR:
                        if (resource.data != null) {
                            mPagerAdapter.notifyChanged(resource.data);
                            mArticleViewPager.setCurrentItem(mInitialArticlePos);
                        }

//                        Snackbar.make(mCoordinatorLayout,
//                                getResources().getText(R.string.error_loading_article_list),
//                                Snackbar.LENGTH_SHORT).show();

                        Log.e(LOG_TAG, "Error loading article list. " + resource.message);
                        break;

                    default:
                        throw new IllegalStateException("Unknown resource status: " + resource.status);
                }
            }
        });
    }

    @Override
    public ArticlesViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public void setContainerBackground() {
        int pos = mArticleViewPager.getCurrentItem();
        ArticleDetailsFragment fragment = (ArticleDetailsFragment) mPagerAdapter.getItemAtPos(pos);
        if (fragment != null) {
            Bitmap bitmap = fragment.mPhotoImageView.getBitmap();
            mContainerLayout.setBackgroundColor(computeContainerBgColor(bitmap));
        } else {
            Log.d(LOG_TAG, "Fragment not found at pos: " + pos);
        }
    }

    private static int computeContainerBgColor(Bitmap bitmap) {
        if (bitmap == null) return Color.LTGRAY;

        Palette palette = Palette.from(bitmap).generate();
        return palette.getLightMutedColor(Color.LTGRAY);
    }
}
