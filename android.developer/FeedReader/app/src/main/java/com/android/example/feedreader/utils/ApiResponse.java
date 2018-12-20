package com.android.example.feedreader.utils;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

import retrofit2.Response;

/**
 * A wrapper class on top of the Retrofit response. Used by {@link LiveDataCallAdapter}.
 *
 * Translates the HTTP error codes into string error messages and provides access to the response
 * body via a getter.
 */
public class ApiResponse<T> {
    private final int mCode;

    @Nullable
    private final T mBody;

    @Nullable
    private final String mError;

    ApiResponse(@Nullable String error) {
        mCode = 500;
        mBody = null;
        mError = error;
    }

    ApiResponse(Response<T> response) {
        mCode = response.code();

        if(response.isSuccessful()) {
            mBody = response.body();
            mError = null;
        } else {
            String message = null;
            if (response.errorBody() != null) {
                try {
                    message = response.errorBody().string();
                } catch (IOException ignored) {
                    Log.e("ERROR", "error while parsing response", ignored);
                }
            }
            if (message == null || message.trim().length() == 0) {
                message = response.message();
            }
            mError = message;
            mBody = null;
        }
    }

    public boolean isSuccessful() {
        return mCode >= 200 && mCode < 300;
    }

    public int getCode() {
        return mCode;
    }

    @Nullable
    public T getBody() {
        return mBody;
    }

    @Nullable
    public String getErrorMessage() {
        return mError;
    }
}
