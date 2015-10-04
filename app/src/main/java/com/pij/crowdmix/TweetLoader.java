package com.pij.crowdmix;

import android.content.Context;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import io.fabric.sdk.android.Fabric;

public class TweetLoader {

    private static final String TWITTER_KEY = TwitterSecret.getTwitterKey();
    private static final String TWITTER_SECRET = TwitterSecret.getTwitterSecret();

    private TwitterApiClient client;

    public TweetLoader(Context context) {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(context, new Twitter(authConfig));
    }

    public void setSession(TwitterSession session) {
        client = session == null ? null : Twitter.getApiClient(session);
    }

    public void load(Callback<List<Tweet>> sink) {
        if (client != null) {
            client.getStatusesService().homeTimeline(20, null, null, null, null, true, null, sink);
        }
    }

}
