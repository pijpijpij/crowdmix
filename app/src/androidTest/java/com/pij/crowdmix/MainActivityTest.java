package com.pij.crowdmix;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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

    public void test_TitleCorrect() {
        onView(withId(R.id.action_bar)).check(matches(hasDescendant(withText("Crowdmix"))));
    }

    public void test_Refresh_Present() {
        onView(withId(R.id.action_bar)).check(matches(hasDescendant(withText("Refresh"))));
    }
}