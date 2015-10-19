package com.pij.crowdmix.list;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.pij.crowdmix.R;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.FixedTweetTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import java.util.List;

import static com.pij.android.Utils.cast;

public class TweetListFragment extends ListFragment {

    private TweetListPresenter.Provider provider;
    private TweetListPresenter presenter;
    private boolean loggedIn;

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

        presenter = provider.getTweetListPresenter();
        presenter.setView(new ViewDelegate());
        startLoadingTweets();
    }

    @Override
    public void onDestroyView() {
        presenter.setView(null);
        presenter = null;
        super.onDestroyView();
    }

    /**
     * Get access to the business layer.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        provider = cast(context, TweetListPresenter.Provider.class);
    }

    /**
     * Reset the active events interface to null.
     */
    @Override
    public void onDetach() {
        provider = null;
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        final MenuItem refresh = menu.findItem(R.id.action_refresh);
        refresh.setEnabled(loggedIn);
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
        presenter.loadTweets();
    }

    private class ViewDelegate implements TweetListView {

        @Override
        public void setInProgress(boolean newValue) {
            setListShown(!newValue);
        }

        @Override
        public void setLoggedIn(boolean newValue) {
            loggedIn = newValue;
            getActivity().invalidateOptionsMenu();
        }

        @Override
        public void setTweets(List<Tweet> tweets) {
            final FixedTweetTimeline timeline = new FixedTweetTimeline.Builder().setTweets(tweets).build();
            final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getActivity()).setTimeline(
                    timeline).build();

            setListAdapter(adapter);
        }

        @Override
        public void setError(String cause) {
            Toast.makeText(getActivity(), "Could not load Tweets: " + cause, Toast.LENGTH_SHORT).show();
        }
    }

}
