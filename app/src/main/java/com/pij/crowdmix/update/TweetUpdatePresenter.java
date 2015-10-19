package com.pij.crowdmix.update;

/**
 * @author Pierrejean on 19/10/2015.
 */
public interface TweetUpdatePresenter {

    void setView(TweetUpdateView newValue);

    void tweet(String message);

    interface Provider {

        TweetUpdatePresenter getTweetUpdatePresenter();
    }
}
