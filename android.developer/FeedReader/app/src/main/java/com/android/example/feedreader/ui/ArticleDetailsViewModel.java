package com.android.example.feedreader.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import com.android.example.feedreader.models.Article;
import com.android.example.feedreader.repo.ArticleRepository;
import com.android.example.feedreader.utils.Resource;

import java.util.List;

import javax.inject.Inject;

/**
 * A {@link ViewModel} implementation used by the {@link ArticleListActivity}.
 *
 * Provides access to the {@link ArticleRepository} services.
 */
public class ArticleDetailsViewModel extends ViewModel {
    private ArticleRepository mArticleRepository;

    public static ArticleDetailsViewModel create (FragmentActivity activity) {
        return ViewModelProviders.of(activity).get(ArticleDetailsViewModel.class);
    }

    @Inject
    void setArticleRepository(ArticleRepository repository) {
        mArticleRepository = repository;
    }

    public LiveData<Article> getArticle(long articleId) {
        return mArticleRepository.getArticle(articleId);
    }
}
