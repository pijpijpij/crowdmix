package com.pij.android;

import android.support.annotation.Nullable;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;

/**
 * @author Pierrejean on 10/10/2015.
 */
public class CallbackValve<T> extends Callback<T> {
    private Callback<T> spout;

    public void setSpout(@Nullable Callback<T> spout) {
        this.spout = spout;
    }

    @Override
    public void success(Result<T> result) {
        if (spout != null) spout.success(result);
    }

    @Override
    public void failure(TwitterException e) {
        if (spout != null) spout.failure(e);
    }

}
