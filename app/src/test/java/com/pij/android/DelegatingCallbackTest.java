package com.pij.android;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

/**
 * @author Pierrejean on 04/10/2015.
 */
public class DelegatingCallbackTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private Callback<String> mockDelegate;
    @InjectMocks
    private DelegatingCallback<String> tested;

    @Test(expected = NullPointerException.class)
    public void test_NullDelegate_Throws() {
        //noinspection ConstantConditions
        new DelegatingCallback<String>(null);
    }

    @Test
    public void test_Success_PassedOn() {
        final Result<String> argument = new Result<>("Zip", null);

        tested.success(argument);

        verify(mockDelegate).success(argument);
    }

    @Test
    public void test_Failure_PassedOn() {
        final TwitterException argument = new TwitterException("zap!");

        tested.failure(argument);

        verify(mockDelegate).failure(argument);
    }
}