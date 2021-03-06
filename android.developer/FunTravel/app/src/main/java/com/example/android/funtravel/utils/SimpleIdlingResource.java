package com.example.android.funtravel.utils;

import java.util.concurrent.atomic.AtomicBoolean;

import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;


/**
 * A simple implementation of the {@link IdlingResource} interface.
 *
 * Replicates the code given during the course class.
 */
public class SimpleIdlingResource implements IdlingResource {

    @Nullable
    private volatile ResourceCallback mCallback;

    private AtomicBoolean mIsIdleNow = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }

    public void setIdleState(boolean isIdleNow) {
        mIsIdleNow.set(isIdleNow);
        if (isIdleNow && mCallback != null) {
            mCallback.onTransitionToIdle();
        }
    }
}

