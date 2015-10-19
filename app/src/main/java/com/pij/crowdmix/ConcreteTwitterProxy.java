package com.pij.crowdmix;

import android.content.Context;
import android.support.annotation.Nullable;

import com.pij.crowdmix.config.TwitterSecret;
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
public class ConcreteTwitterProxy implements TwitterProxy {

    private static final String TWITTER_KEY = TwitterSecret.getTwitterKey();
    private static final String TWITTER_SECRET = TwitterSecret.getTwitterSecret();

    private StatusesService service;

    public ConcreteTwitterProxy() {}

    public static void initialise(Context context) {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(context, new Twitter(authConfig));
    }

    @Override
    public void setSession(@Nullable TwitterSession newValue) {
        StatusesService service = newValue == null ? null : Twitter.getApiClient(newValue).getStatusesService();
        setService(service);
    }

    /**
     * Package access so tests can bypass the transformation of session into service, as this the involve real Twitter
     * API. Powermock could have done the trick, but that's enough for the day.
     */
    @Override
    public void setService(@Nullable StatusesService service) {
        this.service = service;
    }

    @Override
    public boolean isConnected() {
        return service != null;
    }

    @Override
    public void load(Callback<List<Tweet>> sink) {
        if (isConnected()) {
            service.homeTimeline(20, null, null, null, null, true, null, sink);
        } else {
            notConnected(sink);
        }
    }

    @Override
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
