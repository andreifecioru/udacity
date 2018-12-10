package com.udacity.gradle.builditbigger.ui;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.arch.lifecycle.LiveData;

import com.android.example.jokedisplay.JokeDisplayActivity;
import com.udacity.gradle.builditbigger.BuildItBiggerApp;
import com.udacity.gradle.builditbigger.R;
import com.android.example.jokedisplay.models.Joke;
import com.udacity.gradle.builditbigger.utils.Resource;
import com.udacity.gradle.builditbigger.utils.SimpleIdlingResource;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.example.jokedisplay.JokeDisplayActivity.JOKE_EXTRA_KEY;


/**
 * This is the app's main (initial) screen. The user requests a joke by pressing
 * the "tell joke" button.
 *
 * We do not launch an AsyncTask as suggested in the project instructions. Instead,
 * we use the AAC patterns: we attach a ViewModel to the activity and load the joke data async
 * by wrapping the response from our "jokes" endpoint into a {@link LiveData} structure (via
 * Retrofit lib).
 *
 * NOTE: there are 2 levels of wrapping on top of the Joke object model. One is {@link LiveData} and another
 * one is {@link Resource} (described in the official docs for AAC) through which we get insight
 * in the state of the underlying resource (whether loading is in progress, successful or failed).
 */
public class MainActivity extends BaseActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;
    @BindView(R.id.btn_tell_joke) Button mTellJokeButton;
    @BindView(R.id.instructions_text_view) TextView mInstructionsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mInstructionsTextView.setVisibility(View.VISIBLE);
        mTellJokeButton.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_tell_joke)
    public void tellJoke(View view) {
        getJoke();
    }

    @VisibleForTesting
    public void getJoke() {
        Log.d(LOG_TAG, "Refreshing the recipe list...");

        // Lock idling resource
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }

        // Get a handle on the view model
        MainActivityViewModel viewModel = MainActivityViewModel.create(this);
        // ... and inject it into our DI setup
        BuildItBiggerApp.getInstance().getAppComponent().inject(viewModel);

        // Show the loading indicator
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mInstructionsTextView.setVisibility(View.INVISIBLE);
        mTellJokeButton.setVisibility(View.INVISIBLE);

        // Watch the view model for any changes in the underlying live data
        viewModel.getJoke().observe(this, new Observer<Resource<Joke>>() {
            @Override
            public void onChanged(@Nullable Resource<Joke> resource) {
                if (resource == null) return;

                switch (resource.status) {
                    case LOADING:
                        Log.d(LOG_TAG, "Loading...");
                        break;

                    case SUCCESS:
                        Log.d(LOG_TAG, "Data is available: " + resource.data);

                        Intent intent = new Intent(MainActivity.this, JokeDisplayActivity.class);
                        intent.putExtra(JOKE_EXTRA_KEY, resource.data);
                        MainActivity.this.startActivity(intent);

                        // Hide the loading progress indicator
                        mLoadingIndicator.setVisibility(View.GONE);
                        break;

                    case ERROR:
                        Log.e(LOG_TAG, "Error loading recipe list. " + resource.message);

                        // Hide the loading progress indicator
                        mLoadingIndicator.setVisibility(View.GONE);
                        mInstructionsTextView.setVisibility(View.VISIBLE);
                        mInstructionsTextView.setText(getString(R.string.try_again));
                        break;

                    default:
                        throw new IllegalStateException("Unknown resource status: " + resource.status);
                }

                // Release idling resource
                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(true);
                }
            }
        });
    }

    @Override
    protected void onConnectivityLost() {
        super.onConnectivityLost();
        mTellJokeButton.setEnabled(false);
    }

    @Override
    protected void onConnectivityRestored() {
        super.onConnectivityRestored();
        mTellJokeButton.setEnabled(true);
    }

    // Setup of idling resource used during instrumented testing.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }

        return mIdlingResource;
    }
}
