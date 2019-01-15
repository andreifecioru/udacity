package com.example.android.funtravel;

import java.io.IOException;

import android.content.Intent;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.android.funtravel.ui.OfferDetailsActivity;
import com.example.android.funtravel.utils.MockServerDispatcher;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import com.example.android.funtravel.ui.ListOffersActivity;

import org.junit.*;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static com.example.android.funtravel.ui.OfferOverviewFragment.OFFER_ARGS_KEY;
import static org.hamcrest.Matchers.*;
import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;

import static androidx.test.espresso.intent.Intents.*;
import static androidx.test.espresso.intent.matcher.IntentMatchers.*;

import static com.example.android.funtravel.utils.Matchers.recyclerViewWithItemCount;
import static com.example.android.funtravel.utils.Matchers.withRecyclerView;


/**
 * This test verifies the proper functioning of the {@link ListOffersActivity} when
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
public class OfferListActivityTest {
    @Rule
    public IntentsTestRule<ListOffersActivity> mActivityTestRule =
            new IntentsTestRule<>(ListOffersActivity.class, false, false);

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

        ListOffersActivity activity = mActivityTestRule.getActivity();

        // Once the activity is up, we can register our idling resource.
        mIdlingResource = activity.getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);

        // Refresh the DB contents
        activity.onRefresh();
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


    @Test
    public void successfullyLoadingOffers() {
        // Check that the empty-view for our recycler view is not displayed
        onView(withId(R.id.empty_view))
                .check(matches(not(isDisplayed())));

        // Once the loading is complete, we expect the grid layout to display exactly one item
        onView(withId(R.id.rv_offer_list))
                .check(matches(recyclerViewWithItemCount(1)));

        // Let's also check the title of the offer
        onView(withRecyclerView(R.id.rv_offer_list)
                .atPosition(0))
                .check(matches(hasDescendant(withText("Lorem ipsum dolor sit amet"))));
    }

    @Test
    public void loadOffers_itemClick() {
        // Perform a "click" on the first (and only) item in the grid view
        onView(withRecyclerView(R.id.rv_offer_list)
                .atPosition(0))
                .perform(click());

        // Verify that the intent is properly formulated.
        intended(allOf(
                // the intent is internal (to an activity in the same app)
                isInternal(),
                // ... and points to the desired activity
                hasComponent(OfferDetailsActivity.class.getName()),
                // ... and contains the expected extra info
                hasExtraWithKey(OFFER_ARGS_KEY)
        ));
    }
}
