package com.pij.crowdmix.update;

import android.support.annotation.NonNull;

import com.pij.crowdmix.TwitterProxy;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Pierrejean on 18/10/2015.
 */
public abstract class TweetUpdatePresenterTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    protected TwitterProxy mockProxy;
    @Mock
    protected TweetUpdateView mockView;
    @Captor
    ArgumentCaptor<List<Tweet>> tweets;

    @NonNull
    protected abstract TweetUpdatePresenter createDefaultSut();

    @NonNull
    protected abstract TweetUpdatePresenter createViewLessSut();

    @NonNull
    private Answer<Tweet> createGoodAnswer(final Tweet tweet) {
        return new Answer<Tweet>() {
            @Override
            @SuppressWarnings("unchecked")
            public Tweet answer(InvocationOnMock invocationOnMock) throws Throwable {
                ((Callback<Tweet>)invocationOnMock.getArguments()[1]).success(new Result<>(tweet, null));
                return tweet;
            }
        };
    }

    @NonNull
    private Answer<Tweet> createBadAnswer(final String message) {
        return new Answer<Tweet>() {
            @Override
            @SuppressWarnings("unchecked")
            public Tweet answer(InvocationOnMock invocationOnMock) throws Throwable {
                ((Callback<Tweet>)invocationOnMock.getArguments()[1]).failure(new TwitterException(message));
                return null;
            }
        };
    }


    @Test
    public void test_setView_UpdatesLoginStateOnView() {
        TweetUpdatePresenter tested = createViewLessSut();
        when(mockProxy.isConnected()).thenReturn(true);

        tested.setView(mockView);

        verify(mockView).setLoggedIn(true);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_update_CallsTwitter() {
        TweetUpdatePresenter tested = createViewLessSut();

        tested.tweet("Ha!");

        verify(mockProxy).sendUpdate(eq("Ha!"), any(Callback.class));
    }

    @Test
    public void test_update_SetsInProgress() {
        TweetUpdatePresenter tested = createDefaultSut();

        tested.tweet("whatever");

        verify(mockView).setInProgress(true);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_updateSuccessful_ClearsView() {
        TweetUpdatePresenter tested = createDefaultSut();
        final Tweet mockTweet = mock(Tweet.class);
        doAnswer(createGoodAnswer(mockTweet)).when(mockProxy).sendUpdate(eq("zip"), any(Callback.class));

        tested.tweet("zip");

        verify(mockView).setUpdate(null);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_updateSuccessful_ResetsInProgress() {
        TweetUpdatePresenter tested = createDefaultSut();
        final Tweet mockTweet = mock(Tweet.class);
        doAnswer(createGoodAnswer(mockTweet)).when(mockProxy).sendUpdate(eq("zup"), any(Callback.class));

        tested.tweet("zup");

        verify(mockView).setInProgress(true);
        verify(mockView).setInProgress(false);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_updateFails_SetsErrorOnView() {
        TweetUpdatePresenter tested = createDefaultSut();
        doAnswer(createBadAnswer("Ha!")).when(mockProxy).sendUpdate(eq("whatever"), any(Callback.class));

        tested.tweet("whatever");

        verify(mockView).setError("Ha!");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_updateFails_ResetsInProgress() {
        TweetUpdatePresenter tested = createDefaultSut();
        doAnswer(createBadAnswer("Ha!")).when(mockProxy).sendUpdate(eq("whatever"), any(Callback.class));

        tested.tweet("whatever");

        verify(mockView).setInProgress(true);
        verify(mockView).setInProgress(false);
    }

}
