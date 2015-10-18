package com.pij.crowdmix.list;

import android.support.annotation.NonNull;

import com.pij.crowdmix.TwitterProxy;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Pierrejean on 18/10/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class TweetListPresenterTest {

    @Captor
    ArgumentCaptor<List<Tweet>> tweets;
    @Mock
    private TwitterProxy mockProxy;
    @Mock
    private TweetListView mockView;

    @NonNull
    private TweetListPresenter createDefaultSut() {
        TweetListPresenter tested = new TweetListPresenter(mockProxy);
        tested.setView(mockView);
        return tested;
    }

    @NonNull
    private Answer<List<Tweet>> createGoodAnswer(final List<Tweet> tweets) {
        return new Answer<List<Tweet>>() {
            @Override
            @SuppressWarnings("unchecked")
            public List<Tweet> answer(InvocationOnMock invocationOnMock) throws Throwable {
                ((Callback<List<Tweet>>)invocationOnMock.getArguments()[0]).success(new Result<>(tweets, null));
                return tweets;
            }
        };
    }

    @NonNull
    private Answer<List<Tweet>> createBadAnswer(final String message) {
        return new Answer<List<Tweet>>() {
            @Override
            @SuppressWarnings("unchecked")
            public List<Tweet> answer(InvocationOnMock invocationOnMock) throws Throwable {
                ((Callback<List<Tweet>>)invocationOnMock.getArguments()[0]).failure(new TwitterException(message));
                return Collections.emptyList();
            }
        };
    }


    @Test(expected = NullPointerException.class)
    public void test_NullProxy_Throws() {
        //noinspection ConstantConditions
        new TweetListPresenter(null);
    }

    @Test
    public void test_setView_UpdatesLoginStateOnView() {
        TweetListPresenter tested = new TweetListPresenter(mockProxy);
        when(mockProxy.isConnected()).thenReturn(true);

        tested.setView(mockView);

        verify(mockView).setLoggedIn(true);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_loadTweets_CallsTwitter() {
        TweetListPresenter tested = new TweetListPresenter(mockProxy);

        tested.loadTweets();

        verify(mockProxy).load(any(Callback.class));
    }

    @Test
    public void test_loadTweets_SetsInProgress() {
        TweetListPresenter tested = createDefaultSut();

        tested.loadTweets();

        verify(mockView).setInProgress(true);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_TweetsLoaded_SetsTweetsOnView() {
        TweetListPresenter tested = createDefaultSut();
        final List<Tweet> emptyList = Collections.singletonList(mock(Tweet.class));
        doAnswer(createGoodAnswer(emptyList)).when(mockProxy).load(any(Callback.class));

        tested.loadTweets();

        verify(mockView).setTweets(tweets.capture());
        assertThat(tweets.getValue().size(), equalTo(1));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_TweetsLoaded_ResetsInProgress() {
        TweetListPresenter tested = createDefaultSut();
        final List<Tweet> emptyList = emptyList();
        doAnswer(createGoodAnswer(emptyList)).when(mockProxy).load(any(Callback.class));

        tested.loadTweets();

        verify(mockView).setInProgress(true);
        verify(mockView).setInProgress(false);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_TweetsFailToLoad_SetsMessageOnView() {
        TweetListPresenter tested = createDefaultSut();
        doAnswer(createBadAnswer("Ha!")).when(mockProxy).load(any(Callback.class));

        tested.loadTweets();

        verify(mockView).setError("Ha!");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_TweetsFailToLoad_ResetsInProgress() {
        TweetListPresenter tested = createDefaultSut();
        doAnswer(createBadAnswer("Ha!")).when(mockProxy).load(any(Callback.class));

        tested.loadTweets();

        verify(mockView).setInProgress(true);
        verify(mockView).setInProgress(false);
    }

}
