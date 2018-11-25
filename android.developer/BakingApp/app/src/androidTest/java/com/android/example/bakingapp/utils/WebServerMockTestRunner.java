package com.android.example.bakingapp.utils;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.test.runner.AndroidJUnitRunner;


/**
 * Custom test runner allowing us to inject a testing version of our app (see {@link TestBakingApp}.
 *
 * This whole setup is described here (inspiration source):
 * https://medium.com/mindorks/instrumentation-testing-with-mockwebserver-and-dagger2-56778477f0cf
 */
public class WebServerMockTestRunner extends AndroidJUnitRunner {

    @Override
    public void onCreate(Bundle arguments) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        super.onCreate(arguments);
    }

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return super.newApplication(cl, TestBakingApp.class.getName(), context);
    }
}

