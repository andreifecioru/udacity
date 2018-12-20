package com.android.example.feedreader.api;

import android.arch.lifecycle.LiveData;

import com.android.example.feedreader.models.Article;
import com.android.example.feedreader.utils.ApiResponse;

import java.util.List;

import retrofit2.http.GET;

/**
 * Definition of the article-feed web API as required by the Retrofit lib.
 */
public interface FeedApi {
    @GET("xyz-reader-json")
    LiveData<ApiResponse<List<Article>>> fetchArticles();
}
