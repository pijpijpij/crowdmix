package com.pij.android;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

/**
 * @author Pierrejean on 04/10/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class DelegatingCallbackTest {

    @Mock
    private Callback<String> mockDelegate;

    @Test(expected = NullPointerException.class)
    public void test_NullDelegate_Throws() {
        //noinspection ConstantConditions
        new DelegatingCallback<String>(null);
    }

    @Test
    public void test_Success_PassedOn() {
        final DelegatingCallback<String> tested = new DelegatingCallback<>(mockDelegate);
        final Result<String> argument = new Result<>("Zip", null);

        tested.success(argument);

        verify(mockDelegate).success(argument);
    }

    @Test
    public void test_Failure_PassedOn() {
        final DelegatingCallback<String> tested = new DelegatingCallback<>(mockDelegate);
        final TwitterException argument = new TwitterException("zap!");

        tested.failure(argument);

        verify(mockDelegate).failure(argument);
    }
}