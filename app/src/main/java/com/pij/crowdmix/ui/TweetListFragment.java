package com.pij.crowdmix.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.pij.crowdmix.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.FixedTweetTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import java.util.List;

import static com.pij.android.Utils.cast;

public class TweetListFragment extends ListFragment {

    private static final Events NOOP_EVENTS = new NoopEvents();

    private Events events = NOOP_EVENTS;
    private TweetDisplayer displayer = new TweetDisplayer();

    public TweetListFragment() {
    }

    public static TweetListFragment newInstance() {
        return new TweetListFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        setEmptyText(getString(R.string.empty));

        startLoadingTweets();
    }

    /**
     * Get access to the business layer.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        events = cast(context, Events.class);
    }

    /**
     * Reset the active events interface to the empty implementation.
     */
    @Override
    public void onDetach() {
        events = NOOP_EVENTS;
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        final MenuItem refresh = menu.findItem(R.id.action_refresh);
        refresh.setEnabled(events.isLoggedIn());
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                startLoadingTweets();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startLoadingTweets() {
        events.loadTweets(displayer);
    }

    private void display(List<Tweet> tweets) {
        final FixedTweetTimeline timeline = new FixedTweetTimeline.Builder().setTweets(tweets).build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getActivity()).setTimeline(
                timeline).build();

        setListAdapter(adapter);
    }

    /**
     * A callback interface that all activities containing this fragment must implement. This mechanism allow the
     * "injection" of a timeline into this fragment.
     */
    public interface Events {

        boolean isLoggedIn();

        void loadTweets(Callback<List<Tweet>> callback);
    }

    /**
     * Utility class that provides an empty implementation.
     */
    public static class NoopEvents implements Events {

        /**
         * @return <code>false</code>
         */
        @Override
        public boolean isLoggedIn() {
            return false;
        }

        @Override
        public void loadTweets(
                Callback<List<Tweet>> callback) {
            // Does nothing.
        }

    }

    private class TweetDisplayer extends Callback<List<Tweet>> {
        @Override
        public void success(Result<List<Tweet>> result) {
            display(result.data);
            Log.d("PJC", "Received " + result.data.size() + " tweets to display");
        }

        @Override
        public void failure(TwitterException e) {
            Toast.makeText(getActivity(), "Could not load Tweets: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
