package com.pij.crowdmix.ui;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.pij.crowdmix.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;

/**
 * @author Pierrejean on 03/10/2015.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    /**
     * Inits and release Espresso Intents before and after each test run.
     */
    @Rule
    public IntentsTestRule<MainActivity> activityRule = new IntentsTestRule<>(MainActivity.class);

    private ViewInteraction onLoginButton() {
        return onView(allOf(withId(R.id.login), withText("Log in with Twitter")));
    }

    @Test
    public void test_InitialScreen_RefreshTitlePresent() {
        onView(allOf(isDescendantOfA(withId(R.id.action_bar)), withText("Crowdmix"))).check(
                matches(isCompletelyDisplayed()));
    }

    @Test
    public void test_InitialScreen_RefreshPresent() {
        onView(allOf(isDescendantOfA(withId(R.id.action_bar)), withText("Refresh"))).check(
                matches(isCompletelyDisplayed()));
    }

    @Test
    public void test_InitialScreen_RefreshDisabled() {
        onView(allOf(isDescendantOfA(withId(R.id.action_bar)), withText("Refresh"))).check(matches(not(isEnabled())));
    }

    public void test_InitialScreen_LoginButtonPresent() {
        onLoginButton().check(matches(isCompletelyDisplayed()));
    }

    //    @Test
    //    public void test_LoginButtonPressed_PopsUp() {
    //        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(0, null));
    //
    //        onLoginButton().perform(click());
    //    }

    @Test
    public void test_AuthenticationFailed_LoginButtonVisible() {
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null));

        onLoginButton().perform(click()).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void test_AuthenticationOK_LoginButtonHidden() {
        intending(not(isInternal())).respondWith(createOKAuthenticationResult());

        onLoginButton().perform(click()).check(doesNotExist());
        //        onLoginButton().perform(click()).check(matches(not(isDisplayed())));
    }

    @NonNull
    private Instrumentation.ActivityResult createOKAuthenticationResult() {
        final Intent data = new Intent();
        data.putExtra("tk", "token");
        data.putExtra("ts", "tokenSecret");
        data.putExtra("screen_name", "screenName");
        data.putExtra("user_id", 0L);
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, data);
    }

}