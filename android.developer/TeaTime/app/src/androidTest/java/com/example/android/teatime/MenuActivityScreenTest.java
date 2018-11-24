package com.example.android.teatime;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.anything;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;

@RunWith(AndroidJUnit4.class)
public class MenuActivityScreenTest {
    @Rule
    public ActivityTestRule<MenuActivity> mActivityTestRule =
            new ActivityTestRule<>(MenuActivity.class);

    @Test
    public void clickGridViewItem_OpensOrderActivity() {
        // Click on the 1st position in the grid-view
        onData(anything())
                .inAdapterView(withId(R.id.tea_grid_view))
                .atPosition(1)
                .perform(click());

        // Check the result
        onView(withId(R.id.tea_name_text_view))
                .check(matches(withText("Green Tea")));
    }
}
