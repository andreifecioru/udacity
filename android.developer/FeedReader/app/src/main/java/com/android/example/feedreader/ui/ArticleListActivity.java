package com.android.example.feedreader.ui;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.android.example.feedreader.FeedReaderApp;
import com.android.example.feedreader.R;
import com.android.example.feedreader.models.Article;
import com.android.example.feedreader.utils.Resource;
import com.android.example.feedreader.utils.ui.RecyclerViewEmptyViewSupport;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.example.feedreader.ui.ArticleDetailsActivity.ARTICLE_POS_EXTRA_KEY;


public class ArticleListActivity
        extends BaseActivity
        implements ArticleAdapter.OnArticleClick,
                   SwipeRefreshLayout.OnRefreshListener {
    private static final String LOG_TAG = ArticleListActivity.class.getSimpleName();

    private static final String BUNDLE_RECYCLER_LAYOUT_STATE = "recycler.layout.state";

    @BindView(R.id.layout_coordinator) CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.rv_article_list) RecyclerViewEmptyViewSupport mArticlesRecyclerView;
    @BindView(R.id.empty_view) LinearLayout mEmptyView;
    @BindView(R.id.empty_view_no_articles) LinearLayout mEmptyViewNoArticles;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindInt(R.integer.list_column_count) int mListColumnCount;

    private ArticleAdapter mArticleAdapter;
    private Parcelable mRecyclerViewState;
    private ArticlesViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        ButterKnife.bind(this);

        // Get a handle on the view model
        mViewModel = ArticlesViewModel.create(this);
        // ... and inject it into our DI setup
        FeedReaderApp.getInstance().getAppComponent().inject(mViewModel);

        // Setup the layout-manager for our grid
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(mListColumnCount, StaggeredGridLayoutManager.VERTICAL);
        mArticlesRecyclerView.setLayoutManager(layoutManager);

        // Setup the adapter for our recycler view (start with an empty list)
        mArticleAdapter = new ArticleAdapter( this, mViewModel, this);
        mArticlesRecyclerView.setAdapter(mArticleAdapter);

        // Set the empty view for our recycler view
        mArticlesRecyclerView.setEmptyView(mEmptyView);

        // Set the listener for our swipe-to-refresh layout
        mSwipeRefreshLayout.setOnRefreshListener(this);

        // Setup the action-bar and the home button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setLogo(R.drawable.logo);
        }

        // Kick-start data loading (initializes the view model)
        mSwipeRefreshLayout.setRefreshing(true);
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

                            mArticleAdapter.notifyChanged(resource.data);
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        break;

                    case SUCCESS:
                        Log.d(LOG_TAG, "Fetched " + resource.data + " articles.");
                        if (resource.data != null) {
                            mArticleAdapter.notifyChanged(resource.data);
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                        break;

                    case ERROR:
                        if (resource.data != null) {
                            mArticleAdapter.notifyChanged(resource.data);
                        }
                        mSwipeRefreshLayout.setRefreshing(false);

                        Snackbar.make(mCoordinatorLayout,
                                getResources().getText(R.string.error_loading_article_list),
                                Snackbar.LENGTH_SHORT).show();

                        Log.e(LOG_TAG, "Error loading article list. " + resource.message);
                        break;

                    default:
                        throw new IllegalStateException("Unknown resource status: " + resource.status);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_list_articles_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.menu_refresh:
                Log.d(LOG_TAG, "Fetching articles on user request.");
                mSwipeRefreshLayout.setRefreshing(true);
                fetchArticles();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Save/restore the state of the recycler view to persist the scrolling position
    // while switching between apps.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        RecyclerView.LayoutManager layoutManager = mArticlesRecyclerView.getLayoutManager();

        if (layoutManager != null) {
            outState.putParcelable(BUNDLE_RECYCLER_LAYOUT_STATE,
                    layoutManager.onSaveInstanceState());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            mRecyclerViewState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT_STATE);
            restoreRecyclerViewState();
        }
    }

    private void restoreRecyclerViewState() {
        if (mRecyclerViewState != null) {
            RecyclerView.LayoutManager layoutManager = mArticlesRecyclerView.getLayoutManager();

            if (layoutManager != null) {
                layoutManager.onRestoreInstanceState(mRecyclerViewState);
            }
        }
    }

    @Override
    public void onArticleClick(Article article, int position) {
        Log.d(LOG_TAG, "Details for position: " + position);
        Intent intent = new Intent(this, ArticleDetailsActivity.class);
        intent.putExtra(ARTICLE_POS_EXTRA_KEY, position);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        Log.d(LOG_TAG, "Fetching articles on user request.");
        fetchArticles();
    }
}
