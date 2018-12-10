package com.udacity.gradle.builditbigger;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.android.example.jokedisplay.JokeDisplayActivity;
import com.udacity.gradle.builditbigger.ui.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.*;

import static android.support.test.espresso.intent.Intents.*;
import static android.support.test.espresso.intent.matcher.IntentMatchers.*;

import static com.android.example.jokedisplay.JokeDisplayActivity.JOKE_EXTRA_KEY;


@RunWith(AndroidJUnit4.class)
public class JokeMainActivityTest {
    @Rule
    public IntentsTestRule<MainActivity> mActivityTestRule =
            new IntentsTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void beforeEachTest() {
        // Register our idling resource.
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @After
    public void afterEachTest() {
        // Un-register our idling resource.
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

    @Test
    public void successfullyLoadJoke() {
        // Press the "tell joke" button
        onView(withId(R.id.btn_tell_joke)).perform(click());


        // When pressing the "tell joke" button, an internal intent
        // is issued with the joke content inside it.
        // We verify that the intent is properly formulated:
        //  - has the expected key
        //  - payload (the joke text) is not null
        intended(allOf(
                // the intent is internal (to an activity in the same app)
                isInternal(),
                // ... and points to the desired activity
                hasComponent(JokeDisplayActivity.class.getName()),
                // ... and contains the expected extra info
                hasExtraWithKey(JOKE_EXTRA_KEY),
                hasExtra(is(JOKE_EXTRA_KEY), not(isEmptyOrNullString()))
        ));
    }
}
