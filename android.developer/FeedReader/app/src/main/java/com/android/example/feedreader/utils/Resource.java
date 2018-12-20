package com.android.example.feedreader.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * The "resource" abstraction as described in the AAC official docs.
 *
 * Inspired by from: https://developer.android.com/jetpack/docs/guide#addendum
 */
public class Resource<T> {
    public enum Status { SUCCESS, ERROR, LOADING }

    @NonNull
    public final Status status;

    @Nullable
    public final T data;

    @Nullable
    public final String message;

    private Resource(@NonNull Status status,
                     @Nullable T data,
                     @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String message, @Nullable T cachedData) {
        return new Resource<>(Status.ERROR, cachedData, message);
    }

    public static <T> Resource<T> loading(@Nullable T cachedData) {
        return new Resource<>(Status.LOADING, cachedData, null);
    }
}
