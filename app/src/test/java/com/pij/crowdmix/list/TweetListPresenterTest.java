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
public abstract class TweetListPresenterTest {

    @Mock
    protected TwitterProxy mockProxy;
    @Mock
    protected TweetListView mockView;
    @Captor
    ArgumentCaptor<List<Tweet>> tweets;

    @NonNull
    protected abstract TweetListPresenter createDefaultSut();

    @NonNull
    protected abstract TweetListPresenter createViewLessSut();

    @SuppressWarnings("unchecked")
    private void createSingleTweetAnswer(Tweet tweet) {
        final List<Tweet> emptyList = Collections.singletonList(tweet);
        doAnswer(createGoodAnswer(emptyList)).when(mockProxy).load(any(Callback.class));
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

    @SuppressWarnings("unchecked")
    private void createBadAnswer(String error) {
        final TwitterException exception = new TwitterException(error);
        doAnswer(createBadAnswer(exception)).when(mockProxy).load(any(Callback.class));
    }

    @NonNull
    private Answer<List<Tweet>> createBadAnswer(final TwitterException exception) {
        return new Answer<List<Tweet>>() {
            @Override
            @SuppressWarnings("unchecked")
            public List<Tweet> answer(InvocationOnMock invocationOnMock) throws Throwable {
                ((Callback<List<Tweet>>)invocationOnMock.getArguments()[0]).failure(exception);
                return Collections.emptyList();
            }
        };
    }


    @Test
    public void test_setView_UpdatesLoginStateOnView() {
        TweetListPresenter tested = createViewLessSut();
        when(mockProxy.isConnected()).thenReturn(true);

        tested.setView(mockView);

        verify(mockView).setLoggedIn(true);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_loadTweets_CallsTwitter() {
        TweetListPresenter tested = createViewLessSut();

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
    public void test_loadTweetsWithoutView_DoesNotThrow() {
        TweetListPresenter tested = createDefaultSut();
        tested.setView(null);

        tested.loadTweets();
    }

    @Test
    public void test_TweetsLoaded_SetsTweetsOnView() {
        TweetListPresenter tested = createDefaultSut();
        createSingleTweetAnswer(mock(Tweet.class));

        tested.loadTweets();

        verify(mockView).setTweets(tweets.capture());
        assertThat(tweets.getValue().size(), equalTo(1));
    }

    @Test
    public void test_TweetsLoaded_ResetsInProgress() {
        TweetListPresenter tested = createDefaultSut();
        createSingleTweetAnswer(mock(Tweet.class));

        tested.loadTweets();

        verify(mockView).setInProgress(true);
        verify(mockView).setInProgress(false);
    }

    @Test
    public void test_TweetsLoadedWithoutView_DoesNotThrow() {
        TweetListPresenter tested = createDefaultSut();
        tested.setView(null);
        createSingleTweetAnswer(mock(Tweet.class));

        tested.loadTweets();
    }

    @Test
    public void test_TweetsFailToLoad_SetsMessageOnView() {
        TweetListPresenter tested = createDefaultSut();
        createBadAnswer("Ha!");

        tested.loadTweets();

        verify(mockView).setError("Ha!");
    }

    @Test
    public void test_TweetsFailToLoad_ResetsInProgress() {
        TweetListPresenter tested = createDefaultSut();
        createBadAnswer("Ha!");

        tested.loadTweets();

        verify(mockView).setInProgress(true);
        verify(mockView).setInProgress(false);
    }

    @Test
    public void test_TweetsFailToLoadWithoutView_DoesNotThrow() {
        TweetListPresenter tested = createDefaultSut();
        tested.setView(null);
        createBadAnswer("H1!");

        tested.loadTweets();
    }

}
