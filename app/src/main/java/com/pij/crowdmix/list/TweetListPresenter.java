package com.pij.crowdmix.list;

/**
 * @author Pierrejean on 19/10/2015.
 */
public interface TweetListPresenter {

    void setView(TweetListView newValue);

    void loadTweets();
}
