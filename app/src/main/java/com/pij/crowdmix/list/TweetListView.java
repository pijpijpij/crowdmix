package com.pij.crowdmix.list;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

/**
 * @author Pierrejean on 18/10/2015.
 */
public interface TweetListView {

    void setInProgress(boolean newValue);

    void setLoggedIn(boolean newValue);

    void setTweets(List<Tweet> tweets);

    void setError(String cause);

    /**
     * Utility class that does nothing, whatever the call.
     * @author Pierrejean on 18/10/2015.
     */
    class Noop implements TweetListView {

        @Override
        public void setInProgress(boolean ignored) { }

        @Override
        public void setLoggedIn(boolean ignored) { }

        @Override
        public void setTweets(List<Tweet> ignored) { }

        @Override
        public void setError(String ignored) { }
    }
}
