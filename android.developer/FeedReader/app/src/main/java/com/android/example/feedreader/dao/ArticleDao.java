package com.android.example.feedreader.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.android.example.feedreader.models.Article;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * DAO definition for the article model.
 * Services provided:
 *  - retrieve a particular article by ID
 *  - insert a list of articles into DB
 *  - clear all the articles from DB (used during testing)
 */
@Dao
public interface ArticleDao {
    @Query("SELECT * FROM articles WHERE id = :articleId")
    LiveData<List<Article>> getArticle(long articleId);

    @Query("SELECT * FROM articles ORDER BY id ASC LIMIT 1 OFFSET :position")
    LiveData<List<Article>> getArticleAtPosition(long position);

    @Query("SELECT count(*) FROM articles")
    LiveData<Long> getArticleCount();

    @Insert(onConflict = REPLACE)
    void insertArticles(Article... articles);

    @Query("DELETE FROM articles")
    void deleteAllArticles();
}
