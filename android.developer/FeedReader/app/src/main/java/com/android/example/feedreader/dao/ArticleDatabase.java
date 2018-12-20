package com.android.example.feedreader.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.android.example.feedreader.models.Article;

/**
 * The "articles" DB contains all the info related to posts:
 */
@Database(
        entities = {Article.class},
        version = 1,
        exportSchema = false)
public abstract class ArticleDatabase extends RoomDatabase {
    public abstract ArticleDao getArticleDao();
}

