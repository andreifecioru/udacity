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
package com.example.android.background.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class WaterReminderFirebaseJobService extends JobService {
    private static final String LOG_TAG = ReminderUtilities.class.getSimpleName();

    private AsyncTask mBackgroundTask;

    @Override
    public boolean onStartJob(final JobParameters job) {
        mBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Context context = WaterReminderFirebaseJobService.this;
                ReminderTasks.executeTask(context, ReminderTasks.ACTION_CHARGING_REMINDER);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                // job successful: no need for rescheduling
                jobFinished(job, false);
            }
        };

        mBackgroundTask.execute();

        Log.i(LOG_TAG, "Service is executing...");

        // job not finished - we're doing work in background
        return true;
    }

    // called when the job execution constraints are no longer met
    @Override
    public boolean onStopJob(JobParameters job) {
        // are we doing someth. in the background?
        // if yes, cancel it
        if (mBackgroundTask != null) mBackgroundTask.cancel(true);

        // re-try the job when constraints are re-met
        return true;
    }
}
