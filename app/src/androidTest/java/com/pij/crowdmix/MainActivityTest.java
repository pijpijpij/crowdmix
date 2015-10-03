package com.pij.crowdmix;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * @author Pierrejean on 03/10/2015.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity tested;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        tested = getActivity();
    }

    public void test_Title_Correct() {
        onView(allOf(isDescendantOfA(withId(R.id.action_bar)), withText("Crowdmix"))).check(
                matches(isCompletelyDisplayed()));
    }

    public void test_Refresh_Present() {
        onView(allOf(isDescendantOfA(withId(R.id.action_bar)), withText("Refresh"))).check(
                matches(isCompletelyDisplayed()));
    }

    public void test_EmptyText_Present() {
        onView(allOf(isDescendantOfA(withId(R.id.fragment)), withText("There aren't any tweet."))).check(
                matches(isCompletelyDisplayed()));
    }
}
