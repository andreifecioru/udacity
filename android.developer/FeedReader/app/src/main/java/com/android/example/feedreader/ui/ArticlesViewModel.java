package com.android.example.feedreader.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import com.android.example.feedreader.models.Article;
import com.android.example.feedreader.repo.ArticleRepository;
import com.android.example.feedreader.utils.Resource;

import javax.inject.Inject;

/**
 * A {@link ViewModel} implementation used by {@link ArticleListActivity}
 * and {@link ArticleDetailsActivity}.
 *
 * Provides access to the {@link ArticleRepository} services.
 */
public class ArticlesViewModel extends ViewModel {
    private ArticleRepository mArticleRepository;

    public static ArticlesViewModel create (FragmentActivity activity) {
        return ViewModelProviders.of(activity).get(ArticlesViewModel.class);
    }

    @Inject
    void setArticleRepository(ArticleRepository repository) {
        mArticleRepository = repository;
    }

    LiveData<Resource<Long>> fetchArticles() {
        return mArticleRepository.fetchArticles();
    }

    LiveData<Article> getArticleAtPosition(long position) {
        return mArticleRepository.getArticleAtPosition(position);
    }

    public LiveData<Article> getArticleById(long articleId) {
        return mArticleRepository.getArticle(articleId);
    }

    public void deleteAllArticles(Runnable onComplete) {
        mArticleRepository.deleteAllArticles(onComplete);
    }
}
