package com.pij.android;

import android.support.annotation.NonNull;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * @author Pierrejean on 04/10/2015.
 */
public class DelegatingCallback<T> extends Callback<T> {
    private final Callback<T> delegate;

    public DelegatingCallback(@NonNull Callback<T> delegate) {this.delegate = notNull(delegate);}

    @Override
    public void success(Result<T> result) {
        delegate.success(result);
    }

    @Override
    public void failure(TwitterException e) {
        delegate.failure(e);
    }
}
