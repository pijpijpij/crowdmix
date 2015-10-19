package com.pij.crowdmix;

import com.twitter.sdk.android.core.models.Tweet;

/**
 * @author Pierrejean on 19/10/2015.
 */
public interface TweetStore {

    void insert(Tweet newTweet);
}
