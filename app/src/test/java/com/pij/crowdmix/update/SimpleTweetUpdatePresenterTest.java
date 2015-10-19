package com.pij.crowdmix.update;

import android.support.annotation.NonNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Pierrejean on 18/10/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class SimpleTweetUpdatePresenterTest extends TweetUpdatePresenterTest {

    @NonNull
    protected SimpleTweetUpdatePresenter createDefaultSut() {
        SimpleTweetUpdatePresenter tested = createViewLessSut();
        tested.setView(mockView);
        return tested;
    }

    @NonNull
    protected SimpleTweetUpdatePresenter createViewLessSut() {
        return new SimpleTweetUpdatePresenter(mockProxy);
    }


    @Test(expected = NullPointerException.class)
    public void test_NullProxy_Throws() {
        //noinspection ConstantConditions
        new SimpleTweetUpdatePresenter(null);
    }

}
