package com.pij.crowdmix.list;

import android.support.annotation.NonNull;

import com.pij.crowdmix.TwitterProxy;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * @author Pierrejean on 18/10/2015.
 */
public class TweetListPresenter {

    private final static TweetListView.Noop NOOP_VIEW = new TweetListView.Noop();

    private final TwitterProxy tweeter;
    private final PresenterDelegate delegate;
    private TweetListView view;

    public TweetListPresenter(@NonNull TwitterProxy tweeter) {
        this.view = NOOP_VIEW;
        this.tweeter = notNull(tweeter);
        this.delegate = new PresenterDelegate();
    }

    public void setView(TweetListView newValue) {
        view = newValue == null ? NOOP_VIEW : newValue;
        view.setLoggedIn(tweeter.isConnected());
    }

    public void loadTweets() {
        view.setInProgress(true);
        tweeter.load(delegate);
    }

    private void onSuccess(Result<List<Tweet>> result) {
        view.setTweets(result.data);
        view.setInProgress(false);
    }

    private void onFailure(TwitterException e) {
        view.setError(e.getMessage());
        view.setInProgress(false);
    }

    private class PresenterDelegate extends Callback<List<Tweet>> {

        @Override
        public void success(Result<List<Tweet>> result) {
            onSuccess(result);
        }

        @Override
        public void failure(TwitterException e) {
            onFailure(e);
        }
    }

}
