package com.pij.crowdmix.list;

import android.support.annotation.NonNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Pierrejean on 18/10/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class SimpleTweetListPresenterTest extends TweetListPresenterTest {

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
