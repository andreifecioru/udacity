package com.android.example.feedreader.repo;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.android.example.feedreader.models.Article;
import com.android.example.feedreader.utils.ApiResponse;
import com.android.example.feedreader.utils.NetworkBoundResource;
import com.android.example.feedreader.api.FeedApi;
import com.android.example.feedreader.dao.ArticleDao;

import com.android.example.feedreader.utils.Resource;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * This class implements the "repository" abstraction for articles
 * as described in the AAC official docs. It is the
 * "single source of truth" for all article-related info.
 *
 * See: https://developer.android.com/jetpack/docs/guide
 */
@Singleton
public class ArticleRepository {
    private static final String LOG_TAG = ArticleRepository.class.getSimpleName();

    private final FeedApi mFeedApi;

    // access DB via DAOs
    private final ArticleDao mArticleDao;

    // access the DB on a background thread
    private final Executor mExecutor;

    @Inject
    ArticleRepository(FeedApi feedApi, Executor executor,
                      ArticleDao articleDao) {
        mFeedApi = feedApi;
        mArticleDao = articleDao;
        mExecutor = executor;
    }

    /**
     * This is actually a helper method used during testing.
     *
     * @param onComplete callback invoked when the operation is complete.
     * */
    @WorkerThread
    public void deleteAllArticles(final Runnable onComplete) {
        // we delete all articles on a background thread.
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mArticleDao.deleteAllArticles();
                onComplete.run();
            }
        });
    }

    /**
     * We fetch the articles using the {@link NetworkBoundResource} implementation.
     *
     * This allows us to download the articles only once and then hit the DB cache
     * multiple times when needed. This also provides the off-line support we want.
     */
    public LiveData<Resource<Long>> fetchArticles() {
        return new NetworkBoundResource<Long, List<Article>>() {

            @Override
            protected void saveCallResult(@NonNull List<Article> articles) {
                Log.d(LOG_TAG, "Saving data to the DB (" + articles.size() +" entries).");

                // Perform DB insertions
                Article[] arrArticles = new Article[articles.size()];
                articles.toArray(arrArticles);
                mArticleDao.insertArticles(arrArticles);
            }

            @Override
            protected boolean shouldFetch(@Nullable Long articleCount) {
                // fetch if we have nothing in local cache
                return articleCount == null || articleCount == 0;
            }

            @NonNull
            @Override
            protected LiveData<Long> loadFromDb() {
                Log.d(LOG_TAG, "Loading data from DB.");
                return mArticleDao.getArticleCount();
            }

            @Override
            protected LiveData<ApiResponse<List<Article>>> createCall() {
                return mFeedApi.fetchArticles();
            }
        }.getAsLiveData();
    }

    /**
     * Fetch the article with a specified ID and wrap the result in LiveData.
     *
     * @param articleId the ID of the article we are fetching.
     */
    public LiveData<Article> getArticle(final long articleId) {
        return Transformations.map(mArticleDao.getArticle(articleId), new Function<List<Article>, Article>() {
            @Override
            public Article apply(List<Article> articles) {
                if (articles != null && !articles.isEmpty()) {
                    return articles.get(0);
                }
                return null;
            }
        });
    }

    /**
     * Fetch the n-th article (sorted by ID) and wrap the result in LiveData.
     * Acts pretty much like a DB cursor (i.e. provides random access to the
     * rows in the DB).
     *
     * This query is used by the various adapters that access articles based on
     * a "position".
     *
     * @param position the "position" of the article in the set of articles sorted
     *                 by their ID.
     */
    public LiveData<Article> getArticleAtPosition(final long position) {
        return Transformations.map(mArticleDao.getArticleAtPosition(position),
            new Function<List<Article>, Article>() {
                @Override
                public Article apply(List<Article> articles) {
                    if (articles != null && !articles.isEmpty()) {
                        return articles.get(0);
                    }
                    return null;
                }
        });
    }
}
