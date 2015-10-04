package com.pij.crowdmix;

import com.pij.android.NoopCallback;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * @author Pierrejean on 04/10/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class TwitterProxyTest {

    @Mock
    private StatusesService mockService;
    @Mock
    private Callback<List<Tweet>> mockCallback;

    private TwitterProxy tested;

    @Before
    public void setUp() {
        tested = new TwitterProxy();

    }

    @Test
    public void test_ServiceNotSet_NotLoggedIn() {
        tested.setService(null);
        assertFalse(tested.isConnected());
    }

    @Test
    public void test_ServiceSet_LoggedIn() {
        tested.setService(mockService);
        assertTrue(tested.isConnected());
    }

    @Test
    public void test_ServiceSetLoadTweets_CallsCallback() {
        tested.setService(mockService);

        final NoopCallback<List<Tweet>> callback = new NoopCallback<>();
        tested.load(callback);

        verify(mockService).homeTimeline(eq(20), anyLong(), anyLong(), anyBoolean(), anyBoolean(), anyBoolean(),
                                         anyBoolean(), eq(callback));
    }

    @Test
    public void test_ServiceSetUpdate_CallsCallback() {
        tested.setService(mockService);

        final NoopCallback<Tweet> callback = new NoopCallback<>();
        tested.sendUpdate("ha!", callback);

        verify(mockService).update(eq("ha!"), anyLong(), anyBoolean(), anyDouble(), anyDouble(), anyString(),
                                   anyBoolean(), anyBoolean(), anyString(), eq(callback));
    }

    @Test
    public void test_ServiceNotSetLoadTweets_CallsFailure() {
        tested.setService(null);

        tested.load(mockCallback);

        verify(mockCallback).failure(any(TwitterException.class));
    }

    public void test_ServiceSetUpdate_CallsFailure() {
        tested.setService(null);

        tested.sendUpdate("ha!", new NoopCallback<Tweet>());

        verify(mockCallback).failure(any(TwitterException.class));
    }
}