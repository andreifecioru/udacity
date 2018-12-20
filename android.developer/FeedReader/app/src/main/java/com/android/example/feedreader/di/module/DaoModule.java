package com.android.example.feedreader.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.android.example.feedreader.dao.ArticleDao;
import com.android.example.feedreader.dao.ArticleDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * DI module providing access to the DB via various DAO objects.
 */
@Module
public class DaoModule {
    private static final String DATABASE_NAME = "feedreader.db";

    @Provides
    @Singleton
    public ArticleDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application, ArticleDatabase.class, DATABASE_NAME)
                .build();
    }

    @Provides
    @Singleton
    public ArticleDao provideArticleDao(ArticleDatabase db) {
        return db.getArticleDao();
    }

    @Provides
    @Singleton
    public Executor provideSingleThreadExecutor() {
        return Executors.newSingleThreadExecutor();
    }
}

