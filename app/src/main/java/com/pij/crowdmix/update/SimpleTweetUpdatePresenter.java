package com.pij.crowdmix.update;

import android.support.annotation.NonNull;

import com.pij.crowdmix.TwitterProxy;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import static com.pij.crowdmix.update.TweetUpdateView.NOOP_VIEW;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.Validate.notNull;

public class SimpleTweetUpdatePresenter implements TweetUpdatePresenter {

    private final TwitterProxy tweeter;
    private final PresenterDelegate delegate;
    private TweetUpdateView view;

    public SimpleTweetUpdatePresenter(@NonNull TwitterProxy tweeter) {
        this.view = NOOP_VIEW;
        this.tweeter = notNull(tweeter);
        this.delegate = new PresenterDelegate();
    }

    @Override
    public void setView(TweetUpdateView newValue) {
        view = defaultIfNull(newValue, NOOP_VIEW);
        view.setLoggedIn(tweeter.isConnected());
    }

    @Override
    public void tweet(String message) {
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
