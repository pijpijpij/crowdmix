package com.pij.crowdmix.list;

import android.support.annotation.NonNull;

import com.pij.crowdmix.TweetStore;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import static com.pij.crowdmix.list.TweetListView.NOOP_VIEW;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * @author Pierrejean on 19/10/2015.
 */
public class CachingTweetListPresenter implements TweetListPresenter, TweetStore {

    private final TweetListPresenter decorated;
    private final ViewDelegate delegator = new ViewDelegate();
    private TweetListView view;
    private ArrayList<Tweet> tweets = new ArrayList<>();

    public CachingTweetListPresenter(@NonNull TweetListPresenter decorated) {
        this.decorated = notNull(decorated);
    }

    public void setView(TweetListView newValue) {
        view = defaultIfNull(newValue, NOOP_VIEW);
        decorated.setView(delegator);
    }

    @Override
    public void loadTweets() {
        decorated.loadTweets();
    }

    @Override
    public void insert(Tweet first) {
        tweets.add(0, first);
        view.setTweets(tweets);
    }

    private class ViewDelegate implements TweetListView {

        @Override
        public void setInProgress(boolean newValue) {
            view.setInProgress(newValue);
        }

        @Override
        public void setLoggedIn(boolean newValue) {
            view.setLoggedIn(newValue);
        }

        @Override
        public void setTweets(List<Tweet> newValue) {
            tweets = new ArrayList<>(newValue);
            view.setTweets(tweets);
        }

        @Override
        public void setError(String cause) {
            view.setError(cause);
        }
    }
}
