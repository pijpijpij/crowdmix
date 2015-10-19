package com.pij.crowdmix.ui;

import android.support.annotation.NonNull;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.PerformException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.EditText;

import com.pij.crowdmix.R;
import com.twitter.sdk.android.core.models.Tweet;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Math.max;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;

/**
 * @author Pierrejean on 03/10/2015.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    /**
     * Initialises and release Espresso Intents before and after each test run.
     */
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    private String createRandomString(int maxLength) {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = max(1, generator.nextInt(maxLength));
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char)(generator.nextInt('Z' - ' ') + ' ');
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    @NonNull
    private DataInteraction onTweetList() {return onData(instanceOf(Tweet.class));}

    @Before
    public void ensureLoggedIn() {
        onView(allOf(withId(R.id.login), withText("Log in with Twitter"))).check(matches(not(isDisplayed())));
    }

    @Before
    public void ensureNoKeyboard() {
        Espresso.closeSoftKeyboard();
    }

    @Test
    public void test_InitialScreen_TitlePresent() {
        onView(allOf(isDescendantOfA(withId(R.id.action_bar)), withText("Crowdmix"))).check(
                matches(isCompletelyDisplayed()));
    }

    @Test
    public void test_InitialScreen_RefreshPresent() {
        onView(allOf(isDescendantOfA(withId(R.id.action_bar)), withText("Refresh"))).check(
                matches(isCompletelyDisplayed()));
    }

    @Test
    public void test_InitialScreen_RefreshEnabled() {
        onView(allOf(isDescendantOfA(withId(R.id.action_bar)), withText("Refresh"))).check(matches(isEnabled()));
    }

    @Test
    public void test_InitialScreen__LoginButtonHidden() {
        // Already done in precondition!
    }

    @Test
    public void test_InitialScreen__20tweetsPresent() {

        onTweetList().atPosition(20 - 1).check(matches(isEnabled()));
    }

    @Test(expected = PerformException.class)
    public void test_InitialScreen_21tweetsNotPresent() {

        onTweetList().atPosition(21 - 1).check(doesNotExist());
    }

    @Test
    public void test_AddATWeet_IsShownImmediatly() {
        final String message = createRandomString(10);
        onTweetList().atPosition(0).check(matches(not(hasDescendant(withText(message)))));
        onView(allOf(withId(R.id.message), CoreMatchers.<View>instanceOf(EditText.class))).perform(
                replaceText(message));
        onView(withText("Tweet!")).perform(click());

        onTweetList().atPosition(0).check(matches(hasDescendant(withText(message))));
    }
}
