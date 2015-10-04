package com.pij.crowdmix;

import android.content.Context;
import android.support.annotation.Nullable;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * Both a factory (with static method and the implementation of the business function.
 */
public class TwitterProxy {

    private static final String TWITTER_KEY = TwitterSecret.getTwitterKey();
    private static final String TWITTER_SECRET = TwitterSecret.getTwitterSecret();

    private StatusesService service;

    public TwitterProxy() {}

    public static void initialise(Context context) {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(context, new Twitter(authConfig));
    }

    public static StatusesService createLoggedInClient(TwitterSession session) {
        return session == null ? null : Twitter.getApiClient(session).getStatusesService();
    }

    public void setService(@Nullable StatusesService service) {
        this.service = service;
    }

    public boolean isConnected() {
        return service != null;
    }

    public void load(Callback<List<Tweet>> sink) {
        if (isConnected()) {
            service.homeTimeline(20, null, null, null, null, true, null, sink);
        } else {
            notConnected(sink);
        }
    }

    public void sendUpdate(String message, Callback<Tweet> callback) {
        if (isConnected()) {
            service.update(message, null, null, null, null, null, null, null, null, callback);
        } else {
            notConnected(callback);
        }
    }

    private <T> void notConnected(Callback<T> callback) {
        callback.failure(new TwitterException("Not connected to Twitter"));
    }

}
