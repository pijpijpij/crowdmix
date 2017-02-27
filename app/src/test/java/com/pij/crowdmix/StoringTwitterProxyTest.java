package com.pij.crowdmix;

import android.support.annotation.NonNull;

import com.twitter.sdk.android.core.models.Tweet;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * @author Pierrejean on 19/10/2015.
 */
public class StoringTwitterProxyTest extends TwitterProxyTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private TweetStore mockTweetStore;

    @NonNull
    @Override
    StoringTwitterProxy createDefaultSut() {
        return new StoringTwitterProxy(new ConcreteTwitterProxy(), mockTweetStore);
    }

    @Test
    public void test_successfulSendUpdate_storesTheTweet() {
        StoringTwitterProxy tested = createDefaultSut();
        tested.setService(mockService);
        setupGoodAnswerToUpdate("hi!");

        tested.sendUpdate("hi!", mockUpdateCallback);

        verify(mockTweetStore).insert(any(Tweet.class));
    }

}