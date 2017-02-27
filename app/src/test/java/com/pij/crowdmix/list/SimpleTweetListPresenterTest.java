package com.pij.crowdmix.list;

import android.support.annotation.NonNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Pierrejean on 18/10/2015.
 */
public class SimpleTweetListPresenterTest extends TweetListPresenterTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @NonNull
    protected SimpleTweetListPresenter createDefaultSut() {
        SimpleTweetListPresenter tested = new SimpleTweetListPresenter(mockProxy);
        tested.setView(mockView);
        return tested;
    }

    @NonNull
    protected SimpleTweetListPresenter createViewLessSut() {
        return new SimpleTweetListPresenter(mockProxy);
    }

    @Test(expected = NullPointerException.class)
    public void test_NullProxy_Throws() {
        //noinspection ConstantConditions
        new SimpleTweetListPresenter(null);
    }

}
