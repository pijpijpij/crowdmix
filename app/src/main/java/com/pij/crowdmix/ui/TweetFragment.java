package com.pij.crowdmix.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pij.crowdmix.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import org.apache.commons.lang3.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.pij.android.Utils.cast;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.stripToNull;

public class TweetFragment extends Fragment {

    @Bind(R.id.message)
    TextView messageView;
    @Bind(R.id.button)
    View button;

    private Callback<Tweet> resultAnalyser = new Callback<Tweet>() {
        @Override
        public void success(Result<Tweet> ignored) {
            messageView.setText(null);
        }

        @Override
        public void failure(TwitterException e) {
            Log.d(getClass().getSimpleName(), "Failed to Tweet", e);
            Toast.makeText(getActivity(), "Failed to Tweet: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
    private Events events;
    private String message;

    public TweetFragment() {
    }

    public static TweetFragment newInstance() {
        return new TweetFragment();
    }

    /**
     * Get access to the business layer.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        events = cast(context, Events.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tweet, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setMessage(null);
    }

    /**
     * Reset the active events interface to null.
     */
    @Override
    public void onDetach() {
        events = null;
        super.onDetach();
    }

    /**
     * Spaces or null are stored as null.
     */
    @OnTextChanged(R.id.message)
    void setMessage(CharSequence newValue) {
        message = stripToNull(defaultIfEmpty(newValue, StringUtils.EMPTY).toString());
        button.setEnabled(message != null);
    }

    @OnClick(R.id.button)
    void postTweet() {
        events.tweet(message, resultAnalyser);
    }

    /**
     * A callback interface that all activities containing this fragment must implement. This mechanism allow the
     * "injection" of a tweeting object into this fragment.
     */
    public interface Events {

        void tweet(String message, Callback<Tweet> callback);
    }

}
