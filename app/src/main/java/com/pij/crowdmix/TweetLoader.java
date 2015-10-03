package com.pij.crowdmix;

import android.content.Context;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.Timeline;
import com.twitter.sdk.android.tweetui.UserTimeline;

import io.fabric.sdk.android.Fabric;

public class TweetLoader {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = TwitterSecret.getTwitterKey();
    private static final String TWITTER_SECRET = TwitterSecret.getTwitterSecret();

    public TweetLoader(Context context) {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(context, new Twitter(authConfig));
    }

    public Timeline<Tweet> getTimeline() {
        return new UserTimeline.Builder().maxItemsPerRequest(20).screenName("fabric").build();
    }
}
