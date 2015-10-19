package com.pij.crowdmix;

import android.support.annotation.NonNull;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Pierrejean on 04/10/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class TwitterProxyTest {

    @Mock
    StatusesService mockService;
    @Mock
    Callback<List<Tweet>> mockListCallback;
    @Mock
    Callback<Tweet> mockUpdateCallback;

    private TwitterProxy tested;

    @Before
    public void setUp() {
        tested = createDefaultSut();
    }

    @NonNull
    abstract TwitterProxy createDefaultSut();

    @SuppressWarnings("unchecked")
    protected void setupGoodAnswerToLoad() {
        doAnswer(new Answer<Callback<List<Tweet>>>() {
            public Callback<List<Tweet>> answer(InvocationOnMock invocation) throws Throwable {
                ((Callback<List<Tweet>>)invocation.getArguments()[7]).success(
                        new Result<List<Tweet>>(mock(List.class), null));
                return mockListCallback;
            }
        }).when(mockService)
          .homeTimeline(eq(20), anyLong(), anyLong(), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(),
                        any(Callback.class));
    }

    @SuppressWarnings("unchecked")
    protected void setupGoodAnswerToUpdate(String updateMessage) {
        doAnswer(new Answer<Callback<Tweet>>() {
            public Callback<Tweet> answer(InvocationOnMock invocation) throws Throwable {
                ((Callback<Tweet>)invocation.getArguments()[9]).success(new Result<>(mock(Tweet.class), null));
                return mockUpdateCallback;
            }
        }).when(mockService)
          .update(eq(updateMessage), anyLong(), anyBoolean(), anyDouble(), anyDouble(), anyString(), anyBoolean(),
                  anyBoolean(), anyString(), any(Callback.class));
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
    @SuppressWarnings("unchecked")
    public void test_ServiceSetLoadTweets_CallsCallback() {
        tested.setService(mockService);
        setupGoodAnswerToLoad();

        tested.load(mockListCallback);

        verify(mockListCallback).success(any(Result.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_ServiceSetUpdate_CallsCallback() {
        tested.setService(mockService);
        setupGoodAnswerToUpdate("ha!");

        tested.sendUpdate("ha!", mockUpdateCallback);

        verify(mockUpdateCallback).success(any(Result.class));
    }

    @Test
    public void test_ServiceNotSetLoadTweets_CallsFailure() {
        tested.setService(null);

        tested.load(mockListCallback);

        verify(mockListCallback).failure(any(TwitterException.class));
    }

    @Test
    public void test_ServiceSetUpdate_CallsFailure() {
        tested.setService(null);

        tested.sendUpdate("ha!", mockUpdateCallback);

        verify(mockUpdateCallback).failure(any(TwitterException.class));
    }
}