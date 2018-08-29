package com.example.android.popularmovies.utils;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;


/**
 * Implementation of the "network-bound resource" abstraction as presented in the official AAC docs
 * (see https://developer.android.com/jetpack/docs/guide#addendum).
 *
 * The implementation is a slightly modified version of the code at this location:
 * https://github.com/BakhtarSobat/GitHubList/blob/master/GitHubList/app/src/main/java/com/bsobat/github/utils/NetworkBoundResource.java
 *
 * What I have changed is the ability to work completely on off-line mode: if the return type of
 * the createCall() callback is null, we go straight to the DB cache.
 */
public abstract class NetworkBoundResource<ResultType, RequestType> {
    private static final String LOG_TAG = NetworkBoundResource.class.getSimpleName();

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType data);

    @MainThread
    protected abstract boolean shouldFetch(@Nullable ResultType data);

    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    @MainThread
    protected abstract LiveData<ApiResponse<RequestType>> createCall();

    @MainThread
    protected void onFetchFailed(String errorMsg) {}

    public final LiveData<Resource<ResultType>> getAsLiveData() {
        return result;
    }

    @MainThread
    public NetworkBoundResource() {
        result.setValue(Resource.<ResultType>loading(null));
        final LiveData<ResultType> dbSource = loadFromDb();

        result.addSource(dbSource, new Observer<ResultType>() {
            @Override
            public void onChanged(@Nullable ResultType data) {
                result.removeSource(dbSource);
                if (shouldFetch(data)) {
                    fetchFromNetwork(dbSource);
                } else {
                    result.addSource(dbSource, new Observer<ResultType>() {
                        @Override
                        public void onChanged(@Nullable ResultType dbData) {
                            result.setValue(Resource.success(dbData));
                        }
                    });
                }
            }
        });
    }

    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {
        final LiveData<ApiResponse<RequestType>> apiSource = createCall();

        // re-attach DB source to dispatch its value as quickly as possible
        result.addSource(dbSource, new Observer<ResultType>() {
            @Override
            public void onChanged(@Nullable ResultType dbData) {
                result.setValue(Resource.loading(dbData));
            }
        });

        // if there is no API call to be made (offline mode)
        if (apiSource != null) {
            result.addSource(apiSource, new Observer<ApiResponse<RequestType>>() {
                @Override
                public void onChanged(@Nullable final ApiResponse<RequestType> response) {
                    result.removeSource(apiSource);
                    result.removeSource(dbSource);

                    if (response != null) {
                        if (response.isSuccessful()) {
                            saveResultAndReInit(response);
                        } else {
                            onFetchFailed(response.getErrorMessage());
                            // failed to fetch data from network; fall-back to DB data
                            result.addSource(dbSource, new Observer<ResultType>() {
                                @Override
                                public void onChanged(@Nullable ResultType dbData) {
                                    Resource.error(response.getErrorMessage(), dbData);
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    @MainThread
    private void saveResultAndReInit(final ApiResponse<RequestType> response) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Log.d(LOG_TAG, "Saving the new data into the DB.");
                saveCallResult(response.getBody());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                result.addSource(loadFromDb(), new Observer<ResultType>() {
                    @Override
                    public void onChanged(@Nullable ResultType newData) {
                        Log.d(LOG_TAG, "Delivering the new data from the DB.");
                        result.setValue(Resource.success(newData));
                    }
                });
            }
        }.execute();
    }
}
