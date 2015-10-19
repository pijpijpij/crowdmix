package com.pij.crowdmix;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pij.android.DelegatingCallback;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.List;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * @author Pierrejean on 19/10/2015.
 */
public class StoringTwitterProxy implements TwitterProxy {

    private final TwitterProxy decorated;
    private final TweetStore tweetStore;

    public StoringTwitterProxy(@NonNull TwitterProxy decorated, @NonNull TweetStore tweetStore) {
        this.decorated = notNull(decorated);
        this.tweetStore = notNull(tweetStore);
    }

    @Override
    public void setSession(@Nullable TwitterSession newValue) {
        decorated.setSession(newValue);
    }

    @Override
    public void setService(@Nullable StatusesService service) {
        decorated.setService(service);
    }

    @Override
    public boolean isConnected() {
        return decorated.isConnected();
    }

    @Override
    public void load(Callback<List<Tweet>> sink) {
        decorated.load(sink);
    }

    @Override
    public void sendUpdate(String message, Callback<Tweet> callback) {
        CallbackDelegate delegate = new CallbackDelegate(callback);
        decorated.sendUpdate(message, delegate);
    }

    private class CallbackDelegate extends DelegatingCallback<Tweet> {

        public CallbackDelegate(@NonNull Callback<Tweet> delegate) {
            super(delegate);
        }

        @Override
        public void success(Result<Tweet> result) {
            super.success(result);
            tweetStore.insert(result.data);
        }

    }
}
