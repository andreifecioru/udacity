/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine.sync;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.android.sunshine.data.WeatherContract;


public class SunshineSyncUtils {
    private static boolean sInitialized;

    synchronized public static void initialize(Context context) {
        if (sInitialized) return;

        sInitialized = true;

        new CheckDbTask().execute(context);

    }

    /**
     * Helper method to perform a sync immediately using an IntentService for asynchronous
     * execution.
     *
     * @param context The Context used to start the IntentService for the sync.
     */
    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, SunshineSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }

    private static class CheckDbTask extends AsyncTask<Context, Void, Integer> {
        private Context mContext;

        @Override
        protected Integer doInBackground(Context...contexts) {
            mContext = contexts[0];
            ContentResolver resolver = mContext.getContentResolver();
            String[] projectionColumns = { WeatherContract.WeatherEntry._ID };
            String selectionStatement = WeatherContract.WeatherEntry
                    .getSqlSelectForTodayOnwards();
            Cursor cursor = resolver.query(WeatherContract.WeatherEntry.CONTENT_URI,
                    projectionColumns, selectionStatement, null, null);

            if (cursor != null) {
                cursor.close();
                return cursor.getCount();
            }

            return 0;
        }

        @Override
        protected void onPostExecute(Integer recordCount) {
            if (recordCount == 0) {
                SunshineSyncUtils.startImmediateSync(mContext);
            }
        }
    }
}