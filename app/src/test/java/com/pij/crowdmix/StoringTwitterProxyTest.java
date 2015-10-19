package com.pij.crowdmix;

import android.support.annotation.NonNull;

import com.twitter.sdk.android.core.models.Tweet;

import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * @author Pierrejean on 19/10/2015.
 */
public class StoringTwitterProxyTest extends TwitterProxyTest {

    @Mock
    private StoringTwitterProxy.TweetStore mockTweetStore;

    @NonNull
    @Override
    StoringTwitterProxy createDefaultSut() {
        return new StoringTwitterProxy(new ConcreteTwitterProxy(), mockTweetStore);
    }

    @Test
    public void test_successfullSendUpdate_storesTheTweet() {
        StoringTwitterProxy tested = createDefaultSut();
        tested.setService(mockService);
        setupGoodAnswerToUpdate("hi!");

        tested.sendUpdate("hi!", mockUpdateCallback);

        verify(mockTweetStore).insert(any(Tweet.class));
    }

}