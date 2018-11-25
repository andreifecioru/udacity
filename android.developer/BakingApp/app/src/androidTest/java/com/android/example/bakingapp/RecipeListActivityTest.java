package com.android.example.bakingapp;

import android.content.Intent;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.android.example.bakingapp.ui.RecipeDetailsActivity;
import com.android.example.bakingapp.ui.RecipeListActivity;
import com.android.example.bakingapp.utils.MockServerDispatcher;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static com.android.example.bakingapp.ui.RecipeDetailsActivity.INTENT_EXTRA_RECIPE_KEY;
import static com.android.example.bakingapp.utils.Matchers.*;
import static org.hamcrest.Matchers.*;

import static android.support.test.espresso.intent.Intents.*;
import static android.support.test.espresso.intent.matcher.IntentMatchers.*;


/**
 * This test verifies the proper functioning of the {@link RecipeListActivity} when
 * successfully downloading the recipe list from the remote end-point.
 * <p>
 * It demonstrates the following capabilities of instrumented UI testing:
 * - use of various types of view matchers (user-props, hierarchy, obj-matchers, etc.)
 * - use of the {@link MockWebServer} library for intercepting and mocking
 * the response of remote end-points
 * - use of {@link IdlingResource}s to deal with testing async. UI operations
 * - use of the intent verification APIs.
 */
@RunWith(AndroidJUnit4.class)
public class RecipeListActivityTest {
    @Rule
    public IntentsTestRule<RecipeListActivity> mActivityTestRule =
            new IntentsTestRule<>(RecipeListActivity.class, false, false);

    private MockWebServer webServer;

    private IdlingResource mIdlingResource;

    @Before
    public void beforeEachTest() throws IOException {
        // Start the MockWebServer instance (before launching the test activity)
        webServer = new MockWebServer();
        webServer.start(8080);

        // Setup the response mocking with the help of a custom RequestDispatcher.
        webServer.setDispatcher(new MockServerDispatcher.SuccessfulRequestDispatcher());

        // Finally, launch the activity.
        mActivityTestRule.launchActivity(new Intent());

        RecipeListActivity activity = mActivityTestRule.getActivity();

        // Once the activity is up, we can register our idling resource.
        mIdlingResource = activity.getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);

        // To make sure that tests are repeatable, clean the DB before each test run.
        resetDatabase();
    }

    @After
    public void afterEachTest() throws IOException {
        // Un-register our idling resource.
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }

        // Shut-down the MockWebServer instance.
        webServer.shutdown();
    }

    private void resetDatabase() {
        final AtomicBoolean dbResetComplete = new AtomicBoolean(false);

        final RecipeListActivity activity = mActivityTestRule.getActivity();
        activity.deleteAllRecipes(new Runnable() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.refreshRecipeList();
                        dbResetComplete.set(true);
                    }
                });
            }
        });

        // Block-wait until the DB is cleared.
        while (!dbResetComplete.get()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void successfullyLoadingRecipes() {
        // Check that the loading progress bar indicator is no longer displayed
        // once the recipe-loading is complete
        onView(withId(R.id.pb_empty_view))
                .check(matches(not(isDisplayed())));

        // Once the loading is complete, we expect the grid layout to display exactly one item
        onView(withId(R.id.rv_recipe_list))
                .check(matches(recyclerViewWithItemCount(1)));

        // Let's also check the name of the recipe
        onView(withRecyclerView(R.id.rv_recipe_list)
                .atPosition(0))
                .check(matches(hasDescendant(withText("Nutella Pie"))));
    }

    @Test
    public void loadRecipes_itemClick() {
        // Perform a "click" on the first (and only) item in the grid view
        onView(withRecyclerView(R.id.rv_recipe_list)
                .atPosition(0))
                .perform(click());

        // Verify that the intent is properly formulated.
        intended(allOf(
                // the intent is internal (to an activity in the same app)
                isInternal(),
                // ... and points to the desired activity
                hasComponent(RecipeDetailsActivity.class.getName()),
                // ... and contains the expected extra info
                hasExtraWithKey(INTENT_EXTRA_RECIPE_KEY)
        ));
    }
}
