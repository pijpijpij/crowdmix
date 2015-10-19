package com.pij.crowdmix.update;

import android.support.annotation.NonNull;

import com.pij.crowdmix.TwitterProxy;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import static org.apache.commons.lang3.Validate.notNull;

public class TweetUpdatePresenter {

    private final static TweetUpdateView.Noop NOOP_VIEW = new TweetUpdateView.Noop();

    private final TwitterProxy tweeter;
    private final PresenterDelegate delegate;
    private TweetUpdateView view;

    public TweetUpdatePresenter(@NonNull TwitterProxy tweeter) {
        this.view = NOOP_VIEW;
        this.tweeter = notNull(tweeter);
        this.delegate = new PresenterDelegate();
    }

    public void setView(TweetUpdateView newValue) {
        view = newValue == null ? NOOP_VIEW : newValue;
        view.setLoggedIn(tweeter.isConnected());
    }

    void tweet(String message) {
        view.setInProgress(true);
        tweeter.sendUpdate(message, delegate);
    }

    private void onSuccess() {
        view.setUpdate(null);
        view.setInProgress(false);
    }

    private void onFailure(TwitterException e) {
        view.setError(e.getMessage());
        view.setInProgress(false);
    }

    private class PresenterDelegate extends Callback<Tweet> {

        @Override
        public void success(Result<Tweet> result) {
            onSuccess();
        }

        @Override
        public void failure(TwitterException e) {
            onFailure(e);
        }
    }

}
