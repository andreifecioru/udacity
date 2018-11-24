package com.example.android.teatime;

import android.app.Activity;
import android.app.Instrumentation.ActivityResult;
import android.content.Intent;
import android.support.test.runner.AndroidJUnit4;

import org.junit.*;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.action.ViewActions.*;

import static org.hamcrest.Matchers.*;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import static android.support.test.espresso.intent.Intents.*;
import static android.support.test.espresso.intent.matcher.IntentMatchers.*;

@RunWith(AndroidJUnit4.class)
public class OrderSummaryActivityTest {
    private static final String emailMessage = "I just ordered a delicious tea from TeaTime. Next time you are craving a tea, check them out!";

    @Rule
    public IntentsTestRule<OrderSummaryActivity> mActivityRule =
            new IntentsTestRule<>(OrderSummaryActivity.class);

    @Before
    public void stubAllExternalIntents() {
        intending(not(isInternal()))
                .respondWith(new ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void clickSendEmailButton_SendsEmail() {
        onView(withId(R.id.send_email_button))
                .perform(click());

        intended(allOf(
                hasAction(Intent.ACTION_SENDTO),
                hasExtra(Intent.EXTRA_TEXT, emailMessage)
        ));
    }
}
