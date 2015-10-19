package com.pij.crowdmix.list;

import android.support.annotation.NonNull;

import com.twitter.sdk.android.core.models.Tweet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Pierrejean on 18/10/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class CachingTweetListPresenterTest extends TweetListPresenterTest {

    @NonNull
    protected CachingTweetListPresenter createDefaultSut() {
        CachingTweetListPresenter tested = new CachingTweetListPresenter(new SimpleTweetListPresenter(mockProxy));
        tested.setView(mockView);
        return tested;
    }

    @NonNull
    protected TweetListPresenter createViewLessSut() {
        return new CachingTweetListPresenter(new SimpleTweetListPresenter(mockProxy));
    }

    @Test(expected = NullPointerException.class)
    public void test_NullProxy_Throws() {
        //noinspection ConstantConditions
        new CachingTweetListPresenter(null);
    }

    @Test
    public void test_addTweet_updatesView() {
        CachingTweetListPresenter tested = createDefaultSut();

        tested.insert(mock(Tweet.class));
        verify(mockView).setTweets(tweets.capture());
        assertThat(tweets.getValue().size(), equalTo(1));
    }
}
