package com.pij.android;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;

/**
 * Does nothing.
 * @author Pierrejean on 04/10/2015.
 */
public class NoopCallback<T> extends Callback<T> {

    @Override
    public void success(Result<T> ignored) {
    }

    @Override
    public void failure(TwitterException ignored) {
    }
}
