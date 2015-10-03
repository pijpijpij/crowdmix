package com.pij.crowdmix;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.Timeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import static com.pij.android.Utils.cast;

public class TweetListFragment extends ListFragment {

    private static final Events NOOP_EVENTS = new NoopEvents();

    private Events events = NOOP_EVENTS;

    public TweetListFragment() {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setEmptyText(getString(R.string.empty));

        if (savedInstanceState == null) {
            loadTweets(events.getTimeline());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Get access to the timeline.
        events = cast(context, Events.class);
    }

    @Override
    public void onDetach() {
        // Reset the active events interface to the empty implementation.
        events = NOOP_EVENTS;
        super.onDetach();
    }

    private void loadTweets(Timeline<Tweet> timeline) {
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getActivity()).setTimeline(
                timeline).build();
        setListAdapter(adapter);
    }

    /**
     * A callback interface that all activities containing this fragment must implement. This mechanism allow the
     * "injection" of a timeline into this fragment.
     */
    public interface Events {
        /**
         * Get the timeline.
         */
        Timeline<Tweet> getTimeline();
    }

    /**
     * Utility class that provides an empty implementation.
     */
    public static class NoopEvents implements Events {
        @Override
        public Timeline<Tweet> getTimeline() {
            return new EmptyTimeline();
        }

    }

    /**
     * Does not provide any tweet.
     */
    public static class EmptyTimeline implements Timeline<Tweet> {
        @Override
        public void next(Long aLong, Callback<TimelineResult<Tweet>> callback) {
            // Does nothing
        }

        @Override
        public void previous(Long aLong, Callback<TimelineResult<Tweet>> callback) {
            // Does nothing
        }
    }
}
